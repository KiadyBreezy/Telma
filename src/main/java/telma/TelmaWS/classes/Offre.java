package telma.TelmaWS.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import telma.TelmaWS.Utilitaires.DBConnect;
import telma.TelmaWS.Utilitaires.MongoDBConfig;



public class Offre {
	private int idOffre;
	private String nom;
	private double prix;
	private Categorie categorie;
	private ArrayList<DetailOffre> details;
	
	
	public Offre() {
		
	}
	public Offre(int idOffre, String nom, double prix, Categorie categorie, ArrayList<DetailOffre> details) {
		this.idOffre = idOffre;
		this.nom = nom;
		this.prix = prix;
		this.categorie = categorie;
		this.details = details;
	}
	public int getIdOffre() {
		return idOffre;
	}
	public void setIdOffre(int idOffre) {
		this.idOffre = idOffre;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public Categorie getCategorie() {
		return categorie;
	}
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}
	public ArrayList<DetailOffre> getDetails() {
		return details;
	}
	public void setDetails(ArrayList<DetailOffre> details) {
		this.details = details;
	}
	public void insertDetailMega(String type, double valeur, String unite, String siteSpecifique) {
		DetailOffre detail = new DetailOffre();
		detail.DetailOffreMega(type, valeur, unite, siteSpecifique);
		getDetails().add(detail);
	}
	public void insertDetailAppel(String type, double valeur,String unite ,double mop, double aop, double international) {
		DetailOffre detail = new DetailOffre();
		detail.DetailOffreAppel(type, valeur, unite, mop, aop, international);
		getDetails().add(detail);
	}
	
	public static ArrayList<Offre> getOffres(){
		ArrayList<Offre> offres = new ArrayList<Offre>();
		MongoDBConfig config = new MongoDBConfig();
		MongoClient client = config.mongoClient();
		MongoDatabase db = client.getDatabase("TelmaMongo");
		
		FindIterable<org.bson.Document> dc = db.getCollection("Offre").find();
		for(org.bson.Document d: dc) {
			Offre of = new Offre();
			of.setIdOffre((int)d.get("_id"));
			of.setNom((String)d.get("nom"));
			of.setPrix((double)d.get("prix"));
			
			org.bson.Document cat = (org.bson.Document) d.get("categorie");
			Categorie ct = new Categorie();
			ct.setTypeOffre((String)cat.get("typeOffre"));
			ct.setDuration((String)cat.get("duration"));
			ct.setDuree((int)cat.get("duree"));
			ct.setCategorie((String)cat.get("categorie"));
			
			ArrayList<DetailOffre> listd = new ArrayList<>();
			ArrayList<org.bson.Document> details = (ArrayList<org.bson.Document>)d.get("details");
			for (org.bson.Document det : details){
				DetailOffre detail = new DetailOffre();
				detail.setAop((double)det.get("aop"));
				detail.setInternational((double)det.get("international"));
				detail.setMop((double)det.get("mop"));
				detail.setSiteSpecifique((String)det.get("siteSpecifique"));
				detail.setType((String)det.get("type"));
				detail.setUnite((String)det.get("unite"));
				detail.setValeur((double)det.get("valeur"));
				listd.add(detail);
			}
			of.setCategorie(ct);
			of.setDetails(listd);
			offres.add(of);
			
		}
		return offres;
	}
	
	public static Offre getOffreById(int id){

		ArrayList<Offre> offres = new ArrayList<Offre>();
		MongoDBConfig config = new MongoDBConfig();
		MongoClient client = config.mongoClient();
		MongoDatabase db = client.getDatabase("TelmaMongo");
		org.bson.Document doc = new org.bson.Document();
		doc.append("_id", id);
		
		FindIterable<org.bson.Document> dc = db.getCollection("Offre").find(doc);
		for(org.bson.Document d: dc) {
			Offre of = new Offre();
			of.setIdOffre((int)d.get("_id"));
			of.setNom((String)d.get("nom"));
			of.setPrix((double)d.get("prix"));
			
			org.bson.Document cat = (org.bson.Document) d.get("categorie");
			Categorie ct = new Categorie();
			ct.setTypeOffre((String)cat.get("typeOffre"));
			ct.setDuration((String)cat.get("duration"));
			ct.setDuree((int)cat.get("duree"));
			ct.setCategorie((String)cat.get("categorie"));
			
			ArrayList<DetailOffre> listd = new ArrayList<>();
			ArrayList<org.bson.Document> details = (ArrayList<org.bson.Document>)d.get("details");
			for (org.bson.Document det : details){
				DetailOffre detail = new DetailOffre();
				detail.setAop((double)det.get("aop"));
				detail.setInternational((double)det.get("international"));
				detail.setMop((double)det.get("mop"));
				detail.setSiteSpecifique((String)det.get("siteSpecifique"));
				detail.setType((String)det.get("type"));
				detail.setUnite((String)det.get("unite"));
				detail.setValeur((double)det.get("valeur"));
				listd.add(detail);
			}
			of.setCategorie(ct);
			of.setDetails(listd);
			offres.add(of);
		}
		return offres.get(0);
	}
	
	public static void achatOffreViaCredit(Offre of , int idUtilisateur, String dateAchat) throws Exception {
		Connection con = null;   
	    try {
	       con = DBConnect.getConnectionPostgres();
	       MouvementMC.retraitCredit(idUtilisateur, of.getPrix(), con, dateAchat);
	       MouvementOffre.addMouvementOffre(idUtilisateur, of.getIdOffre(), dateAchat, con);
	       con.commit();
	     } catch (Exception e) {
	    	 con.rollback();
	         throw e;
	     }finally{
	         if(con!=null) con.close();
	     }
	}
	
	public static void achatOffreViaMvola(Offre offre, int idUtilisateur, String dateMouvementOffre) throws Exception {
		Connection con = null;   
	    try {
	       con = DBConnect.getConnectionPostgres();
	       MouvementMC.retraitMvola(idUtilisateur, offre.getPrix(), con, dateMouvementOffre);
	       MouvementMC.depotCredit(idUtilisateur, offre.getPrix(), con, dateMouvementOffre);
	       Offre.achatOffreViaCredit(offre, idUtilisateur, dateMouvementOffre);
	       con.commit();
	     } catch (Exception e) {
	    	 con.rollback();
	         throw e;
	     }finally{
	         if(con!=null) con.close();
	     }
		
	}
	
	
}
