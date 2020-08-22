package com.sockets;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Meave Dregonhive on 14/02/2018.
 */
public class Request
{
    private int IDRequest;
    private int IDPharmaSource;
    private String NomMedicament;
    private String Forme;
    private String Dosage;
    private ArrayList<Integer> PharmaciesOUI = new ArrayList<>(); //liste des pharmacies ayant repondu par oui
    private LocalDateTime horodate;
    private String PharmaciesOUIliste = "A";

    public Request(int IDPharmaSource, String nomMedicament, String dosage, String forme,  LocalDateTime horodate) {
        this.IDPharmaSource = IDPharmaSource;
        NomMedicament = nomMedicament;
        Forme = forme;
        Dosage = dosage;
        this.horodate = horodate;
    }

    public Request(int IDRequest) {
        this.IDRequest = IDRequest;
    }

    public int getIDRequest() {
        return IDRequest;
    }

    public String getPharmaciesOUIliste() {
        return PharmaciesOUIliste;
    }

    public void addPharmacieOUI(int ID)
    {
        this.PharmaciesOUI.add(ID);
        this.PharmaciesOUIliste += ";" + ID + ";";
        InformerClient();
    }

    public void InformerClient()
    {
        if(STATICS.UTILISATEURSCONNECTES.contains(new Pharmacie(this.IDPharmaSource)))
        {
            int index = STATICS.UTILISATEURSCONNECTES.indexOf(new Pharmacie(this.IDPharmaSource));
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(STATICS.UTILISATEURSCONNECTES.get(index).getSocket().getOutputStream()));
                writer.write(this.PharmaciesOUIliste);
                writer.newLine();
                writer.flush(); //vide le buffer
            }catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "Request{" +
                "IDRequest=" + IDRequest +
                ", IDPharmaSource=" + IDPharmaSource +
                ", NomMedicament='" + NomMedicament + '\'' +
                ", Forme='" + Forme + '\'' +
                ", Dosage='" + Dosage + '\'' +
                ", horodate=" + horodate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        return IDRequest == request.IDRequest;
    }

    @Override
    public int hashCode() {
        return IDRequest;
    }

    public void ajoutRequest()
    {
        Connection conn = ConnectionManager.getConnection();
        try
        {
            System.out.println("///Connection established.");
            String query = "INSERT INTO Requests VALUES (?,?,?,?,?,?)";
            int id = 0;
            PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, IDPharmaSource);
            statement.setString(2, NomMedicament.toUpperCase());
            statement.setString(3, Dosage.toUpperCase());
            statement.setString(4, Forme.toUpperCase());
            statement.setString(5, horodate.toString());
            statement.setInt(6, 0);
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();

            if (rs.next()) {
                this.IDRequest = rs.getInt(1);
            }
            System.out.println("ID request = " + id);
            statement.close();

        }
        catch (SQLException exc)
        {
            System.out.println(exc);
            exc.getStackTrace();
            System.out.println("Connection failed !");
        }
    }
}

