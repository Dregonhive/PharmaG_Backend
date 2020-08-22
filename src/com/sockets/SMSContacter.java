package com.sockets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Meave Dregonhive on 27/04/2018.
 */
public class SMSContacter {

    public static Socket socket;
    public static void launcher() {
        System.out.println("test");
        ExecutorService exec = null;
        int port = 6600; //For the sms
        try (
                ServerSocket SERVER = new ServerSocket(port); // Socket du serveur : unique.

        )

        {
            String Input;
            System.out.println("Serveur ouvert, en attente du SMSer... ");
            Socket CLIENT = SERVER.accept();
            socket = CLIENT;
            System.out.println("Connexion SMSer sur le port " + port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            //  BufferedReader in = new BufferedReader(new InputStreamReader(CLIENT.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(CLIENT.getOutputStream()));
            writer.write("send drugs");
            writer.newLine();
            writer.flush();
            while ((Input = in.readLine()) != null) // Lecture ligne par ligne (idem à Scanner.nextLine();)
            {}

        } catch (IOException e) {
            System.out.println("ERREUR de connexion au port " + port + " OU d'ecoute de clients. ");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally //Si catch declanché, la ligne doit fermée (manuelement puisque le "try" ne la gére pas)
        {
            //shutdown
        }
    }

    public static void SendSMSRequest(String Tel, String Message)
    {
        try
        {
            System.out.println("test");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(";" + Tel + ";;" + Message + ";");
            writer.newLine();
            writer.flush();
        }catch(Exception ex)
        {
            ex.getStackTrace();
        }
    }
}
