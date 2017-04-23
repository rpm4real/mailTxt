import java.util.ArrayList;
import java.util.Arrays;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class mailTxt {

	public static String accountSid;
  	public static String authToken;

  	public static String targetNumber;
  	public static String twilioNumber;

  	public static String emailAccount; 
  	public static String emailPassword; 

  	public static String[] emailInputList; 

  	public static void loadProperties() {
  		Properties prop = new Properties();
  		InputStream input = null; 

  		try {
  			input = new FileInputStream("properties/appProperties.properties");

  			prop.load(input);
  			//twilio config
  			accountSid = prop.getProperty("twiliosid");
  			authToken = prop.getProperty("twiliotoken");
  			targetNumber = prop.getProperty("targetNumber");
  			twilioNumber = prop.getProperty("twilioNumber");

  			//email config 
  			emailAccount = prop.getProperty("emailaccount");
  			emailPassword = prop.getProperty("emailpassword");

  			//
  			emailInputList = prop.getProperty("emails").split(",");

  		} catch (IOException ex) {
  			ex.printStackTrace();
  		} finally {
  			if (input != null) {
  				try {
  					input.close();
  				} catch (IOException e) {
  					e.printStackTrace();
  				}
  			}
  		}

  	}

	public static void smsTwilio(MailPreview myPreview) {
		//do twilio action
		System.out.println("Sender: "+ myPreview.getFromAddress());
		System.out.println("Subject: "+ myPreview.getSubject());

		Twilio.init(accountSid,authToken);

		String messageContent = "New email from: "+ myPreview.getFromAddress()
			+ "\n Subject: " + myPreview.getSubject();

    	Message message = Message.creator(new PhoneNumber(targetNumber), new PhoneNumber(twilioNumber), 
        	messageContent).create();

    	System.out.println(message.getSid());
	}

	public static void main(String[] args){

		String emailAddress;

		loadProperties();

		MailClient bean=new MailClient();

		System.out.println("Getting new messages...");
		ArrayList<MailPreview> previews = bean.getNewPreviews("imap", "imap.gmail.com", "993", emailAccount, emailPassword);
		for(MailPreview preview : previews) {
			//parse for email
			emailAddress = preview.getFromAddress();
			emailAddress = emailAddress.substring(emailAddress.indexOf("<")+1);
			emailAddress = emailAddress.substring(0,emailAddress.indexOf(">"));
			if (Arrays.asList(emailInputList).contains(emailAddress)) {
				smsTwilio(preview); 
			}
		}
	}

}
