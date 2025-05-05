package com.example.service;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.dto.MessageDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.context.Context;
public interface EmailService {
    void sendEmail(MessageDTO messageDTO);
}

@Service
class EmailServiceImpl implements EmailService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private org.thymeleaf.spring6.SpringTemplateEngine templateEngine;

    @Override
    public void sendEmail(MessageDTO messageDTO) {
	try {
	    logger.info("START... Sending email");

	    MimeMessage message = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
	    
	    //load template email with content
	    Context context = new Context();
	    context.setVariable("name", messageDTO.getToName());
	    context.setVariable("content", messageDTO.getContent());
	    String html = templateEngine.process("welcome-email", context);
	    
	    ///send email
	    helper.setTo(messageDTO.getTo());
	    helper.setText(html, true);
	    helper.setSubject(messageDTO.getSubject());
	    helper.setFrom(messageDTO.getFrom());
	    javaMailSender.send(message);

	    logger.info("END... Email sent success");
	} catch (MessagingException e) {
	    logger.error("Email sent with error: " + e.getMessage());
	}
    }
}
