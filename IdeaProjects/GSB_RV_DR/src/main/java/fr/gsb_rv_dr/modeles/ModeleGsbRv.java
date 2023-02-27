package fr.gsb_rv_dr.modeles;

import fr.gsb_rv_dr.entites.Praticien;
import fr.gsb_rv_dr.entites.RapportVisite;
import fr.gsb_rv_dr.entites.Visiteur;
import fr.gsb_rv_dr.technique.ConnexionBD;
import fr.gsb_rv_dr.technique.ConnexionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModeleGsbRv {

    public static Visiteur seConnecter(String matricule, String mdp) throws ConnexionException {

        // Code de test à compléter

        Connection connexion = ConnexionBD.getConnexion();

        String requete = "SELECT Visiteur.vis_matricule, vis_nom, vis_prenom "
                + "FROM Visiteur "
                + "INNER JOIN Travailler "
                + "ON Visiteur.vis_matricule = Travailler.vis_matricule "
                + "WHERE tra_role LIKE 'Délégué' "
                + "AND jjmmaa =  (SELECT max(jjmmaa) FROM Travailler where tra_role = 'Délégué' ) "
                + "AND Visiteur.vis_matricule LIKE ? "
                + "AND vis_mdp = ?";

        try {
            PreparedStatement requetePreparee = (PreparedStatement) connexion.prepareStatement(requete);
            requetePreparee.setString(1, matricule);
            requetePreparee.setString(2, mdp);
            ResultSet resultat = requetePreparee.executeQuery();
            if (resultat.next()) {
                Visiteur visiteur = new Visiteur(matricule, resultat.getString("vis_nom"), resultat.getString("vis_prenom"));
                requetePreparee.close();
                return visiteur;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
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

    public static List<Visiteur> getVisiteurs() throws ConnexionException {
        Connection connexion = ConnexionBD.getConnexion();

        String requete = "SELECT vis_matricule ,vis_nom, vis_prenom "
                + "FROM Visiteur ";

        try {
            PreparedStatement requetePreparee = (PreparedStatement) connexion.prepareStatement(requete);
            ResultSet resultat = requetePreparee.executeQuery();
            List<Visiteur> listVisiteurs = new ArrayList<>();
            if (resultat.next()) {
                listVisiteurs.add(new Visiteur(resultat.getString("vis_matricule"), resultat.getString("vis_nom"), resultat.getString("vis_prenom")));
                while (resultat.next()) {
                    listVisiteurs.add(new Visiteur(resultat.getString("vis_matricule"), resultat.getString("vis_nom"), resultat.getString("vis_prenom")));
                }
                return listVisiteurs;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public static List<RapportVisite> getRapportVisite(String matricule, int mois, String annee) throws ConnexionException {

        Connection connexion = ConnexionBD.getConnexion();

        String requete = "SELECT RapportVisite.rap_num, RapportVisite.rap_date_visite, RapportVisite.rap_date_redaction, RapportVisite.rap_bilan, RapportVisite.rap_autre_motif, RapportVisite.rap_coef_confiance, RapportVisite.rap_lu, Praticien.pra_num , Praticien.pra_nom, Praticien.pra_ville, Praticien.pra_coefnotoriete, Visiteur.vis_matricule, Visiteur.vis_nom, Visiteur.vis_prenom, Motif.mot_libelle "
                + "FROM RapportVisite "
                + "INNER JOIN Visiteur "
                + "ON RapportVisite.vis_matricule = Visiteur.vis_matricule "
                + "INNER JOIN Praticien "
                + "ON RapportVisite.pra_num = Praticien.pra_num "
                + "INNER JOIN Motif "
                + "ON RapportVisite.mot_num = Motif.mot_num "
                + "WHERE Visiteur.vis_matricule LIKE ? "
                + "AND MONTH(rap_date_visite) = ? "
                + "AND YEAR(rap_date_visite) = ? ";

        try {

            PreparedStatement requetePreparee = (PreparedStatement) connexion.prepareStatement(requete);
            requetePreparee.setString(1, matricule);
            requetePreparee.setInt(2, mois);
            requetePreparee.setString(3, annee);

            ResultSet resultat = requetePreparee.executeQuery();
            List<RapportVisite> listRapportVisite = new ArrayList<>();
            if (resultat.next()) {

                Praticien praticien = new Praticien(resultat.getInt("pra_num"), resultat.getString("pra_nom"), resultat.getString("pra_ville"), resultat.getDouble("pra_coefnotoriete"), resultat.getDate("rap_date_visite").toLocalDate(), resultat.getInt("rap_coef_confiance"));
                Visiteur visiteur = new Visiteur(resultat.getString("vis_matricule"), resultat.getString("vis_nom"), resultat.getString("vis_prenom"));

                listRapportVisite.add(new RapportVisite(resultat.getInt("rap_num"), resultat.getDate("rap_date_visite").toLocalDate(), resultat.getDate("rap_date_redaction").toLocalDate(), resultat.getString("rap_bilan"), resultat.getString("mot_libelle"), resultat.getInt("rap_coef_confiance"), resultat.getBoolean("rap_lu"), visiteur ,praticien));


                while (resultat.next()) {
                    listRapportVisite.add(new RapportVisite(resultat.getInt("rap_num"), resultat.getDate("rap_date_visite").toLocalDate(), resultat.getDate("rap_date_redaction").toLocalDate(), resultat.getString("rap_bilan"), resultat.getString("mot_libelle"), resultat.getInt("rap_coef_confiance"), resultat.getBoolean("rap_lu"), visiteur ,praticien));
                }
                return listRapportVisite;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public static boolean setRapportVisiteLu(String matricule, int numRapport) throws ConnexionException {
        Connection connexion = ConnexionBD.getConnexion();

        String requete = "UPDATE RapportVisite  "
                + "SET rap_lu = true "
                + "WHERE vis_matricule LIKE ? "
                + "AND rap_num = ? ";
        try {
            PreparedStatement requetePreparee = (PreparedStatement) connexion.prepareStatement(requete);
            requetePreparee.setString(1, matricule);
            requetePreparee.setInt(2, numRapport);
            ResultSet resultat = requetePreparee.executeQuery();
            if(resultat != null){
                return true;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return false;


    }

    // insert RapportVisite(vis_matricule, rap_num, rap_date_visite, rap_date_redaction, pra_num , mot_num , rap_coef_confiance , rap_lu) values("a131",2,"2023-01-11","2023-01-16",2,4,2,0);

}
