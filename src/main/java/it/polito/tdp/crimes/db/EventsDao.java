package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.model.OffenseTypePairs;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events";
		
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			
			List<Event> list = new ArrayList<>();
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public void loadAllEvents(Map<Long, String> offenseTypes, Integer month, String eventCategory) {
		String sql = "SELECT incident_id, offense_type_id " + 
				"FROM EVENTS " + 
				"WHERE MONTH(reported_date)=? AND offense_category_id=? " + 
				"GROUP BY offense_type_id";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, month);
			st.setString(2, eventCategory);
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				offenseTypes.put(res.getLong("incident_id"), res.getString("offense_type_id"));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<OffenseTypePairs> getOffenseTypePairs(Integer month, String eventCategory) {
		String sql = "SELECT COUNT(DISTINCT (e1.neighborhood_id)) AS n, e1.offense_type_id, e2.offense_type_id " + 
				"FROM EVENTS AS e1, EVENTS AS e2 " + 
				"WHERE e1.neighborhood_id=e2.neighborhood_id AND e1.offense_category_id=? AND MONTH(e1.reported_date)=? AND e2.offense_category_id=? AND MONTH(e2.reported_date)=? AND e1.offense_type_id!=e2.offense_type_id " + 
				"GROUP BY e1.offense_type_id, e2.offense_type_id";
		List<OffenseTypePairs> offenseTypes = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, eventCategory);
			st.setInt(2, month);
			st.setString(3, eventCategory);
			st.setInt(4, month);
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(res.getInt("n")>0)
					offenseTypes.add(new OffenseTypePairs(res.getString("e1.offense_type_id"), res.getString("e2.offense_type_id"), res.getInt("n")));
			}

			conn.close();
			return offenseTypes;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
