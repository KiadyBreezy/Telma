package telma.TelmaWS.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import telma.TelmaWS.Utilitaires.DBConnect;
import telma.TelmaWS.Utilitaires.Methodes;

public class Test {
	int id;
	String nom;
	public Test(int id, String nom) {
		super();
		this.id = id;
		this.nom = nom;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public static ArrayList<Test> getTests() throws Exception {
		ArrayList<Test> tests = new ArrayList<>();
	    Connection con = null;
	    Statement stat = null;
	    ResultSet res = null;
	    try {
	       con = DBConnect.getConnectionPostgres();
	       stat = con.createStatement();
	       res = stat.executeQuery("select * from test");
	       while(res.next()){
	    	   tests.add(new Test(res.getInt("id"),res.getString("nom")));
	       }
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(con!=null) con.close();
	         if(stat!=null) stat.close();
	         if(res!=null) res.close();

	     }
	      return tests;
	}
	
	public void insertTest() throws Exception {
		Connection con = null;
	    ResultSet res = null;
	    PreparedStatement st = null;
	    Utilisateur user = null;
	    try {
	       con = DBConnect.getConnectionPostgres();
	       String sql = "insert into test values ("+3+",'test')";
	       st = con.prepareStatement(sql);
	       /* st.setInt(1, this.getId());
	       st.setString(2, this.getNom());*/
	       st.executeUpdate();  
	       con.commit();
	     } catch (Exception e) {        
	         e.printStackTrace();
	     }finally{
	         if(con!=null) con.close();
	         if(st!=null) st.close();
	         if(res!=null) res.close();
	     }
	}
	
}
