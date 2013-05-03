package nl.topicus.examenwarroom.data;

public class Instelling {
	public Instelling(String naam, long instellingId, String brin,
			String adres, String postcode, String plaats,
			String telefoonnummer, int aantalExamenLeerlingen) {
		super();
		this.naam = naam;
		this.instellingId = instellingId;
		this.brin = brin;
		this.adres = adres;
		this.postcode = postcode;
		this.plaats = plaats;
		this.telefoonnummer = telefoonnummer;
		this.aantalExamenLeerlingen = aantalExamenLeerlingen;
	}

	private String naam;

	private long instellingId;

	private String brin;

	private String adres;

	private String postcode;

	private String plaats;

	private String telefoonnummer;

	private int aantalExamenLeerlingen;

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public long getInstellingId() {
		return instellingId;
	}

	public void setInstellingId(long instellingId) {
		this.instellingId = instellingId;
	}

	public String getBrin() {
		return brin;
	}

	public void setBrin(String brin) {
		this.brin = brin;
	}

	public String getAdres() {
		return adres;
	}

	public void setAdres(String adres) {
		this.adres = adres;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getPlaats() {
		return plaats;
	}

	public void setPlaats(String plaats) {
		this.plaats = plaats;
	}

	public String getTelefoonnummer() {
		return telefoonnummer;
	}

	public void setTelefoonnummer(String telefoonnummer) {
		this.telefoonnummer = telefoonnummer;
	}

	public int getAantalExamenLeerlingen() {
		return aantalExamenLeerlingen;
	}

	public void setAantalExamenLeerlingen(int aantalExamenLeerlingen) {
		this.aantalExamenLeerlingen = aantalExamenLeerlingen;
	}
}
