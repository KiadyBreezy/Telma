package telma.TelmaWS.controllers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import telma.TelmaWS.Utilitaires.DBConnect;
import telma.TelmaWS.Utilitaires.Methodes;
import telma.TelmaWS.classes.MouvementMA;
import telma.TelmaWS.classes.MouvementMC;
import telma.TelmaWS.classes.Offre;
import telma.TelmaWS.classes.Utilisateur;

@RestController
public class UtilisateurServiceController {
	
	 @RequestMapping(value = "/connexion" , method=RequestMethod.POST, produces = "application/json")
	   public ResponseEntity<Object> connexion(
			   @RequestParam("numeroTelephone") String numeroTelephone,
			   @RequestParam("mdp") String mdp) {
		HashMap<String , Object> resp = new HashMap<>();
		try {
			Utilisateur user = new Utilisateur();
			user.setNumeroTelephone(numeroTelephone);
			user.setMdp(mdp);
			String token = user.connect();
			resp.put("status", "success");
			resp.put("data", token);
		}catch(Exception e) {
			resp.put("status", "error");
			resp.put("message", e.getMessage());
		}	
	   return new ResponseEntity<>(resp, HttpStatus.OK);
	   }
	 
	 @RequestMapping(value = "/inscription" , method=RequestMethod.POST, produces = "application/json")
	   public ResponseEntity<Object> inscription(@RequestParam("numeroTelephone") String numeroTelephone , @RequestParam("nom") String nom,  @RequestParam("mdp") String mdp) {
		HashMap<String , Object> resp = new HashMap<>();
		try {
			Utilisateur user = new Utilisateur();
			user.setNom(nom);
			user.setNumeroTelephone(numeroTelephone);
			user.setMdp(mdp);
			user.insertUtilisateur();
			String token = user.connect();
			resp.put("status", "success");
			resp.put("data", token);
		}catch(Exception e) {
			resp.put("status", "error");
			resp.put("message", e.getMessage());
		}	
	    return new ResponseEntity<>(resp, HttpStatus.OK);
	   }
	 
	 @RequestMapping(value = "/deconnexion" , method=RequestMethod.POST, produces = "application/json")
	  public ResponseEntity<Object> deconnexion(@RequestHeader("Authorization") String authorization) {
		 HashMap<String , Object> resp = new HashMap<>();
		 try {
			 String token = Methodes.extractToken(authorization);
			 new Utilisateur().deleteToken(token);
			 resp.put("status", "success");
		 }
		 catch(Exception e) {
				resp.put("status", "error");
				resp.put("message", e.getMessage());
		 }	
		 
		 return new ResponseEntity<>(resp, HttpStatus.OK);
	 }
	 
	 @RequestMapping(value = "/historiqueAppel" , method=RequestMethod.GET, produces = "application/json")
	  public ResponseEntity<Object> getHistoriqueAppel(@RequestHeader("Authorization") String authorization) {
		 HashMap<String , Object> resp = new HashMap<>();
		 try {
			 String token = Methodes.extractToken(authorization);
			 Utilisateur user = new Utilisateur().findUserByToken(token);
			 System.out.println(user.getIdUtilisateur());
			 ArrayList<MouvementMA> historiqueAppel = MouvementMA.getHistoriqueAppel(user.getIdUtilisateur());
			 resp.put("status", "success");
			 resp.put("data", historiqueAppel);
		 }
		 catch(Exception e) {
				resp.put("status", "error");
				resp.put("message", e.getMessage());
		 }	
		 
		 return new ResponseEntity<>(resp, HttpStatus.OK);
	 }
	 
