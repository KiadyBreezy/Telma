package telma.TelmaWS.Utilitaires;

import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.text.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import telma.TelmaWS.classes.MouvementMA;
import telma.TelmaWS.classes.Offre;
import telma.TelmaWS.classes.Test;
import telma.TelmaWS.classes.Utilisateur;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/* ArrayList<Test> tests = Test.getTests();
		System.out.println(tests.size());
		System.out.println(tests.get(0).getNom());
		 Utilisateur user =  new Utilisateur("0340985103","Rakoto","123456");
		user.insertUtilisateur();
		/* Utilisateur user1 =  new Utilisateur();
		 user1.setNumeroTelephone("0349985403");
		 user1.setMdp("123456");
		  String token = user1.connect();
		  System.out.println(token);*/
		
		/*Utilisateur user = new Utilisateur().findUserByToken("25a0bbb1abaff47f5f17a8d6a2d5202ba0222a1f");
		System.out.println(user.getIdUtilisateur());
		ArrayList<MouvementMA> historiqueA = MouvementMA.getHistoriqueAppel(user.getIdUtilisateur());
		System.out.println(historiqueA.size());*/
		
		/*MongoDBConfig config = new MongoDBConfig();
		MongoClient client = config.mongoClient();
		MongoDatabase db = client.getDatabase("TelmaMongo");
		
		ArrayList<Offre> listof = new Offre().getOffres(); 
		
		Offre ofr = new Offre().getOffreById(1);
		
		System.out.println( ofr.getNom());
		
		/*FindIterable<org.bson.Document> dc = db.getCollection("Offre").find();
		for(org.bson.Document d: dc) {
			System.out.println(d.get("nom"));
			System.out.println(d.get("prix"));
			System.out.println(((org.bson.Document) d.get("categorie")).get("typeOffre"));
		}*/
		 Connection con = DBConnect.getConnectionPostgres();
		double monSolde = Utilisateur.getMonSoldeMvola(1 , con);
		System.out.print("Mon solde:"+monSolde);
		/*double prix = Offre.getOffreById(1).getPrix();
		System.out.print(prix);*/
		//Offre.achatOffreViaCredit(Offre.getOffreById(1) , 17, "2021-03-20 08:00:00");
		Offre o = Offre.getOffreById(3);
		System.out.println("Prix offre:"+o.getPrix());
	}

}
