package edu.osu.waiting4ubackend.service;

import edu.osu.waiting4ubackend.entity.Pet;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    private TemplateEngine templateEngine;

    public EmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    /*public void sendMail(String toEmail, String subject, String message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailMessage.setFrom("waiting4u@gmail.com");

        javaMailSender.send(mailMessage);
    }*/

    //https://springhow.com/spring-boot-email-thymeleaf/
    public void sendMail(String toEmail, String subject, List<Pet> pets) throws MessagingException {
        Context context = new Context();
        context.setVariable("pets", pets);

        String process = templateEngine.process("emailTemplate", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(process, true);

        javaMailSender.send(mimeMessage);
    }


    // forward the contact form submission to our service email
    public void sendContactForm(String subject, String sender, String body) {
        SimpleMailMessage contactMessage = new SimpleMailMessage();

        contactMessage.setTo("waiting4u.service@gmail.com");
        contactMessage.setSubject(subject);
        contactMessage.setText("message from " + sender + ":\n" + body);

        contactMessage.setFrom("waiting4u.service@gmail.com");

        javaMailSender.send(contactMessage);
    }

    // auto reply upon contact form submission
    public void autoReply(String senderName, String senderEmail) {
        SimpleMailMessage replyMessage = new SimpleMailMessage();

        replyMessage.setTo(senderEmail);
        replyMessage.setSubject("Successful form submission");
        replyMessage.setText("Hi " + senderName + ":\n" +
                               "We have received your message and will get back to your shortly.");

        replyMessage.setFrom("waiting4u.service@gmail.com");

        javaMailSender.send(replyMessage);
    }

}

