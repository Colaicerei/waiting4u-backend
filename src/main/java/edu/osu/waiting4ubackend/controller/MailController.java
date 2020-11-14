package edu.osu.waiting4ubackend.controller;
import edu.osu.waiting4ubackend.client.UserDBClient;
import edu.osu.waiting4ubackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@EnableScheduling
public class MailController {

    @Autowired
    private EmailService emailService;

    // daily @Scheduled(cron = "0 0 12 * * MON-FRI")
    // weekly @Scheduled(cron = "0 0 12 * * FRI")
    // monthly @Scheduled(cron = "0 0 12 15 * ?")
    @Scheduled(cron = "0 10 * * * ?") //test every hour
    @GetMapping(value = "/sendmail")
    public String sendmail() {
        UserDBClient userDBClient = new UserDBClient();;
        List<String> mailList = userDBClient.getUserEmails();
        for(String email: mailList){
            emailService.sendMail(email, "weekly updates", "New Pets Added");
            Object system;
            System.out.println("email sent to " + email);
        }
        return "emailsent";
    }
}


//http://zetcode.com/springboot/email/