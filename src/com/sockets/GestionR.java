package com.sockets;

import java.io.*;

import java.net.Socket;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;


public class GestionR implements Runnable //pour les notions de Thread, de l'interface Runnable et de la methode run, veuillez vous referez
// à la partie "Exécuter des tâches simultanément" du cours Java d'OpenClassroom (celui communiqué)
{
    public static int cpt;
    private final Socket socket;
    private int ID;
    private Pharmacie pharmacie;
    public final static File BDD = new File ("/Users/macsamir/Desktop/BDD.txt");

/*    class Control {
        public CountDownLatch latch;
    }
    final Control control = new Control();*/

    public GestionR(Socket client) //constructeur
    {
        this.socket = client;
        STATICS.NbUsersValue += 1;
        STATICS.UpdateDisplayData();


    }

    @Override //annotation de Redéfinition (run existe dans l'interface mère)
    public void run() {
        try     //Exception avec ressources
                (
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                )
        {


            String Input;
            while ((Input = in.readLine()) != null) // Lecture ligne par ligne (idem à Scanner.nextLine();)
            {
                if (".".equals(Input))
                {
                    System.out.println("termination");
                    writer.write(".");
                    writer.newLine();
                    writer.flush(); //vide le buffer
                    break;

                }

                if (Input.charAt(0) == 'I')
                {

                    int id = Integer.parseInt(Input.substring(2));
                    Pharmacie moment = Connect.recherchePharmacieID(id);
                    this.pharmacie = moment;
                    if(moment != null)
                    {
                        this.ID = moment.getID();
                        moment.setSocket(this.socket);
                        STATICS.UTILISATEURSCONNECTES.add(moment);
                        writer.write("Bienvenue sur le serveur, " + moment.getNomPharma() + ".");
                        writer.newLine();
                        writer.flush(); //vide le buffer
                        STATICS.logValue += (LocalDateTime.now().format(STATICS.DATETIMEFORMAT) + " --> " + moment.getNomPharma() + " s'est connecté\n");
                        STATICS.UpdateDisplayData();

                        //========================== sends news
                        int index = this.pharmacie.hasNews();
                        if (index >= 0)
                        {
                            System.out.print("has news: " + index + " thus --> ");
                            index = STATICS.REQUESTS.indexOf(new Request(index));
                            writer.write(STATICS.REQUESTS.get(index).getPharmaciesOUIliste());
                            writer.newLine();
                            writer.flush(); //vide le buffer
                        }
                    }
                    else {
                        writer.write("Vous n'êtes pas identifié sur le serveur  ");
                        writer.newLine();
                        writer.flush(); //vide le buffer
                    }

                }

                if (Input.charAt(0) == 'E') //recup de coords
                {
                    User user = new User(Input.substring(2));
                    user.RecupCoords();
                }

                if (Input.charAt(0) == 'G') // Nouveau compte
                {
                    Pattern p = Pattern.compile(";[\\s\\S]*?;");
                    Matcher m = p.matcher(Input.substring(1));

                    m.find();//Username
                    String username = m.group().substring(1,m.group().length()-1);
                    m.find();//Password
                    String password = m.group().substring(1,m.group().length()-1);
                    m.find();//email
                    String email = m.group().substring(1,m.group().length()-1);
                    m.find();//tel
                    String tel = m.group().substring(1,m.group().length()-1);

                    User user = new User(username, password, email, tel);
                    user.ajoutUser();
                    user.Welcome();
                    writer.write("Utilisateur ajouté au réseau");
                    writer.newLine();
                    writer.flush();

                }

                if (Input.charAt(0) == 'H') //supprimer compte
                {
                    Pattern p = Pattern.compile(";[\\s\\S]*?;");
                    Matcher m = p.matcher(Input.substring(1));

                    m.find();//Email
                    String mail = m.group().substring(1,m.group().length()-1).toUpperCase();


                    User user = new User(mail);
                    user.supprimerUser();
                    sleep(2000);
                    writer.write("Utilisateur supprimé du réseau");
                    writer.newLine();
                    writer.flush();

                }

                if (Input.charAt(0) == 'M') // Ajout d'un medicament pour la restitution
                {
                    ProduitRestitution product = new ProduitRestitution();
                    Pattern p = Pattern.compile(";[\\s\\S]*?;");
                    Matcher m = p.matcher(Input.substring(1));

                    m.find();//Nom
                    product.setNom_commercial(m.group().substring(1,m.group().length()-1).toUpperCase());
                    m.find();//forme
                    product.setForme(m.group().substring(1,m.group().length()-1));
                    m.find();//dosage
                    product.setDosage(m.group().substring(1,m.group().length()-1).toUpperCase());
                    m.find();//quantite
                    product.setQuantite(Integer.parseInt(m.group().substring(1,m.group().length()-1)));
                    m.find();//peremption
                    product.setDatePeremption(LocalDate.parse(m.group().substring(1, m.group().length() - 1)));
                    m.find();//remarques
                    product.setRemarque(m.group().substring(1,m.group().length()-1));

                    product.setIdPharmacie(this.pharmacie.getID());
                    product.ajoutProduit();

                    writer.write("Produit ajouté, Merci pour votre collaboration.");
                    writer.newLine();
                    writer.flush();

                    STATICS.logValue += (LocalDateTime.now().format(STATICS.DATETIMEFORMAT) + " --> " + this.pharmacie.getNomPharma() + " a mis en restitution: " + product.getQuantite() + " " + product.getNom_commercial() + "\n");
                    STATICS.UpdateDisplayData();

                    for(Pharmacie pharma: STATICS.UTILISATEURSCONNECTES)  // Send MAJ to all connected users
                    {
                        BufferedWriter writerMoment = new BufferedWriter(new OutputStreamWriter(pharma.getSocket().getOutputStream()));
                        writerMoment.write("U BDD Restitution alterée");
                        writerMoment.newLine();
                        writerMoment.flush(); //vide le buffer
                        sleep(5000);
                        UpdateBDD.updaterRestitue(pharma.getSocket());
                    }


                }

                if (Input.charAt(1) == 'R')
                {
                    Pattern p = Pattern.compile(";[\\s\\S]*?;");
                    Matcher m = p.matcher(Input);
                    //while(m.find()) {
                    m.find(); //R
                    m.find(); //ID Restitution
                    int ID = Integer.parseInt(m.group().substring(1,m.group().length()-1).toUpperCase());
                    m.find();//quantité
                    int Quantité = Integer.parseInt(m.group().substring(1,m.group().length()-1).toUpperCase());
                    STATICS.logValue += (LocalDateTime.now().format(STATICS.DATETIMEFORMAT) + " --> " + "Requete de reservation du medicament d'ID: " + ID + " (quantité = " + Quantité + ") de la part de " + this.pharmacie.getNomPharma() + "\n" );
                    STATICS.UpdateDisplayData();
                    ProduitRestitution moment = Connect.rechercheRestitutionID(ID);
                    if(moment != null )
                    {
                        int momentQuantité = moment.getQuantite() - Quantité;

                        if (momentQuantité == 0) {
                            Connect.DeleteRestitutionID(moment.getID());
                            System.out.println("Epuisé");

                        }
                        else
                        {
                            moment.setQuantite(Quantité);
                            Connect.UpdateRestitutionID(ID, momentQuantité);
                            System.out.println("Decrementé");
                        }
                        if(moment.getIdPharmacie() != this.pharmacie.getID()) { //on ne demande pas de confirmation si la pharmacie source lance une reservation
                            int id;
                            id = Connect.AddAttente(moment);
                            String textForSource = "N;" + id + ";" + moment.getID();
                            Connect.AddNews(moment.getIdPharmacie(), textForSource);

                            Pharmacie momentPharma = new Pharmacie(moment.getIdPharmacie());
                            if (STATICS.UTILISATEURSCONNECTES.contains(momentPharma)) {
                                momentPharma = STATICS.UTILISATEURSCONNECTES.get(STATICS.UTILISATEURSCONNECTES.indexOf(momentPharma));
                                BufferedWriter writerMoment = new BufferedWriter(new OutputStreamWriter(momentPharma.getSocket().getOutputStream()));
                                writerMoment.write(textForSource);
                                writerMoment.newLine();
                                writerMoment.flush(); //vide le buffer
                                sleep(4000);
                            }
                        }
                        Connect.MajBDDMedocs();
                        for(Pharmacie pharma: STATICS.UTILISATEURSCONNECTES)  // Send MAJ to all connected users
                        {
                            BufferedWriter writerMoment = new BufferedWriter(new OutputStreamWriter(pharma.getSocket().getOutputStream()));
                            writerMoment.write("U BDD Restitution alterée");
                            writerMoment.newLine();
                            writerMoment.flush(); //vide le buffer
                            sleep(4000);
                            UpdateBDD.updaterRestitue(pharma.getSocket());
                        }



                    }


                }

                else if (Input.charAt(0) == 'D') // Demande d'un produit
                {
                    STATICS.logValue += (LocalDateTime.now().format(STATICS.DATETIMEFORMAT) + " --> " + pharmacie.getNomPharma() + " demande le medicament: " + Input.substring(2) + "\n");
                    STATICS.UpdateDisplayData();
                    System.out.println("input = " + Input);
                    Pattern p = Pattern.compile(";[\\s\\S]*?;");
                    Matcher m = p.matcher(Input.substring(0));
                    m.find();//Nom
                    String NomComm = (m.group().substring(1,m.group().length()-1).toUpperCase());
                    m.find();
                    String Dosage = (m.group().substring(1,m.group().length()-1).toUpperCase());
                    m.find();
                    String Forme = (m.group().substring(1,m.group().length()-1).toUpperCase());

                    Request request = new Request(this.pharmacie.getID(), NomComm, Dosage, Forme, LocalDateTime.now());
                    System.out.println(request.toString());
                    request.ajoutRequest();
                    STATICS.REQUESTS.add(request);
                    String TextToSend = "C ;" + request.getIDRequest() + ";;" + this.pharmacie.getID() + ";" + Input.substring(2) + ";";
                    if(false )//STATICS.UTILISATEURSCONNECTES.size() < 2)
                    {
                        sleep(2000);
                        writer.write("Pas d'autres clients en ligne, désolé.");
                        writer.newLine();
                        writer.flush(); //vide le buffer
                    }
                    //   toAll diffusion = new toAll(CLIENTS, client, cpt, Input);
                    //   diffusion.run();
                    //  diffusion.join();
                    else {
                        toAll diffusion = new toAll(this.socket, TextToSend);
                        diffusion.start();
                        writer.write("T;" + request.getIDRequest() + ";");
                        writer.newLine();
                        writer.flush(); //vide le buffer
                    }
                }

                else if (Input.charAt(0) == 'F') // Reponse à une requete
                {
                    Pattern p = Pattern.compile(";[\\s\\S]*?;");
                    Matcher m = p.matcher(Input.substring(0));
                    m.find();//ID request
                    int IDreq = Integer.parseInt(m.group().substring(1,m.group().length()-1).toUpperCase());
                    m.find();//OUI ou NON
                    String reponse = (m.group().substring(1,m.group().length()-1).toUpperCase());
                    m.find();

                    STATICS.logValue += (LocalDateTime.now().format(STATICS.DATETIMEFORMAT) + " --> " + pharmacie.getNomPharma() + " répond (" + reponse + ") à la requête: ID = " + IDreq);
                    STATICS.UpdateDisplayData();

                    if(reponse.equals("OUI"))
                    {
                        System.out.println(STATICS.REQUESTS.indexOf(new Request(IDreq)));
                        STATICS.REQUESTS.get(STATICS.REQUESTS.indexOf(new Request(IDreq))).addPharmacieOUI(this.pharmacie.getID());
                        sleep(2000);
                        writer.write("Merci pour votre Soutien, votre réponse sera transmise.");
                        writer.newLine();
                        writer.flush();

                    }
                    else {
                        writer.write("Votre réponse sera transmise.");
                        writer.newLine();
                        writer.flush(); //vide le buffer
                    }
                }

                else
                {
                    System.out.println(Input);
                }
            }

        } catch (IOException e)
        {
            System.out.println("==================================================================");
            System.out.println(System.currentTimeMillis() + " ERREUR! d'In/Out: GestionR " + e);
            System.out.println("Client = " + this.socket.toString());
            e.printStackTrace();
            System.out.println("==================================================================");
        }
        catch (Exception ex)
        {
            System.out.println(System.currentTimeMillis() + " ERREUR! (Globale, GestionR " + ex);
            ex.printStackTrace();
        }
        finally {
            STATICS.NbUsersValue -= 1;
            STATICS.logValue += (LocalDateTime.now().format(STATICS.DATETIMEFORMAT) + " --> " + "Pharmacie " + this.pharmacie.getNomPharma() + " s'est deconnecté \n");
            STATICS.UpdateDisplayData();
            try {
                this.socket.close();
                STATICS.UTILISATEURSCONNECTES.remove(this);
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }


        }
    }

}


