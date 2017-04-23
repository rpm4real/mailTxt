//package org.mano.imapexample;

import java.util.Properties;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress; 
import javax.mail.Store;

public class MailClient {

   private Properties getServerProperties(String protocol, String host, String port) {
      Properties properties = new Properties();
      properties.put(String.format("mail.%s.host", protocol), host);
      properties.put(String.format("mail.%s.port",protocol), port);
      properties.setProperty(String.format("mail.%s.socketFactory.class",protocol), "javax.net.ssl.SSLSocketFactory");
      properties.setProperty(String.format("mail.%s.socketFactory.fallback",protocol), "false");
      properties.setProperty(String.format("mail.%s.socketFactory.port",protocol), String.valueOf(port));

      return properties;
   }

   public ArrayList<MailPreview> getNewPreviews(String protocol, String host, String port, String userName, String password){
      Properties properties = getServerProperties(protocol, host, port);
      Session session = Session.getDefaultInstance(properties);

      ArrayList<MailPreview> mails = new ArrayList<MailPreview>();
      try {
         Store store = session.getStore(protocol);
         store.connect(userName, password);

         Folder inbox = store.getFolder("INBOX");
         inbox.open(Folder.READ_WRITE);

         int count = inbox.getMessageCount();
         //only look at the most recent 20 messages
         Message[] messages = inbox.getMessages(count-20, count);
         
         for (Message message : messages) {
            if (!message.getFlags().contains(Flags.Flag.SEEN)) {
            //if (true) { //for debugging
               Address[] fromAddresses = message.getFrom();
               MailPreview mail = new MailPreview(fromAddresses[0].toString(),message.getSubject()); 
               mails.add(mail);
            }
         }
         inbox.close(false);
         store.close();
      } catch (NoSuchProviderException ex) {
         System.out.println("No provider for protocol: "+ protocol);
         ex.printStackTrace();
      } catch (MessagingException ex) {
         System.out.println("Could not connect to the message store");
         ex.printStackTrace();
      }

      return mails;
   }

   public void getNewEmails(String protocol, String host, String port, String userName, String password){
      Properties properties = getServerProperties(protocol, host, port);
      Session session = Session.getDefaultInstance(properties);
      try {
         Store store = session.getStore(protocol);
         store.connect(userName, password);

         Folder inbox = store.getFolder("INBOX");
         inbox.open(Folder.READ_WRITE);

         int count = inbox.getMessageCount();
         System.out.println("# of messages: "+ count);
         //only look at the most recent 10 messages
         Message[] messages = inbox.getMessages(count-10, count);
         for (Message message : messages) {
            //if (!message.getFlags().contains(Flags.Flag.SEEN)) {
            if (true) {
               Address[] fromAddresses = message.getFrom();
               System.out.println("...................");
               System.out.println("\t From: " + fromAddresses[0].toString());
               System.out.println("\t To: "   + parseAddresses(message.getRecipients(RecipientType.TO)));
               System.out.println("\t CC: "+ parseAddresses(message.getRecipients(RecipientType.CC)));
               System.out.println("\t Subject: "+ message.getSubject());
               System.out.println("\t Sent Date:"+ message.getSentDate().toString());
               try {
                  String content = message.getContent().toString();
                  if (content.length() > 100) {
                     System.out.println("Preview of content:");
                     System.out.println(content.substring(0,99));
                  }
                  else {
                     System.out.println(content);
                  }
               } catch (Exception ex) {
                  System.out.println("Error reading content!!");
                  ex.printStackTrace();
               }
            }

         }
         inbox.close(false);
         store.close();
      } catch (NoSuchProviderException ex) {
         System.out.println("No provider for protocol: "+ protocol);
         ex.printStackTrace();
      } catch (MessagingException ex) {
         System.out.println("Could not connect to the message store");
         ex.printStackTrace();
      }
   }

   public void getFolders(String protocol, String host, String port, String userName, String password){
      Properties properties = getServerProperties(protocol, host, port);
      Session session = Session.getDefaultInstance(properties);
      try {
         Store store = session.getStore(protocol);
         store.connect(userName, password);

         System.out.println(store);

         Folder[] f = store.getDefaultFolder().list();
         for(Folder fd:f)
            System.out.println(">> "+ fd.getName());
      } catch (NoSuchProviderException ex) {
         System.out.println("No provider for protocol: "+ protocol);
         ex.printStackTrace();
      } catch (MessagingException ex) {
         System.out.println("Could not connect to the message store");
         ex.printStackTrace();
      }
   }

   private String parseAddresses(Address[] address) {

      String listOfAddress = "";
      if ((address == null) || (address.length < 1))
         return null;
      if (!(address[0] instanceof InternetAddress))
         return null;

      for (int i = 0; i < address.length; i++) {
         InternetAddress internetAddress = (InternetAddress) address[0];
         listOfAddress += internetAddress.getAddress()+",";
      }
      return listOfAddress;
   }
}


