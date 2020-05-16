package it.polito.tdp.crimes.model;

public class OffenseTypePairs {
	
	private String offenseType1;
	private String offenseType2;
	private Integer neighborhoodNumber;
	
	public OffenseTypePairs(String offenseType1, String offenseType2, Integer neighborhoodNumber) {
		this.offenseType1 = offenseType1;
		this.offenseType2 = offenseType2;
		this.neighborhoodNumber = neighborhoodNumber;
	}

	public String getOffenseType1() {
		return offenseType1;
	}

	public void setOffenseType1(String offenseType1) {
		this.offenseType1 = offenseType1;
	}

	public String getOffenseType2() {
		return offenseType2;
	}

	public void setOffenseType2(String offenseType2) {
		this.offenseType2 = offenseType2;
	}

	public Integer getNeighborhoodNumber() {
		return neighborhoodNumber;
	}

	public void setNeighborhoodNumber(Integer neighborhoodNumber) {
		this.neighborhoodNumber = neighborhoodNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((offenseType1 == null) ? 0 : offenseType1.hashCode());
		result = prime * result + ((offenseType2 == null) ? 0 : offenseType2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OffenseTypePairs other = (OffenseTypePairs) obj;
		if (offenseType1 == null) {
			if (other.offenseType1 != null)
				return false;
		} else if (!offenseType1.equals(other.offenseType1))
			return false;
		if (offenseType2 == null) {
			if (other.offenseType2 != null)
				return false;
		} else if (!offenseType2.equals(other.offenseType2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return offenseType1 + ", " + offenseType2 + ", " + neighborhoodNumber +"\n";
	}

}
