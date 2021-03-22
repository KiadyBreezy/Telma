package telma.TelmaWS.classes;

public class Categorie {
		String typeOffre;
		String duration;
		int duree;
		String categorie;
		
	public Categorie() {
			super();
		}
	public Categorie(String typeOffre, String duration, int duree, String categorie) {
			this.typeOffre = typeOffre;
			this.duration = duration;
			this.duree = duree;
			this.categorie = categorie;
		}
	public String getTypeOffre() {
		return typeOffre;
	}
	public void setTypeOffre(String typeOffre) {
		this.typeOffre = typeOffre;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public int getDuree() {
		return duree;
	}
	public void setDuree(int duree) {
		this.duree = duree;
	}
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
		
}
