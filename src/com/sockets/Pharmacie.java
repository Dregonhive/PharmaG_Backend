package com.sockets;

import java.net.Socket;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Meave Dregonhive on 20/04/2018.
 */
public class Pharmacie implements Comparable{
    private int ID;
    private String nomPharma;
    private String Adresse;
    private String EmailPrincip;
    private String numTelephone;
    private float LONGITUDE;
    private float LATITUDE;
    private int IN;
    private int OUT;
    private Socket socket;

    public Pharmacie(int ID, String nomPharma, String adresse, String emailPrincip, String numTelephone, float LONGITUDE, float LATITUDE, int IN, int OUT) {
        this.ID = ID;
        this.nomPharma = nomPharma;
        Adresse = adresse;
        EmailPrincip = emailPrincip;
        this.numTelephone = numTelephone;
        this.LONGITUDE = LONGITUDE;
        this.LATITUDE = LATITUDE;
        this.IN = IN;
        this.OUT = OUT;
    }

    public Pharmacie(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getNomPharma() {
        return nomPharma;
    }

    public String getAdresse() {
        return Adresse;
    }

    public String getNumTelephone() {
        return numTelephone;
    }

    public float getLONGITUDE() {
        return LONGITUDE;
    }

    public float getLATITUDE() {
        return LATITUDE;
    }

    public String getEmailPrincip() {
        return EmailPrincip;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    public int hasNews() //returns request ID
    {
        int moment = -1;
        try(Statement state = ConnectionManager.getConnection().createStatement())
        {

            String requete = "SELECT * FROM Requests WHERE Pharmasource LIKE \"" + this.ID + "\"";
            ResultSet result = state.executeQuery(requete);
            ResultSetMetaData resultMeta = result.getMetaData();
            if(result.next()) {
                moment = Integer.parseInt(result.getObject("ID").toString());
            }
            result.close();

        }
        catch(Exception e){
            System.out.println("\n Database Failure! (Class = recherche Restitution Connect.java)");
            e.printStackTrace();
        }
        return moment;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pharmacie pharmacie = (Pharmacie) o;

        return ID == pharmacie.ID;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public int compareTo(Object o) {
        Pharmacie moment = (Pharmacie) o;
        if (this.ID == moment.ID) return 0;
        else if (this.ID > moment.ID) return 1;
        return -1;
    }

    @Override
    public String toString() {
        return "Pharmacie{" +
                "ID=" + ID +
                ", nomPharma='" + nomPharma + '\'' +
                ", Adresse='" + Adresse + '\'' +
                ", EmailPrincip='" + EmailPrincip + '\'' +
                ", numTelephone='" + numTelephone + '\'' +
                ", LONGITUDE=" + LONGITUDE +
                ", LATITUDE=" + LATITUDE +
                ", IN=" + IN +
                ", OUT=" + OUT +
                ", socket=" + socket +
                '}';
    }
}
