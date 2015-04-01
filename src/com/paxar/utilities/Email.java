
package com.paxar.utilities;

import java.io.File;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


/**
 * @author DickLobacz
 *
 * Email is used to send SMPT email to one or more addresses.
 */
public class Email implements java.io.Serializable{

	/**
	 * Properties 
	 */
	
	public String toAddresses;		// String array of email recipients.
	public String mailHost;			// IP or hostname of SMTP mail server.
	public String message;			// Email message.
	public String fromAddress;		// Who is sending the email.
	public String subject;			// Email subject.
	public String encoding;			// Email message encoding.
	public String attachment1;		// Path to a file attachment.
	public String attachment2;		// Path to a file attachment.
	public String attachment3;		// Path to a file attachment.
	public String attachmentName1;	// The attachment file name that will appear in the email.
	public String attachmentName2;	// The attachment file name that will appear in the email.
	public String attachmentName3;	// The attachment file name that will appear in the email.

	public Email() {
		super();
		mailHost = "mail.paxar.com";
		encoding = "text/html; charset=\"iso-8859-1\"";

	}

	public void sendMail(){
		
    	// Initialize defaults for the mail message.
    	
        Properties sysProps = System.getProperties();
        sysProps.put("mail.smtp.host", this.mailHost);
        Session mailSession = Session.getDefaultInstance(sysProps, null);
        MimeMessage mimeMessage = new MimeMessage(mailSession);

    	try{
    		
    		if (this.fromAddress != null){
    			mimeMessage.setFrom(new InternetAddress(this.fromAddress));
    		}else{
    			System.out.println("Email: Warning!  From address is empty.");
    		}
    		if (this.subject != null){
    			mimeMessage.setSubject(this.subject);
			}else{
				System.out.println("Email: Warning!  Subject is empty.");
			}
    		
    		if (this.toAddresses != null){
    			mimeMessage.setRecipients(Message.RecipientType.TO, this.toAddresses);
			}else{
				System.out.println("Email: Warning!  Subject is empty.");
			}
    		
    		mimeMessage.setSentDate(new java.util.Date());
    	
    	    // set message BODY
    	    MimeBodyPart mimebodypart = new MimeBodyPart();
    	    mimebodypart.setContent(this.message, this.encoding);

    	    // attach message BODY
    	    MimeMultipart mimemultipart = new MimeMultipart();
    	    mimemultipart.addBodyPart(mimebodypart);
    	    
    	    // Add attachments?
  
		    
	        try
		    {
	        	if (this.attachment1 != null){
				    // attach FILE
					mimebodypart = new MimeBodyPart();
	
			        	
			        FileDataSource filedatasource = new FileDataSource(this.attachment1);
			        mimebodypart.setDataHandler(new DataHandler(filedatasource));
	
				    mimebodypart.setFileName(this.attachmentName1); // set FILENAME
				    mimemultipart.addBodyPart(mimebodypart);
				    mimeMessage.setContent(mimemultipart); 

			    }
	        	if (this.attachment2 != null){
				    // attach FILE
					mimebodypart = new MimeBodyPart();
	
			        	
			        FileDataSource filedatasource = new FileDataSource(this.attachment2);
			        mimebodypart.setDataHandler(new DataHandler(filedatasource));
	
				    mimebodypart.setFileName(this.attachmentName2); // set FILENAME
				    mimemultipart.addBodyPart(mimebodypart);
				    mimeMessage.setContent(mimemultipart); 
				    


			    }
	        	if (this.attachment3 != null){
				    // attach FILE
					mimebodypart = new MimeBodyPart();
	
			        	
			        FileDataSource filedatasource = new FileDataSource(this.attachment3);
			        mimebodypart.setDataHandler(new DataHandler(filedatasource));
	
				    mimebodypart.setFileName(this.attachmentName3); // set FILENAME
				    mimemultipart.addBodyPart(mimebodypart);
				    mimeMessage.setContent(mimemultipart); 


			    }
		    }
		    catch(Exception exception3)
		    {
		        System.out.println("\tError in sending file not been able to attach ......\t" + this.attachment1 + ".  Error = " + exception3.getMessage());
		    }
        	mimeMessage.setContent(mimemultipart);
	        Transport.send( mimeMessage );	
	        
    	}catch (javax.mail.MessagingException e){
    		
			System.err.println("Email: Error sending email - " + e.getMessage()); 
    	}
    
    }

}