	 @RequestMapping(value = "/faireDepotMvola" , method=RequestMethod.POST, produces = "application/json")
	   public ResponseEntity<Object> faireDepotMvola(@RequestHeader("Authorization") String authorization , @RequestParam("montant") String montant , @RequestParam("typeTransaction") String typeTransaction,  @RequestParam("typeMouvementMC") String typeMouvementMC , @RequestParam("dateMouvementMC") String date) {
		HashMap<String , Object> resp = new HashMap<>();
		try {
			String token = Methodes.extractToken(authorization);
			Utilisateur user = new Utilisateur().findUserByToken(token);
			MouvementMC mouvMC = new MouvementMC();
			mouvMC.setIdUtlisateur(user.getIdUtilisateur());
			mouvMC.setMontant(Double.parseDouble(montant));
			mouvMC.setTypeTransaction(typeTransaction);
			mouvMC.setTypeMouvementMC(typeMouvementMC);
			MouvementMC.faireDepotMvola(mouvMC , date);
			resp.put("status", "success");
		}catch(Exception e) {
			resp.put("status", "error");
			resp.put("message", e.getMessage());
		}	
	    return new ResponseEntity<>(resp, HttpStatus.OK);
	   }
	 
	 @RequestMapping(value = "/achatOffreViaCredit" , method=RequestMethod.POST, produces = "application/json")
	   public ResponseEntity<Object> achatOffreViaCredit(@RequestHeader("Authorization") String authorization , @RequestParam("idOffre") int idOffre , @RequestParam("dateMouvementOffre") String dateMouvementOffre) {
		HashMap<String , Object> resp = new HashMap<>();
		Connection con = null;
		try {
			con = DBConnect.getConnectionPostgres();
			Offre offre = Offre.getOffreById(idOffre);
			Utilisateur user = new Utilisateur().findUserByToken(Methodes.extractToken(authorization));
			double soldeCredit = Utilisateur.getMonCredit(user.getIdUtilisateur(), con);
			if(soldeCredit >= offre.getPrix()) {
				try {
					Offre.achatOffreViaCredit(offre, user.getIdUtilisateur() , dateMouvementOffre);	
					resp.put("status", "success");
					resp.put("message","Votre offre a bien ete changé en "+offre.getNom());
				}catch(Exception e) {
					resp.put("status", "error");
					resp.put("message", e.getMessage());
				}	
			}else {
			resp.put("status", "error");
			resp.put("data" ,"Votre crédit est insuffisant pour le changement d'offre");
			}
		}catch(Exception e) {
			resp.put("status", "error");
			resp.put("message", e.getMessage());
		}	
	    return new ResponseEntity<>(resp, HttpStatus.OK);
	   }
	   
	 @RequestMapping(value = "/achatOffreViaMvola" , method=RequestMethod.POST, produces = "application/json")
	   public ResponseEntity<Object> achatOffreViaMvola(@RequestHeader("Authorization") String authorization , @RequestParam("idOffre") int idOffre , @RequestParam("dateMouvementOffre") String dateMouvementOffre) {
		HashMap<String , Object> resp = new HashMap<>();
		Connection con = null;
		try {
			con = DBConnect.getConnectionPostgres();
			Offre offre = Offre.getOffreById(idOffre);
			Utilisateur user = new Utilisateur().findUserByToken(Methodes.extractToken(authorization));
			double soldeMvola = Utilisateur.getMonSoldeMvola(user.getIdUtilisateur(), con);
			if(soldeMvola >= offre.getPrix()) {
				try {
					Offre.achatOffreViaMvola(offre, user.getIdUtilisateur() , dateMouvementOffre);	
					resp.put("status", "success");
					resp.put("message","Votre offre a bien ete changé en " +offre.getNom());
				}catch(Exception e) {
					resp.put("status", "error");
					resp.put("message", e.getMessage());
				}	
			}else {
			resp.put("status", "error");
			resp.put("data" ,"Votre crédit est insuffisant pour le changement d'offre");
			}
		}catch(Exception e) {
			resp.put("status", "error");
			resp.put("message", e.getMessage());
		}	
	    return new ResponseEntity<>(resp, HttpStatus.OK);
	   }
	 
	 
	 
	 
	 
	 

}
