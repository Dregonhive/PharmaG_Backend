package com.sockets;

/**
 * Created by Meave Dregonhive on 04/03/2018.
 */


import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer extends Thread
{
    private String TargetEmail;
    private String text;

    public Mailer(String targetEmail, String text) {
        TargetEmail = targetEmail;
        this.text = text;
    }

    public void run()
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("pharmag.esi@gmail.com","PharmaRefractor2018");
                    }
                });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress("pharmag.esi@gmail.com"));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TargetEmail));


            message.setSubject("PharmaG: Récuperation de coordonnées de connexion ");

            message.setText(text);

            Transport.send(message);

            System.out.println("Mail Sent");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
