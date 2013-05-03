package nl.topicus.examenwarroom.data;

public class Straat {
	private long straatId;

	public Straat(long straatId) {
		this.straatId = straatId;
	}

	public long getStraatId() {
		return straatId;
	}

	public void setStraatId(long straatId) {
		this.straatId = straatId;
	}

	@Override
	public boolean equals(Object obj) {
		Straat straat = (Straat) obj;
		if (straat.getStraatId() == straatId) {
			return true;
		}
		return false;
	}

}
