package telma.TelmaWS.classes;

public class DetailOffre {
	String type;
	double valeur;
	String unite;
	double mop;
	double aop;
	double international;
	String siteSpecifique;
	
	public DetailOffre() {
		
	}
	public void DetailOffreMega(String type, double valeur, String unite, String siteSpecifique) {
		setType(type);
		setValeur(valeur);
		setUnite(unite);
		setSiteSpecifique(siteSpecifique);
	}
	public void DetailOffreAppel(String type, double valeur,String unite ,double mop, double aop, double international) {
		setType(type);
		setValeur(valeur);
		setUnite(unite);
		setMop(mop);
		setAop(aop);
		setInternational(international);
	}
	
	public String getUnite() {
		return unite;
	}
	public void setUnite(String unite) {
		this.unite = unite;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getValeur() {
		return valeur;
	}
	public void setValeur(double valeur) {
		this.valeur = valeur;
	}
	public double getMop() {
		return mop;
	}
	public void setMop(double mop) {
		this.mop = mop;
	}
	public double getAop() {
		return aop;
	}
	public void setAop(double aop) {
		this.aop = aop;
	}
	public double getInternational() {
		return international;
	}
	public void setInternational(double international) {
		this.international = international;
	}
	public String getSiteSpecifique() {
		return siteSpecifique;
	}
	public void setSiteSpecifique(String siteSpecifique) {
		this.siteSpecifique = siteSpecifique;
	}
	
}
