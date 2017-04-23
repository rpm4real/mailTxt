

public class MailPreview {

	String fromAddress; 
	String fromSubject;

	public MailPreview(String address, String subject) {
		this.fromAddress = address; 
		this.fromSubject = subject;
	}

	public String getFromAddress() {
		return fromAddress; 
	}

	public String getSubject() {
		return fromSubject;
	}


}