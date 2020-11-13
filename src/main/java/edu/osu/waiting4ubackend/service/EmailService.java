package edu.osu.waiting4ubackend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String toEmail, String subject, String message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailMessage.setFrom("waiting4u@gmail.com");

        javaMailSender.send(mailMessage);
    }

    //https://mkyong.com/spring-boot/spring-boot-how-to-send-email-via-smtp/
    /*public void sendMailWithAttachment(String toEmail, String subject, String message) throws MessagingException, IOException {

        MimeMailMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(message);

        helper.setFrom("waiting4u.news@gmail.com");
        helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

        javaMailSender.send(mailMessage);
    }*/

}

