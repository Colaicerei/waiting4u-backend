package edu.osu.waiting4ubackend.controller;

import edu.osu.waiting4ubackend.client.PetDBClient;
import edu.osu.waiting4ubackend.client.UserDBClient;
import edu.osu.waiting4ubackend.entity.Pet;
import edu.osu.waiting4ubackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import java.util.List;

@Controller
@EnableScheduling
public class MailController {
    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 * * * * ?") //test every minute
    //@Scheduled(cron = "0 0 18 * * MON-FRI") //daily
    public void sendmailDaily() throws MessagingException {
        UserDBClient userDBClient = new UserDBClient();
        List<String> dailyList = userDBClient.getUserEmails("Daily");
        if (CollectionUtils.isEmpty(dailyList)) {
            System.out.println("no daily emails subscribed");
            return;
        }
        PetDBClient petDBClient = new PetDBClient();
        List<Pet> dailyPetList = petDBClient.getPets();
        for (String email : dailyList) {
            emailService.sendMail(email, "Daily updates", dailyPetList);
            System.out.println("daily email sent to " + email);
        }
    }

    @Scheduled(cron = "0 35 * * * ?") //test every hour
    //@Scheduled(cron = "0 0 18 * * FRI")    //weekly
    public void sendmailWeekly() throws MessagingException {
        UserDBClient userDBClient = new UserDBClient();
        List<String> weeklyList = userDBClient.getUserEmails("Weekly");
        if (CollectionUtils.isEmpty(weeklyList)) {
            System.out.println("no weekly emails subscribed");
            return;
        }
        PetDBClient petDBClient = new PetDBClient();
        List<Pet> weeklyPetList = petDBClient.getPets();
        for (String email : weeklyList) {
            emailService.sendMail(email, "Weekly updates", weeklyPetList);
        }
    }

    @Scheduled(cron = "0 38 * * * ?") //test every hour
    //@Scheduled(cron = "0 0 18 18 * ?")    //monthly
    public void sendmailMonthly() throws MessagingException {
        UserDBClient userDBClient = new UserDBClient();
        List<String> monthlyList = userDBClient.getUserEmails("Monthly");
        if (CollectionUtils.isEmpty(monthlyList)) {
            System.out.println("no monthly emails subscribed");
            return;
        }
        PetDBClient petDBClient = new PetDBClient();
        List<Pet> monthlyPetList = petDBClient.getPets();
        for (String email : monthlyList) {
            emailService.sendMail(email, "Monthly updates", monthlyPetList);
            System.out.println("monthly email sent to " + email);
        }
    }

}
