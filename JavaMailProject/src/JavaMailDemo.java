import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class JavaMailDemo {
	public static void main(String[] args) throws Exception {
		Properties properties=new Properties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host","smtp.qq.com");
		properties.setProperty("mail.smtp.port","465");
		properties.setProperty("mail.smtp.auth", "true");
		
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		properties.setProperty("mail.smtp.socketFactory.port", "465");
		Session session = Session.getInstance(properties);
		MimeMessage message = createMimeMessage(session,"594943806@qq.com","cheryl_hoo@163"
				+ ".com","11111@qq.com","28066933@qq.com");
		Transport transport = session.getTransport();
		transport.connect("594943806@qq.com","jbcuviukoalmbdec");
		transport.sendMessage(message,message.getAllRecipients());
		transport.close();
	}
	
	public static MimeMessage createMimeMessage(Session session,String send,String receive, String cReceive,String mReceive) throws Exception {
		MimeMessage message = new MimeMessage(session);
		Address address = new InternetAddress(send,"发件人的名字","UTF-8");
//		message.addFrom(address);
		message.setFrom(address);
		message.setSubject("这是标题，还有图片+附件","UTF-8");
//		message.setContent("正文内容 ***你好!***","text/html;charset=utf-8");

		MimeBodyPart imagePart = new MimeBodyPart();
		DataHandler imageDataHandler = new DataHandler(new FileDataSource("src/book.png"));
		imagePart.setDataHandler(imageDataHandler);
		imagePart.setContentID("myBookIco");
		
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setContent("正文内容 ***你好!***image:<img src='cid:myBookIco' />","text/html;charset=UTF-8");
		
		MimeMultipart mm_text_image = new MimeMultipart();
		mm_text_image.addBodyPart(imagePart);
		mm_text_image.addBodyPart(textPart);
		mm_text_image.setSubType("related");
		
		MimeBodyPart text_image_bodyPart = new MimeBodyPart();
		text_image_bodyPart.setContent(mm_text_image);
		
		MimeBodyPart attachment = new MimeBodyPart();
		DataHandler fileDataHandler = new DataHandler(new FileDataSource("src/book.png"));
		attachment.setDataHandler(fileDataHandler);
		attachment.setFileName(MimeUtility.encodeText(fileDataHandler.getName()));
		
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(text_image_bodyPart);
		mm.addBodyPart(attachment);
		mm.setSubType("mixed");
		
		message.setContent(mm ,"text/html;charset=UTF-8");
		
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receive,"收件人A","UTF-8"));
		message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(cReceive,"抄送人C","UTF-8") );
		message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(mReceive,"密送人M","UTF-8") );
		message.setSentDate(new Date());
		message.saveChanges();
		return message;
	}
}
