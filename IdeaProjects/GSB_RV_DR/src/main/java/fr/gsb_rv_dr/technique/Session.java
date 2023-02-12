package fr.gsb_rv_dr.technique;


import fr.gsb_rv_dr.entites.Visiteur;

public class Session {
   static private Session session = null;
   private Visiteur leVisiteur ;

    private Session(Visiteur leVisiteur) {
        this.leVisiteur = leVisiteur;
    }

    public static Session getSession() {
        return session;
    }

    public static void setSession(Session session) {
        Session.session = session;
    }

    public Visiteur getLeVisiteur() {
        return leVisiteur;
    }

    public void setLeVisiteur(Visiteur leVisiteur) {
        this.leVisiteur = leVisiteur;
    }

    static public void ouvrir(Visiteur visiteur){
        Session.session = new Session(visiteur);
    }

    static public void fermer(){
        Session.session = null;
    }

    static public boolean estOuverte(){
        if(Session.session != null){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Session{" +
                "leVisiteur=" + leVisiteur +
                "session=" + session +
                '}';
    }
}
