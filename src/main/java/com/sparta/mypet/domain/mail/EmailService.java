package com.sparta.mypet.domain.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender javaMailSender;

	private final TemplateEngine templateEngine;

	public void sendEmail(String to, String verificationCode) {

		Context context = new Context();
		context.setVariable("verificationCode", verificationCode);

		String process = templateEngine.process("verification-email", context);

		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setSubject("이메일 인증");
			helper.setText(process, true);
			helper.setTo(to);

			javaMailSender.send(message);
		} catch (MessagingException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

}
