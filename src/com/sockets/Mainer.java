package com.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

/**
 * Created by Meave Dregonhive on 27/04/2018.
 */
public class Mainer {

    public static void launcher()
    {
        System.out.println("test");
        ExecutorService exec = null;
        int port = 7575;
        try ( //Exception avec ressources pour eviter de fermer manuelement les sockets (comme fclose).
              // donc si le catch est declenché --> fermeture automatique de tous ce qui est entre parentheses (avant l'accolade { )
              ServerSocket SERVER = new ServerSocket(port); // Socket du serveur : unique.
        )

        {

          //  SMSContacter.launcher();
            exec = Executors.newFixedThreadPool(50);
            System.out.println("Serveur ouvert, en attente de clients... ");
            while(true) //Reboucle infiniment (car serveur...) pour gérer les clients.
            {
                Socket CLIENT = SERVER.accept();
                System.out.println("Connexion Client sur le port " + port + " --> Client = " + CLIENT.getPort() );

                //------ Envoi de la base de données
                UpdateBDD.updaterRestitue(CLIENT);
                try {
                    sleep(4000);


                } catch (Exception ex) {
                    System.out.println("TimeOut Exception " + ex.getMessage());
                    ex.printStackTrace();
                }
                Runnable Gestion = new GestionR(CLIENT);
                exec.execute(Gestion);
            }

        } catch (IOException e)
        {
            System.out.println("ERREUR de connexion au port " + port + " OU d'ecoute de clients. ");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally //Si catch declanché, la ligne doit fermée (manuelement puisque le "try" ne la gére pas)
        {
            if (exec != null)
                exec.shutdown();
        }
    }
}
