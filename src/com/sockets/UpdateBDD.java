package com.sockets;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * Created by Meave Dregonhive on 24/04/2018.
 */
public class UpdateBDD
{
    public static void updaterRestitue(Socket CLIENT)
    {
        try {
            byte[] chaine = new byte[(int) STATICS.BDD.length()];
            BufferedInputStream bIN = new BufferedInputStream(new FileInputStream(STATICS.BDD));
            bIN.read(chaine, 0, chaine.length);
            OutputStream Out = CLIENT.getOutputStream();
            Out.write(chaine, 0, chaine.length);
            Out.flush();
            System.out.println("Une BDD envoyée");
            sleep(4000);
        }catch (Exception ex){
            System.out.println("updaterRestitue ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
