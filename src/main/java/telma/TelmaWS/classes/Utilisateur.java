package telma.TelmaWS.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import ch.qos.logback.core.db.DBHelper;
import telma.TelmaWS.Utilitaires.DBConnect;
import telma.TelmaWS.Utilitaires.Methodes;

public class Utilisateur {
	int idUtilisateur;
	String numeroTelephone;
	String  nom;
	String  mdp;
	
	public Utilisateur(int idUtilisateur, String numeroTelephone, String nom, String mdp) {
		super();
		this.idUtilisateur = idUtilisateur;
		this.numeroTelephone = numeroTelephone;
		this.nom = nom;
		this.mdp = mdp;
	}
	
	public Utilisateur(String numeroTelephone, String nom, String mdp) {
		super();
		this.numeroTelephone = numeroTelephone;
		this.nom = nom;
		this.mdp = mdp;
	}
	
	public Utilisateur() {}

	public int getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public String getNumeroTelephone() {
		return numeroTelephone;
	}

	public void setNumeroTelephone(String numeroTelephone) {
		this.numeroTelephone = numeroTelephone;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	
	public static ArrayList<Utilisateur> getUtilisateurs() throws Exception {
		ArrayList<Utilisateur> utilisateurs = new ArrayList<>();
	    Connection con = null;
	    Statement stat = null;
	    ResultSet res = null;
	    try {
	       con = DBConnect.getConnectionPostgres();
	       stat = con.createStatement();
	       res = stat.executeQuery("select * from utilisateur");
	       while(res.next()){
	           utilisateurs.add(new Utilisateur(res.getInt("idUtilisateur"),res.getString("numeroTelephone"), res.getString("nom"),res.getString("mdp")));
	       }
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(con!=null) con.close();
	         if(stat!=null) stat.close();
	         if(res!=null) res.close();

	     }
	      return utilisateurs;
	}
	
	public String connect() throws Exception {
		Connection con = null;
	    ResultSet res = null;
	    PreparedStatement st = null;
	    Utilisateur user = null;
	    String token = "";
	    try {
	       con = DBConnect.getConnectionPostgres();
	       String sql = "select * from utilisateur where numeroTelephone =  ? and mdp = ?";
	       st = con.prepareStatement(sql);
	       st.setString(1, this.getNumeroTelephone());
	       st.setString(2, Methodes.encryptPassword(this.getMdp()));
	       res = st.executeQuery();
	       while(res.next()){
	    	   user = new Utilisateur(res.getInt("idUtilisateur"), res.getString("numeroTelephone"), res.getString("nom"), res.getString("mdp"));
	       }
	        token = user.generateToken();
	        user.insertToken(con,token);
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(con!=null) con.close();
	         if(st!=null) st.close();
	         if(res!=null) res.close();

	     }
	      return token;
	}
	
	private void insertToken(Connection con , String token) throws Exception {
	    ResultSet res = null;
	    PreparedStatement st = null;
	    try {
	       String sql = "insert into token (idUtilisateur , tokenValue) values(? , ?)";
	       st = con.prepareStatement(sql);
	       st.setInt(1, this.getIdUtilisateur());
	       st.setString(2, this.generateToken());
	       st.execute(); 
	       con.commit();
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(st!=null) st.close();
	         if(res!=null) res.close();
	     }
	}
	
	public void deleteToken(String token) throws Exception {
	    ResultSet res = null;
	    PreparedStatement st = null;
	    Connection con = null;
	    try {
	       con = DBConnect.getConnectionPostgres(); 
	       System.out.println(token);
	       String sql = "delete from token where tokenValue = ? ";
	       st = con.prepareStatement(sql);
	       st.setString(1, token);
	       st.execute(); 
	       con.commit();
	     } catch (Exception e) {
	         throw e;
	     }finally{
	    	 if(con!=null) st.close();
	         if(st!=null) st.close();
	         if(res!=null) res.close();
	     }
	}

	public String generateToken() {
		String token = Methodes.encryptPassword(this.getNumeroTelephone()+new Date().toString());
		return token;
	}
	
	public void insertUtilisateur() throws Exception {
		Connection con = null;
	    ResultSet res = null;
	    PreparedStatement st = null;
	    try {
	       con = DBConnect.getConnectionPostgres();
	       String sql = "insert into utilisateur (numeroTelephone, nom, mdp) values(? , ? , ?)";
	       st = con.prepareStatement(sql);
	       st.setString(1, this.getNumeroTelephone());
	       st.setString(2, this.getNom());
	       System.out.println(this.getMdp());
	       st.setString(3, Methodes.encryptPassword(this.getMdp()));
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
	
	public Utilisateur findUserByToken(String token) throws Exception {
		Connection con = null;
	    ResultSet res = null;
	    PreparedStatement st = null;
	    Utilisateur user = null;
	    try {
	       con = DBConnect.getConnectionPostgres();
	       String sql = "select * from v_user_token where tokenValue =  ? ";
	       st = con.prepareStatement(sql);
	       st.setString(1, token);
	       res = st.executeQuery();
	       while(res.next()){
	    	   user = new Utilisateur(res.getInt("idUtilisateur"), res.getString("numeroTelephone"), res.getString("nom"), res.getString("mdp"));
	       }
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(con!=null) con.close();
	         if(st!=null) st.close();
	         if(res!=null) res.close();

	     }
	      return user;
	}
	
	public static double getMonCredit(int idUtilisateur , Connection con) throws Exception {
		double credit = 0;
	    ResultSet res = null;
	    PreparedStatement st = null;
	    try {
	       String sql = "select credit from v_soldeCredit where idUtilisateur =  ? ";
	       st = con.prepareStatement(sql);
	       st.setInt(1, idUtilisateur);
	       res = st.executeQuery();
	       while(res.next()){
	    	   credit = res.getDouble("credit");
	       }	  
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(st!=null) st.close();
	         if(res!=null) res.close();
	     }
		
		return credit;	
	}
	
	public static double getMonSoldeMvola(int idUtilisateur, Connection con) throws Exception {
		double credit = 0;
	    ResultSet res = null;
	    PreparedStatement st = null;
	    try {
	       String sql = "select mvola from v_soldeMvola where idUtilisateur =  ? ";
	       st = con.prepareStatement(sql);
	       st.setInt(1, idUtilisateur);
	       res = st.executeQuery();
	       while(res.next()){
	    	   credit = res.getDouble("mvola");
	       }	  
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(st!=null) st.close();
	         if(res!=null) res.close();
	     }		
		return credit;	
	}
	
	
	
}
