package fr.gsb.rv.dr.entites;

import java.util.Date;

public class Visiteur {
    private String matricule;
    private String nom;
    private String prenom;
    private String adresse;
    private int codePostal;
    private String ville;
    private Date dateEmbauche;


    public Visiteur(String matricule, String nom, String prenom, String adresse, int codePostal, String ville, Date dateEmbauche) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
        this.dateEmbauche = dateEmbauche;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Date getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    @Override
    public String toString() {
        return "Visiteur{" +
                "matricule='" + matricule + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", codePostal=" + codePostal +
                ", ville='" + ville + '\'' +
                ", dateEmbauche=" + dateEmbauche +
                '}';
    }
}
