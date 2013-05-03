package nl.topicus.examenwarroom.data;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Taak {

	private State staat;

	private Date startDate;

	private int progressie;

	private long taskId;

	private Straat straat;

	private String status;

	private String naam;

	private String wachtrijstatus;

	private String formattedBeginTijd;

	private String formattedGemiddeldeVorigJaar;

	private long beginTijd;

	private String categorie;

	private String type;

	private Instelling instelling;

	public Taak() {

	}

	public Instelling getSchool() {
		return instelling;
	}

	public void setSchool(Instelling school) {
		this.instelling = school;
	}

	public State getStaat() {
		return staat;
	}

	public void setStaat(State staat) {
		this.staat = staat;
	}

	@JsonIgnore
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getProgressie() {
		return progressie;
	}

	public void setProgressie(int progressie) {
		this.progressie = progressie;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public Straat getStraat() {
		return straat;
	}

	public void setStraat(Straat straat) {
		this.straat = straat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public String getWachtrijstatus() {
		return wachtrijstatus;
	}

	public void setWachtrijstatus(String wachtrijstatus) {
		this.wachtrijstatus = wachtrijstatus;
	}

	public String getFormattedBeginTijd() {
		return formattedBeginTijd;
	}

	public void setFormattedBeginTijd(String formattedBeginTijd) {
		this.formattedBeginTijd = formattedBeginTijd;
	}

	public String getFormattedGemiddeldeVorigJaar() {
		return formattedGemiddeldeVorigJaar;
	}

	public void setFormattedGemiddeldeVorigJaar(
			String formattedGemiddeldeVorigJaar) {
		this.formattedGemiddeldeVorigJaar = formattedGemiddeldeVorigJaar;
	}

	public long getBeginTijd() {
		return beginTijd;
	}

	public void setBeginTijd(long beginTijd) {
		this.beginTijd = beginTijd;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
