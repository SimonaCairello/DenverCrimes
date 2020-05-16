package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String, DefaultWeightedEdge> graph;
	private Map<Long, String> offenseTypes;
	private List<OffenseTypePairs> offenseTypePairs;
	private List<String> best;

	public Model() {
		this.dao = new EventsDao();
		this.offenseTypes = new HashMap<>();
	}
	
	public List<Event> listAllEvents(){
		return dao.listAllEvents();
	}
	
	public Set<String> getAllCategories() {
		List<Event> events = this.listAllEvents();
		Set<String> categories = new HashSet<String>();
		
		for(Event e : events) {
			if(!categories.contains(e.getOffense_category_id()))
				categories.add(e.getOffense_category_id());
		}
		return categories;	
	}
	
	public Set<Integer> getAllMonths() {
		List<Event> events = this.listAllEvents();
		Set<Integer> months = new HashSet<Integer>();
		
		for(Event e : events) {
			if(!months.contains(e.getReported_date().getMonthValue()))
				months.add(e.getReported_date().getMonthValue());
		}
		return months;	
	}
	
	public void generateGraph(Integer month, String offenseCategory) {
		this.graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		dao.loadAllEvents(offenseTypes, month, offenseCategory);
		this.offenseTypePairs = dao.getOffenseTypePairs(month, offenseCategory);
		
		for(String o : offenseTypes.values())
			graph.addVertex(o);

		for(OffenseTypePairs ot : offenseTypePairs) {
			DefaultWeightedEdge edge = this.graph.getEdge(ot.getOffenseType1(), ot.getOffenseType2());
			if(edge==null) {
				Graphs.addEdge(graph, ot.getOffenseType1(), ot.getOffenseType2(), ot.getNeighborhoodNumber());
			}
		}
	}
	
	public Set<OffenseTypePairs> getEdgeForMeanWeight() {
		double avg = 0;
		double sum = 0;
		Integer count = 0;
		Set<OffenseTypePairs> edges = new HashSet<>();
		
		for(DefaultWeightedEdge e : this.graph.edgeSet()) {
			sum += graph.getEdgeWeight(e);
			count++;
		}
		
		avg = sum/count;
		
		for(DefaultWeightedEdge ed : this.graph.edgeSet()) {
			if(graph.getEdgeWeight(ed)>avg)
				edges.add(new OffenseTypePairs(graph.getEdgeSource(ed), graph.getEdgeTarget(ed), (int) graph.getEdgeWeight(ed)));
		}
		
		return edges;
	}
	
	public List<String> getCamminoMax(String v1, String v2) {
		List<String> parziale = new ArrayList<>();
		this.best = new ArrayList<>();
		parziale.add(v1);
		ricorsiva(v2, parziale, 0);
		return this.best;
	}

	private void ricorsiva(String v2, List<String> parziale, int L) {
		if(parziale.get(parziale.size()-1).equals(v2)) {
			if(parziale.size()>this.best.size()) {
				this.best = new ArrayList<>(parziale);
			}
			return;
		}
		
		for(String prossimo : Graphs.neighborListOf(this.graph, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(prossimo)) {
				parziale.add(prossimo);
				this.ricorsiva(v2, parziale, L+1);
				parziale.remove(parziale.size()-1);
			}
		}
	}
}
