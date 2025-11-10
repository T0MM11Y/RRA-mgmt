package com.twm.mgmt.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.twm.mgmt.model.common.MailVo;


import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MailUtils {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	/**
	 * @param mailVo
	 */
	public void sendSimpleMail(MailVo mailVo) {
		this.sendSimpleMail(mailVo.getFrom(), mailVo.getToEmails().toArray(String[]::new), mailVo.getCopyEmails().toArray(String[]::new), mailVo.getSecretEmails().toArray(String[]::new), mailVo.getSubject(), mailVo.getContent());
	}

	/**
	 * @param from
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param content
	 */
	public void sendSimpleMail(String from, String[] to, String[] cc, String[] bcc, String subject, String content) {
		var simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(from);
		simpleMailMessage.setTo(to);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(content);
		if (cc != null) {
			simpleMailMessage.setCc(cc);
		}
		if (bcc != null) {
			simpleMailMessage.setBcc(bcc);
		}
		mailSender.send(simpleMailMessage);
	}

	/**
	 * @param mailVo
	 */
	public void sendMimeMail(MailVo mailVo) {
		try {
			//System.out.println("entersendMimeMailfunc1");
			var template = freeMarkerConfigurer.getConfiguration().getTemplate(String.format("email/%s", "emailTemplate.html"));
			//System.out.println("entersendMimeMailfunc2");
			

			
			var templateHtml = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailVo.getParams());
			//System.out.println("entersendMimeMailfunc3");
			this.sendMimeMail(mailVo.getFrom(), mailVo.getToEmails().toArray(String[]::new), mailVo.getCopyEmails().toArray(String[]::new), mailVo.getSecretEmails().toArray(String[]::new), mailVo.getSubject(), templateHtml, mailVo.getAttachments());
			//System.out.println("entersendMimeMailfunc4");
		} catch (IOException | TemplateException e) {
			log.error("Mail Send Error: {}", e.getMessage(), e);
		}
	}
	
	public void sendMail(MailVo mailVo,String html) {
		try {
			//System.out.println("entersendMimeMailfunc1");
			var template = freeMarkerConfigurer.getConfiguration().getTemplate(String.format("email/%s", html));
			//System.out.println("entersendMimeMailfunc2");
			

			
			var templateHtml = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailVo.getParams());
			//System.out.println("entersendMimeMailfunc3");
			this.sendMimeMail(mailVo.getFrom(), mailVo.getToEmails().toArray(String[]::new), mailVo.getCopyEmails().toArray(String[]::new), mailVo.getSecretEmails().toArray(String[]::new), mailVo.getSubject(), templateHtml, mailVo.getAttachments());
			//System.out.println("entersendMimeMailfunc4");
		} catch (IOException | TemplateException e) {
			log.error("Mail Send Error: {}", e.getMessage(), e);
		}
	}

	/**
	 * @param from
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param content
	 * @param attachments
	 */
	public void sendMimeMail(String from, String[] to, String[] cc, String[] bcc, String subject, String content,
			Map<String, byte[]> attachments) {
		try {
			var mimeMessage = mailSender.createMimeMessage();
			var mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setTo(to);
			if (cc != null) {
				mimeMessageHelper.setCc(cc);
			}
			if (bcc != null) {
				mimeMessageHelper.setBcc(bcc);
			}
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(content, true);// 第二個參數true 格式是否為html
			
			if (attachments != null && !attachments.isEmpty()) {
				for (var entry : attachments.entrySet()) {
					mimeMessageHelper.addAttachment(entry.getKey(), new ByteArrayDataSource(entry.getValue(), MediaType.APPLICATION_OCTET_STREAM_VALUE));
				}
			}
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			log.error("Mail Send Error: {}", e.getMessage(), e);
		}
	}



}
