package fr.gsb_rv_dr.technique;

public class ConnexionException extends Exception {

    @Override
    public String getMessage(){
        return "[Nok] Connexion BD" ;
    }

}
