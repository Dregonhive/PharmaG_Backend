package com.sockets;

import java.io.*;
import java.net.Socket;

/**
 * Created by Meave Dregonhive on 14/02/2018.
 */
public class toAll extends Thread
{
  //  private int TIMELIMIT = 10000;
    private Socket CLIENTS[]; //Liste des Clients en ligne
    private Socket client; //Client source de la requéte
    private Request REQUESTS[]; //Liste des Requetes envoyées
    private int cpt;
    private String msg;

    public toAll(Socket client, String msg) //
    {
        for(Pharmacie pharmacie: STATICS.UTILISATEURSCONNECTES)
        {

        }
        this.client = client;
        this.msg = msg;
        //this.REQUESTS = new Request[cpt-1];
    }

    public void run()
    {
            try
            {
                for(Pharmacie pharmacie: STATICS.UTILISATEURSCONNECTES)
                {
                    System.out.println("testtest2");
                    if(true )//pharmacie.getSocket() != this.client)
                    {
                        System.out.println("testtest");
                        System.out.println(msg);
                        BufferedWriter writerAll = new BufferedWriter(new OutputStreamWriter(pharmacie.getSocket().getOutputStream()));
                        writerAll.write(msg);
                        writerAll.newLine();
                        writerAll.flush();
                    }
                }
            }
            catch (IOException e)
            {
                System.out.println(System.currentTimeMillis() + " ERREUR! d'In/Out: toAll " + e);
                e.getStackTrace();
            }

            catch (Exception ex)
            {
                System.out.println("ERREUR! (Globale, toAll " + ex);
                System.out.println("yup");
                ex.getStackTrace();
            }
        }



    }


