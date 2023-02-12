package fr.gsb_rv_dr.modeles;

import fr.gsb_rv_dr.entites.Praticien;
import fr.gsb_rv_dr.entites.Visiteur;
import fr.gsb_rv_dr.technique.ConnexionBD;
import fr.gsb_rv_dr.technique.ConnexionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ModeleGsbRv {
    
    public static Visiteur seConnecter(String matricule , String mdp ) throws ConnexionException {
        
        // Code de test à compléter
        
        Connection connexion = ConnexionBD.getConnexion() ;

        String requete = "SELECT Visiteur.vis_matricule, vis_nom, vis_prenom "
                + "FROM Visiteur "
                + "INNER JOIN Travailler "
                + "ON Visiteur.vis_matricule = Travailler.vis_matricule "
                + "WHERE tra_role LIKE 'Délégué' "
                + "AND jjmmaa =  (SELECT max(jjmmaa) FROM Travailler where tra_role = 'Délégué' ) "
                + "AND Visiteur.vis_matricule LIKE ? "
                + "AND vis_mdp = ?" ;
        
        try {
            PreparedStatement requetePreparee = (PreparedStatement) connexion.prepareStatement( requete ) ;
            requetePreparee.setString( 1 , matricule );
            requetePreparee.setString(2, mdp);
            ResultSet resultat = requetePreparee.executeQuery() ;
            if( resultat.next() ){
                Visiteur visiteur = new Visiteur(matricule,resultat.getString( "vis_nom" ),resultat.getString( "vis_prenom" ));
                requetePreparee.close() ;
                return visiteur ;
            }
            else {
                return null ;
            }
        }
        catch( Exception e ){
            return null ;
        } 
    }


    public static List<Praticien> getPraticiensHesitant() throws ConnexionException {

        Connection connexion = ConnexionBD.getConnexion();

        String requete = "SELECT RapportVisite.pra_num, Praticien.pra_nom, Praticien.pra_ville, Praticien.pra_coefnotoriete, RapportVisite.rap_date_visite, RapportVisite.rap_coef_confiance "
                + "FROM Praticien "
                + "INNER JOIN RapportVisite "
                + "ON RapportVisite.pra_num = Praticien.pra_num "
                + "WHERE rap_coef_confiance < 5 ";

        try {
            PreparedStatement requetePreparee = (PreparedStatement) connexion.prepareStatement(requete);
            ResultSet resultat = requetePreparee.executeQuery();
            List<Praticien> listPraticiens = new ArrayList<Praticien>();
            if (resultat.next()) {
                listPraticiens.add(new Praticien(resultat.getInt("pra_num"), resultat.getString("pra_nom"), resultat.getString("pra_ville"), resultat.getDouble("pra_coefnotoriete"), resultat.getDate("rap_date_visite").toLocalDate(), resultat.getInt("rap_coef_confiance")));
                while (resultat.next()) {
                    listPraticiens.add(new Praticien(resultat.getInt("pra_num"), resultat.getString("pra_nom"), resultat.getString("pra_ville"), resultat.getDouble("pra_coefnotoriete"), resultat.getDate("rap_date_visite").toLocalDate(), resultat.getInt("rap_coef_confiance")));
                }
                return listPraticiens;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;

    }
}
