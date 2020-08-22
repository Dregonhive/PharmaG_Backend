package com.sockets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

public class Connect {
    public static ArrayList<ProduitRestitution> rechercheRestitution(String champ, String critere){

        ArrayList<ProduitRestitution> moment = null;
        try(Statement state = ConnectionManager.getConnection().createStatement())
        {

            String requete = "SELECT * FROM RESTITUTION WHERE " +champ+" LIKE \"" + critere + "\"";
            ResultSet result = state.executeQuery(requete);
            ResultSetMetaData resultMeta = result.getMetaData();
            /*result.next();
            if (result.getObject(1) != null) {
                System.out.print("2\t" + result.getObject(1).toString() + "\t |");

            }*/
            while (result.next()) {
                moment.add(new ProduitRestitution(Integer.parseInt(result.getObject("ID").toString()), result.getObject("NOM").toString(), result.getObject("FORME").toString(), result.getObject("DOSAGE").toString()
                        , Integer.parseInt(result.getObject("QUANTITE").toString()), (java.sql.Date)result.getObject("PEREMPTION"), Integer.parseInt(result.getObject("IDPHARMA").toString()), result.getObject("REMARQUES").toString()));
            }
            result.close();

        }
        catch(Exception e){
            System.out.println("\n Database Failure! (Class = recherche Restitution Connect.java)");
            e.printStackTrace();
        }
        System.out.println("moment = " + moment.get(0).getNom_commercial());
        return moment;

    }

    public static ProduitRestitution rechercheRestitutionID(int ID)
    {

        ProduitRestitution moment = null;
        try(Statement state = ConnectionManager.getConnection().createStatement())
        {

            String requete = "SELECT * FROM RESTITUTION WHERE ID LIKE \"" + ID + "\"";
            ResultSet result = state.executeQuery(requete);
            ResultSetMetaData resultMeta = result.getMetaData();
            if(result.next()) {
                moment = new ProduitRestitution(Integer.parseInt(result.getObject("ID").toString()),
                        result.getObject("NOM").toString(), result.getObject("FORME").toString(),
                        result.getObject("DOSAGE").toString(), Integer.parseInt(result.getObject("QUANTITE").toString()),
                        (java.sql.Date)result.getObject("PEREMPTION"), Integer.parseInt(result.getObject("IDPHARMA").toString()),
                        result.getObject("REMARQUES").toString());
            }
            result.close();

        }
        catch(Exception e){
            System.out.println("\n Database Failure! (Class = recherche Restitution Connect.java)");
            e.printStackTrace();
        }
        return moment;

    }

    public static void UpdateRestitutionID(int ID, int Quantite) //par ID
    {
        try(Statement state = ConnectionManager.getConnection().createStatement())
        {

            String test = "UPDATE RESTITUTION SET QUANTITE = \"" + Quantite + "\" WHERE (ID LIKE  \"" + ID + "\");";
            System.out.println("Query " + test);
            state.executeUpdate(test);
        }
        catch(Exception e){
            System.out.println("\n Database Failure! (Class = update Restitution par ID Connect.java)");
            e.printStackTrace();
        }

    }
    public static void DeleteRestitutionID(int ID) //par ID
    {
        try(Statement state = ConnectionManager.getConnection().createStatement())
        {

            String test = "DELETE FROM RESTITUTION WHERE (ID LIKE " + ID + ");";
            System.out.println("Query " + test);
            state.executeUpdate(test);
        }
        catch(Exception e){
            System.out.println("\n Database Failure! (Class = delete Restitution par IDConnect.java)");
            e.printStackTrace();
        }

    }

    public static void Restore()
    {
        try(Statement state = ConnectionManager.getConnection().createStatement())
        {

            String test = "TRUNCATE Requests" ;
            System.out.println("Query " + test);
            state.executeUpdate(test);
        }
        catch(Exception e){
            System.out.println("\n Database Failure! (Class = restore Connect.java)");
            e.printStackTrace();
        }

    }

    public static void AddNews(int IDpharma, String Commande) {
        try (Statement state = ConnectionManager.getConnection().createStatement()) {

            String test = "INSERT INTO NEWS (IDPHARMA, COMMANDE) VALUES ('" + IDpharma + "', '" + Commande + "');";
            System.out.println("Query " + test);
            state.executeUpdate(test);
            state.close();
        } catch (Exception e) {
            System.out.println("\n Database Failure! (Class = add attente Restitution Connect.java)");
            e.printStackTrace();
        }
    }

    public static int AddAttente(ProduitRestitution produit)
    {
        try(Statement state = ConnectionManager.getConnection().createStatement())
        {

            String test = "INSERT INTO ATTENTE (NOM, FORME, DOSAGE, QUANTITE, PEREMPTION, IDPHARMA, ID, REMARQUES) VALUES ('" + produit.getNom_commercial() + "', '"+ produit.getForme() + "', '"+ produit.getDosage() + "', " + produit.getQuantite() + ", '" + produit.getDatePeremption() + "' ," + produit.getIdPharmacie() + ", "+ produit.getID() + ", '"+ produit.getRemarque() + "');";
            System.out.println("Query " + test);
            state.executeUpdate(test, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = state.getGeneratedKeys();

            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
                System.out.println("ID request = " + id);
            }

            state.close();
            return id;
        }
        catch(Exception e){
            System.out.println("\n Database Failure! (Class = add attente Restitution Connect.java)");
            e.printStackTrace();
        }
        return -1;

    }

