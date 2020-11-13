package edu.osu.waiting4ubackend.controller;
import edu.osu.waiting4ubackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MailController {

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/sendmail")
    public String sendmail() {

        emailService.sendMail("colaicerei@gmail.com", "weekly updates", "Test mail");

        return "emailsent";
    }
}



//http://zetcode.com/springboot/email/