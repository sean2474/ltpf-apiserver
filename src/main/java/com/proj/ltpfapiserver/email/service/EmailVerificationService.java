package com.proj.ltpfapiserver.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.proj.ltpfapiserver.mapper.EmailVerificationMapper;
import com.proj.ltpfapiserver.model.EmailVerification;

import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import java.time.LocalDateTime;

import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

@Service
public class EmailVerificationService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private EmailVerificationMapper emailVerificationMapper;

  @Autowired
  private TemplateEngine templateEngine;

  public void sendVerificationEmail(String email) throws Exception {
    int code = generateVerificationCode();
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    final Context ctx = new Context();
    ctx.setVariable("code", code);

    String html = templateEngine.process("emailVerification", ctx);
    helper.setTo(email);
    helper.setSubject("LTPF 인증번호");
    helper.setText(html, true);

    javaMailSender.send(message);

    System.out.println("storing... ");
    EmailVerification emailVerification = new EmailVerification();
    emailVerification.setEmail(email);
    emailVerification.setCode(code);
    emailVerification.setExpireDate(LocalDateTime.now().plusMinutes(10));
    emailVerificationMapper.insertVerificationCode(emailVerification);
    System.out.println("stored!!");
  }

  private int generateVerificationCode() {
    return (int) ((Math.random() * 899999) + 100000);
  }

  public boolean verifyEmail(String email, int code) throws Exception {
    EmailVerification emailVerification = emailVerificationMapper.findByEmail(email);
    return emailVerification != null && emailVerification.getCode() == code && emailVerification.getExpireDate().isAfter(LocalDateTime.now());
  }
}
