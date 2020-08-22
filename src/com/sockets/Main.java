
package com.sockets;
//--------------------------------------------------------SERVEUR
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;


public class Main extends Application {

    public static Stage Window;

    @Override
    public void start(Stage primaryStage) throws Exception, IOException{


        Connect.MajBDDMedocs();
        Connect.MajBDDPharmacies();
        Connect.Restore();


        Window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Server.fxml")); //Interface Acceuil Loaded

        //---------------------------------------------------------------------------------------------Initialisation des elements statiques
        Scene scene = new Scene(root, 900, 500);
        Window.setTitle("PharmaG");
        Window.setScene(scene);
        Window.show();

        System.out.println("done");

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            public Boolean call() {
                try {
                    System.out.println("launched");
                    Mainer.launcher();
                    System.out.println("terminated");
                } catch (Exception e) {
                    e.printStackTrace();

                }
                return true;
            }};

        new Thread(task).start();




    }
    public static void main(String[] args) {
        launch(args);
    }

}
