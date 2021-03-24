package telma.TelmaWS.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

public class MouvementOffre {
	int idMouvementOffre;
	int idUtilisateur;
	int idOffre;
	Date dateMouvement;
	
	public MouvementOffre(int idMouvementOffre, int idUtilisateur, int idOffre, Date dateMouvement) {
		super();
		this.idMouvementOffre = idMouvementOffre;
		this.idUtilisateur = idUtilisateur;
		this.idOffre = idOffre;
		this.dateMouvement = dateMouvement;
	}
	public MouvementOffre() {
		super();
	}
	public int getIdMouvementOffre() {
		return idMouvementOffre;
	}
	public void setIdMouvementOffre(int idMouvementOffre) {
		this.idMouvementOffre = idMouvementOffre;
	}
	public int getIdUtilisateur() {
		return idUtilisateur;
	}
	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}
	public int getIdOffre() {
		return idOffre;
	}
	public void setIdOffre(int idOffre) {
		this.idOffre = idOffre;
	}
	public Date getDateMouvement() {
		return dateMouvement;
	}
	public void setDateMouvement(Date dateMouvement) {
		this.dateMouvement = dateMouvement;
	}
	
	public static void addMouvementOffre(int idUtilisateur , int idOffre, String dateMouvementOffre, Connection con) throws Exception {
		ResultSet res = null;
	    PreparedStatement st = null;
	    try {
	       String sql = "insert into MouvementOffre (idUtilisateur, idOffre, dateMouvementOffre) values(? , ? , ?)";
	       st = con.prepareStatement(sql);
	       st.setInt(1, idUtilisateur);
	       st.setDouble(2, idOffre); 
	       st.setTimestamp(3, Timestamp.valueOf(dateMouvementOffre));
	       st.execute(); 
	     } catch (Exception e) {
	         throw e;
	     }finally{
	         if(st!=null) st.close();
	         if(res!=null) res.close();
	     }
	}
	
	
}
