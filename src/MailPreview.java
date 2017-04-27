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

	@Override
	public boolean equals(Object o) {
		if (o instanceof MailPreview) {
			MailPreview newO = (MailPreview) o;
			if ( newO.getFromAddress().equals(this.fromAddress) && newO.getSubject().equals(this.fromSubject))
				return true;
			else 
				return false;
		}
		else return false; 
	}


}