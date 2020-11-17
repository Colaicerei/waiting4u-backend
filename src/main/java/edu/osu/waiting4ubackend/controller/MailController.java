package edu.osu.waiting4ubackend.controller;
import edu.osu.waiting4ubackend.client.UserDBClient;
import edu.osu.waiting4ubackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@EnableScheduling
public class MailController {
    @Autowired
    private EmailService emailService;

    //@Scheduled(cron = "0 * * * * ?") //test every minute
    @Scheduled(cron = "0 0 18 * * MON-FRI") //daily
    public String sendmailDaily() {
        UserDBClient userDBClient = new UserDBClient();;
        List<String> dailyList = userDBClient.getUserEmails("Daily");
        for(String email: dailyList){
            emailService.sendMail(email, "Daily updates", "New Pets Added");
            System.out.println("daily email sent to " + email);
        }
        return "daily emails sent";
    }

    //@Scheduled(cron = "0 18 * * * ?") //test every hour
    @Scheduled(cron = "0 0 18 * * FRI")    //weekly
    public String sendmailWeekly() {
        UserDBClient userDBClient = new UserDBClient();;
        List<String> weeklyList = userDBClient.getUserEmails("Weekly");
        for(String email: weeklyList){
            emailService.sendMail(email, "Weekly updates", "New Pets Added");
            System.out.println("weekly email sent to " + email);
        }
        return "weekly emails sent";
    }

    //@Scheduled(cron = "0 28 * * * ?") //test every hour
    @Scheduled(cron = "0 0 18 18 * ?")    //monthly
    public String sendmailMonthly() {
        UserDBClient userDBClient = new UserDBClient();;
        List<String> monthlyList = userDBClient.getUserEmails("Monthly");
        for(String email: monthlyList){
            emailService.sendMail(email, "Monthly updates", "New Pets Added");
            System.out.println("monthly email sent to " + email);
        }
        return "monthly emails sent";
    }
}
