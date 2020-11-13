package edu.osu.waiting4ubackend.controller;
import edu.osu.waiting4ubackend.client.UserDBClient;
import edu.osu.waiting4ubackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MailController {

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/sendmail")
    public String sendmail() {
        UserDBClient userDBClient = new UserDBClient();;
        List<String> mailList = userDBClient.getUserEmails();
        for(String email: mailList){
            emailService.sendMail(email, "weekly updates", "Test mail");
            Object system;
            System.out.println("email sent to " + email);
        }
        return "emailsent";
    }
}


//http://zetcode.com/springboot/email/