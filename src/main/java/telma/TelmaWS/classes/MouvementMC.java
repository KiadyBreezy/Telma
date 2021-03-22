package telma.TelmaWS.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import telma.TelmaWS.Utilitaires.DBConnect;
import telma.TelmaWS.Utilitaires.Methodes;

public class MouvementMC {
	int idMouvementMC;
	int idUtlisateur;
	double montant;
	String typeTransaction;
	String typeMouvementMC;
	Date dateMouvementMC;
	Date dateDateAcceptation;
	public MouvementMC(int idMouvementMC, int idUtlisateur, double montant, String typeTransaction,
			String typeMouvementMC, Date dateMouvementMC, Date dateDateAcceptation) {
		super();
		this.idMouvementMC = idMouvementMC;
		this.idUtlisateur = idUtlisateur;
		this.montant = montant;
		this.typeTransaction = typeTransaction;
		this.typeMouvementMC = typeMouvementMC;
		this.dateMouvementMC = dateMouvementMC;
		this.dateDateAcceptation = dateDateAcceptation;
	}
	public MouvementMC() {
	}
	public int getIdMouvementMC() {
		return idMouvementMC;
	}
	public void setIdMouvementMC(int idMouvementMC) {
		this.idMouvementMC = idMouvementMC;
	}
	public int getIdUtlisateur() {
		return idUtlisateur;
	}
	public void setIdUtlisateur(int idUtlisateur) {
		this.idUtlisateur = idUtlisateur;
	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}
	public String getTypeTransaction() {
		return typeTransaction;
	}
	public void setTypeTransaction(String typeTransaction) {
		this.typeTransaction = typeTransaction;
	}
	public String getTypeMouvementMC() {
		return typeMouvementMC;
	}
	public void setTypeMouvementMC(String typeMouvementMC) {
		this.typeMouvementMC = typeMouvementMC;
	}
	public Date getDateMouvementMC() {
		return dateMouvementMC;
	}
	public void setDateMouvementMC(Date dateMouvementMC) {
		this.dateMouvementMC = dateMouvementMC;
	}
	public Date getDateDateAcceptation() {
		return dateDateAcceptation;
	}
	public void setDateDateAcceptation(Date dateDateAcceptation) {
		this.dateDateAcceptation = dateDateAcceptation;
	}
	
	public static void faireDepotMvola(MouvementMC depotMvola , String dateMouvementMC) throws Exception {
		Connection con = null;
	    ResultSet res = null;
	    PreparedStatement st = null;
	    try {
	       con = DBConnect.getConnectionPostgres();
	       String sql = "insert into MouvementMC (idUtilisateur, montant, typeTransaction,\r\n"
	       		+ "			 typeMouvementMC,  dateMouvementMC) values(? , ? , ? , ? , ? )";
	       st = con.prepareStatement(sql);
	       st.setInt(1, depotMvola.getIdUtlisateur());
	       st.setDouble(2, depotMvola.getMontant());
	       st.setString(3, depotMvola.getTypeTransaction());
	       st.setString(4, depotMvola.getTypeMouvementMC());
	       st.setTimestamp(5, Timestamp.valueOf(dateMouvementMC));
	       st.execute(); 
	       con.commit();
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(con!=null) con.close();
	         if(st!=null) st.close();
	         if(res!=null) res.close();
	     }
	}
	
	public static void retraitCredit(int idUtilisateur , double montant, Connection con , String dateMouvementMC) throws Exception {
	    ResultSet res = null;
	    PreparedStatement st = null;
	    try {
	       String sql = "insert into MouvementMC (idUtilisateur, montant, typeTransaction,\r\n"
	       		+ "			 typeMouvementMC,  dateMouvementMC, dateAcceptation) values(? , ? , ? , ? , ?, ? )";
	       st = con.prepareStatement(sql);
	       st.setInt(1, idUtilisateur);
	       st.setDouble(2, montant);
	       st.setString(3, "retrait");
	       st.setString(4, "credit");
	       st.setTimestamp(5, Timestamp.valueOf(dateMouvementMC));
	       st.setTimestamp(6, Timestamp.valueOf(dateMouvementMC));
	       st.execute(); 
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(st!=null) st.close();
	         if(res!=null) res.close();
	     }
	}
	
	public static void retraitMvola(int idUtilisateur , double montant, Connection con , String dateMouvementMC) throws Exception {
	    ResultSet res = null;
	    PreparedStatement st = null;
	    try {
	       String sql = "insert into MouvementMC (idUtilisateur, montant, typeTransaction,\r\n"
	       		+ "			 typeMouvementMC,  dateMouvementMC, dateAcceptation) values(? , ? , ? , ? , ?, ? )";
	       st = con.prepareStatement(sql);
	       st.setInt(1, idUtilisateur);
	       st.setDouble(2, montant);
	       st.setString(3, "retrait");
	       st.setString(4, "mvola");
	       st.setTimestamp(5, Timestamp.valueOf(dateMouvementMC));
	       st.setTimestamp(6, Timestamp.valueOf(dateMouvementMC));
	       st.execute(); 
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(st!=null) st.close();
	         if(res!=null) res.close();
	     }
	}
	
	public static void depotCredit(int idUtilisateur , double montant, Connection con , String dateMouvementMC) throws Exception {
	    ResultSet res = null;
	    PreparedStatement st = null;
	    try {
	       String sql = "insert into MouvementMC (idUtilisateur, montant, typeTransaction,\r\n"
	       		+ "			 typeMouvementMC,  dateMouvementMC, dateAcceptation) values(? , ? , ? , ? , ?, ? )";
	       st = con.prepareStatement(sql);
	       st.setInt(1, idUtilisateur);
	       st.setDouble(2, montant);
	       st.setString(3, "depot");
	       st.setString(4, "credit");
	       st.setTimestamp(5, Timestamp.valueOf(dateMouvementMC));
	       st.setTimestamp(6, Timestamp.valueOf(dateMouvementMC));
	       st.execute(); 
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(st!=null) st.close();
	         if(res!=null) res.close();
	     }
	}
	
	
	
	
	

	
	
	
}	
