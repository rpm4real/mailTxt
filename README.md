# mailTxt
A small Java application which sends SMS previews of specific email messages.


## Details
This app uses twilio to send an SMS message of a preview (sender, subject) of new email messages from senders specified in the properties file. Currently, it is only setup for imap.gmail.com emails (to simplify properties file). 

To use, once the dependencies are in place, compile right into the `src` directory with `javac -cp "./lib/*" src/*.java`

Once compiled, navigate to `src` and run `java -cp "../lib/*:./" mailTxt` 


## Dependencies 
JavaMail API: https://java.net/projects/javamail/pages/Home

Twilio-java: https://github.com/twilio/twilio-java (along with Twilio account, SMS enabled)

The .jar for these libraries should be placed in `mailTxt/lib/`.


## Configuration
The `properties/appPropertiesTemplate.properties` needs to be filled out with the appropriate configuration information. 