    public static Pharmacie recherchePharmacieID(int ID)
    {

        Pharmacie moment = null;
        try(Statement state = ConnectionManager.getConnection().createStatement())
        {

            String requete = "SELECT * FROM PHARMACIES WHERE ID LIKE \"" + ID + "\"";
            ResultSet result = state.executeQuery(requete);
            ResultSetMetaData resultMeta = result.getMetaData();
            if(result.next()) {
                moment = new Pharmacie(Integer.parseInt(result.getObject("ID").toString()), result.getObject("NOM").toString(),
                        result.getObject("ADRESSE").toString(), result.getObject("EMAIL").toString(), result.getObject("TEL").toString()
                        , Float.parseFloat(result.getObject("LONGITUDE").toString()), Float.parseFloat(result.getObject("LATITUDE").toString()),
                        Integer.parseInt(result.getObject("IN").toString()), Integer.parseInt(result.getObject("OUT").toString()));
            }
            result.close();

        }
        catch(Exception e){
            System.out.println("\n Database Failure! (Class = recherche Restitution Connect.java)");
            e.printStackTrace();
        }
        return moment;

    }

    public static void MajBDDMedocs()
    {
        try (PrintWriter printWriter = new PrintWriter(STATICS.BDD))
        {
            ArrayList<ProduitRestitution> moment = new ArrayList<>();
            try(Statement state = ConnectionManager.getConnection().createStatement()) {

                String requete = "SELECT * FROM RESTITUTION;";
                ResultSet result = state.executeQuery(requete);
                while (result.next()) {
                    moment.add(new ProduitRestitution(Integer.parseInt(result.getObject("ID").toString()), result.getObject("NOM").toString(), result.getObject("FORME").toString(), result.getObject("DOSAGE").toString()
                            , Integer.parseInt(result.getObject("QUANTITE").toString()), (java.sql.Date) result.getObject("PEREMPTION"), Integer.parseInt(result.getObject("IDPHARMA").toString()), result.getObject("REMARQUES").toString()));
                }
                result.close();
            }
            for (ProduitRestitution produit : moment)
            {
                System.out.println(produit.toString());
                printWriter.println(";" + produit.getID() + ";;" + produit.getNom_commercial() + ";;" + produit.getDosage() + ";;" + produit.getForme() + ";;" + produit.getIdPharmacie() + ";;" + produit.getQuantite() + ";;" + produit.getDatePeremption().format(STATICS.DATEFORMAT) + ";;" + produit.getRemarque() + ";");
            }
        }catch (Exception e)
        {
            System.out.println("ERREUR de MAJ de BDD de Medicaments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void MajBDDPharmacies()
    {
        try (PrintWriter printWriter = new PrintWriter(STATICS.BDDPharmacies))
        {
            ArrayList<Pharmacie> moment = new ArrayList<>();
            try(Statement state = ConnectionManager.getConnection().createStatement()) {

                String requete = "SELECT * FROM PHARMACIES;";
                ResultSet result = state.executeQuery(requete);
                while (result.next()) {
                    moment.add(new Pharmacie(Integer.parseInt(result.getObject("ID").toString()), result.getObject("NOM").toString(), result.getObject("ADRESSE").toString(), result.getObject("EMAIL").toString(), result.getObject("TEL").toString()
                            , Float.parseFloat(result.getObject("LONGITUDE").toString()), Float.parseFloat(result.getObject("LATITUDE").toString()), Integer.parseInt(result.getObject("IN").toString()), Integer.parseInt(result.getObject("OUT").toString())));
                }
                result.close();
            }
            for (Pharmacie pharmacie : moment)
            {
                System.out.println(pharmacie.toString());
                printWriter.println(";" + pharmacie.getID() + ";;" + pharmacie.getNomPharma() + ";;" + pharmacie.getAdresse() + ";;" + pharmacie.getLONGITUDE() + ";;" + pharmacie.getLATITUDE() + ";;" + pharmacie.getEmailPrincip() + ";;" + pharmacie.getNumTelephone() +  ";");
            }
        }catch (Exception e)
        {
            System.out.println("ERREUR de MAJ de BDD des Pharmacies ");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /*public static Pharmacie recherchePharma(int ID){

        Pharmacie moment = null;
        try(Statement state = ConnectionManager.getConnection().createStatement())
        {

            String test = "SELECT * FROM AUTRE_PHARM WHERE ID =" + ID;
            ResultSet result = state.executeQuery(test);
            ResultSetMetaData resultMeta = result.getMetaData();
            result.next();
            moment = new Pharmacie(ID, result.getObject("NOM").toString(), result.getObject("ADRESSE").toString(), result.getObject("TELEPHONE").toString(), result.getObject("LONGITUDE").toString(), result.getObject("LATITUDE").toString(), result.getObject("MAIL").toString());
            result.close();

        }
        catch(Exception e){
            System.out.println("\n Database Failure! (Class = Connect2.java Pharma Research)");
            e.printStackTrace();
        }
        System.out.println(moment.toString());
        return moment;

    }

    public static void decrementeMedicament(Panier medicament)
    {
        try(Statement state = ConnectionManager.getConnection().createStatement())
        {

            String test = "UPDATE PRODUIT SET QUANTITE = \"" + (medicament.getQUANTITEMAX() - medicament.getQUANTITE()) + "\" WHERE (NOM_COMMERCIAL LIKE  \"" + medicament.getNOM_MEDICAMENT() + "\") AND (FORME = \"" + medicament.getFORME() + "\") AND (DOSAGE LIKE \"" + medicament.getDOSAGE().substring(0, medicament.getDOSAGE().length() - 3) + "\");";
            System.out.println("Query " + test);
            state.executeUpdate(test);
            System.out.println( medicament.getNOM_MEDICAMENT() + " Decrementé en BDD, nouvelle quantité = " + (medicament.getQUANTITEMAX() - medicament.getQUANTITE()));
        }
        catch(Exception e){
            System.out.println("\n Database Failure! (Class = Connect2.java Pharma Decrement)");
            e.printStackTrace();
        }
    }*/




}