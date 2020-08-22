package com.sockets;

import java.sql.*;

/**
 * Created by Meave Dregonhive on 30/04/2018.
 */
public class User {
    private String Username;
    private String Password;
    private String Email;
    private String Tel;

    public User(String username, String password, String email, String tel) {
        Username = username;
        Password = password;
        Email = email;
        Tel = tel;
    }

    public User(String email) {
        Email = email;
    }

    public void ajoutUser() {
        Connection conn = ConnectionManager.getConnection();
        try {
            System.out.println("///Connection established.");
            String query = "INSERT INTO USERS VALUES (?,?,?,?,?,?)";
            int id = 0;
            PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, Username);
            statement.setString(2, Password);
            statement.setString(3, Email);
            statement.setString(4, Tel);
            statement.setInt(5, 1);
            statement.setInt(6, 1);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException exc) {
            System.out.println(exc);
            exc.printStackTrace();
            System.out.println("Connection failed !");
        }
    }

    public void supprimerUser() {
        try(Statement state = ConnectionManager.getConnection().createStatement())
        {

            String test = "DELETE FROM USERS WHERE (EMAIL = \'" + Email + "\');";
            System.out.println("Query " + test);
            state.executeUpdate(test);
        }
        catch(Exception e){
            System.out.println("\n Database Failure! (method = delete User)");
            e.printStackTrace();
        }
    }


    public void RecupCoords() {

        try(
                Statement state = ConnectionManager.getConnection().createStatement())

        {
            String test = "SELECT * FROM USERS WHERE EMAIL = \"" + Email + "\";";
            System.out.println(test);
            ResultSet result = state.executeQuery(test);
            if (result.next()) {
                String user = result.getObject("USERNAME").toString();
                String password = result.getObject("PASSWORD").toString();
                String text = "Cher client, \n\n\tSuite à votre requéte, veuillez trouver ci-joints les coordonnées de connexion associées à votre Compte PharmaG:\n\n\tNom d'utilisateur: " + user + "\n\tMot de passe: " + password + "\n\n Cordialement, \n PharmaG, Solution du gaspillage de médicaments.";
                Mailer mail = new Mailer(Email, text);
                mail.run();
            }

        }
        catch(Exception e)

        {
            System.out.println("\n Database Failure! (Class = Connect2.java Pharma Research)");
            e.printStackTrace();
        }
    }

    public void Welcome()
    {
        String text = "Cher Utilisateur, le réseau PharmaG vous souhaite la bienvenue et est ravi de vous compter parmi les phrmaciens qui agissent pour la société et l'environement Algeriens,\n\n Cordialement, \n PharmaG, Solution du gaspillage de médicaments.";
        Mailer mail = new Mailer(Email, text);
        mail.run();
    }
}
