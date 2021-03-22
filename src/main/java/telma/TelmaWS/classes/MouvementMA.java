package telma.TelmaWS.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import telma.TelmaWS.Utilitaires.DBConnect;

public class MouvementMA {
	int idMouvementMA;
	int idUtilisateur;
	Date dateMouvementMA;
	double duree; // 100s or 50mo
	String destinataire; //site or numberPhone
	String typeMouvementMA;
	int idOffre;
	
	public MouvementMA(int idMouvementMA, int idUtilisateur, Date dateMouvementMA, double duree, String destinataire,
			String typeMouvementMA, int idOffre) {
		super();
		this.idMouvementMA = idMouvementMA;
		this.idUtilisateur = idUtilisateur;
		this.dateMouvementMA = dateMouvementMA;
		this.duree = duree;
		this.destinataire = destinataire;
		this.typeMouvementMA = typeMouvementMA;
		this.idOffre = idOffre;
	}
	
	public MouvementMA() {}
	
	
	public int getIdMouvementMA() {
		return idMouvementMA;
	}

	public void setIdMouvementMA(int idMouvementMA) {
		this.idMouvementMA = idMouvementMA;
	}

	public int getIdUtilisateur() {
		return idUtilisateur;
	}
	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}
	public Date getDateMouvementMA() {
		return dateMouvementMA;
	}
	public void setDateMouvementMA(Date dateMouvementMA) {
		this.dateMouvementMA = dateMouvementMA;
	}
	public double getDuree() {
		return duree;
	}
	public void setDuree(double duree) {
		this.duree = duree;
	}
	public String getDestinataire() {
		return destinataire;
	}
	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}
	public String getTypeMouvementMA() {
		return typeMouvementMA;
	}
	public void setTypeMouvementMA(String typeMouvementMA) {
		this.typeMouvementMA = typeMouvementMA;
	}
	public int getIdOffre() {
		return idOffre;
	}
	public void setIdOffre(int idOffre) {
		this.idOffre = idOffre;
	}
	
	public static ArrayList<MouvementMA> getHistoriqueAppel(int idUtilisateur) throws Exception{
		ArrayList<MouvementMA> historiques = new ArrayList<>();
	    Connection con = null;
	    Statement stat = null;
	    ResultSet res = null;
	    try {
	       con = DBConnect.getConnectionPostgres();
	       stat = con.createStatement();
	       res = stat.executeQuery("select * from V_MouvementAppel where idUtilisateur = "+idUtilisateur);
	       while(res.next()){
	    	   historiques.add(new MouvementMA(res.getInt("idMouvementMA"),res.getInt("idUtilisateur"), res.getTimestamp("dateMouvementMA"),res.getDouble("duree"), res.getString("destinataire"), res.getString("typeMouvementMA"), res.getInt("idOffre")));
	       }
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(con!=null) con.close();
	         if(stat!=null) stat.close();
	         if(res!=null) res.close();

	     }
	      return historiques;
	}
	
	
	
	
	
	
	
}
