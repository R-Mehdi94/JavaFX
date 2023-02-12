package fr.gsb_rv_dr.technique;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnexionBD {

    private static String dbURL = "jdbc:mariadb://localhost:3306/gsbrv" ;
    private static String user = "adminGsb" ;
    private static String password = "azerty" ;

    private static Connection connexion = null ;

    private ConnexionBD() throws ConnexionException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connexion = (Connection) DriverManager.getConnection(dbURL, user, password) ;
        }
        catch(SQLException | ClassNotFoundException e ) {
            e.printStackTrace();
        }
    }
    public static Connection getConnexion() throws ConnexionException {
        if( connexion == null ){
            new ConnexionBD() ;
        }
        return connexion ;
    }
}
