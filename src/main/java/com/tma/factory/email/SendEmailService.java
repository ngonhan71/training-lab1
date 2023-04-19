package com.tma.factory.email;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {
	
	@Value(value = "${spring.mail.username}")
	private String sender;

	private String verifyAccountRegisterTemplatePath = "/templates/email/EmailVerifyAccountRegister.html";

	private String verifyAccountCreateTemplatePath = "/templates/email/EmailVerifyAccountCreate.html";

	private String forgotPasswordTemplatePath = "/templates/email/EmailForgotPassword.html";
	
	@Autowired
	JavaMailSender javaMailSender;
	
	private String proceedData(String templatePath, Map<String, String> properties) throws IOException {
		
		Resource resource = new ClassPathResource(templatePath);
		File file = resource.getFile();
		
		String html = Files.readString(file.toPath());
		
		for(Entry<String, String> entry : properties.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			html = html.replace("${" + key + "}", value);
		}
		
		return html;
	}

	public void sendVerifyAccountRegister(Map<String, String> properties, String toAddress) throws MessagingException, IOException {
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		
		helper.setFrom(sender, "BookStore");
		helper.setTo(toAddress);
		helper.setText(proceedData(verifyAccountRegisterTemplatePath, properties), true);
		helper.setSubject("Xác minh tài khoản");
		
		javaMailSender.send(message);
		
	}

	public void sendVerifyAccountCreate(Map<String, String> properties, String toAddress) throws MessagingException, IOException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

		helper.setFrom(sender, "BookStore");
		helper.setTo(toAddress);
		helper.setText(proceedData(verifyAccountCreateTemplatePath, properties), true);
		helper.setSubject("Xác minh tài khoản");

		javaMailSender.send(message);

	}

	public void sendForgotPasswordEmail(Map<String, String> properties, String toAddress) throws MessagingException, IOException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

		helper.setFrom(sender, "BookStore");
		helper.setTo(toAddress);
		helper.setText(proceedData(forgotPasswordTemplatePath, properties), true);
		helper.setSubject("Đặt lại mật khẩu");

		javaMailSender.send(message);

	}

}