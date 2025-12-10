package com.project.moneymanager.service;


import com.project.moneymanager.dto.ExpenseDto;
import com.project.moneymanager.entity.ProfileEntity;
import com.project.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;
    private final ProfileRepository profileRepository;
    private final ExpenseService expenseService;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

    @Scheduled(cron = "0 0 22 * * *",zone = "IST") // Every day at 10 PM
    public void sendDailyIncomeExpenseReminder(){
        log.info("Job started: Sending daily income and expense reminder emails to users.");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile: profiles ){
            String body="Hi"+profile.getFullName() +"<br><br>"+
                    "This is a gentle reminder to log your daily income and expenses. Keeping track of your finances is crucial for effective money management.<br><br>"+
                    "You can log your transactions by visiting the following link:<br>"+
                    "<a href="+frontendUrl+" style={display:inline-block; padding: 10px 20px; background-color: #4CAF50;color:#fff;text-decoration:none;border-radius: 5px; font-weight:bold;'>Go to Monay Manager</a>"+
                    "Thank you for using our Money Manager App!<br><br>"+
                    "Best regards,<br>"+
                    "Money Manager Team";
            emailService.sendEmail(profile.getEmail(),"Daily Reminder: Log Your Income and Expenses",body);

        }
    }

    @Scheduled(cron = "0 0 23 * * *",zone = "IST") // Every day at 11 PM
    public void sendDailyExpenseSummary(){
        log.info("Job started: Sending daily expense summary emails to users.");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for(ProfileEntity profile: profiles ){
            List<ExpenseDto> todaysExpenses = expenseService.getExpensesByDate(profile.getId(),java.time.LocalDate.now());
            if(!todaysExpenses.isEmpty())
            {
                StringBuilder body = new StringBuilder();
                body.append("Hi ").append(profile.getFullName()).append(",<br><br>")
                        .append("Here is a summary of your expenses for today:<br><br>")
                        .append("<table style='width:100%; border-collapse: collapse;'>")
                        .append("<tr><th style='border: 1px solid #ddd; padding: 8px;'>Name</th>")
                        .append("<th style='border: 1px solid #ddd; padding: 8px;'>Category</th>")
                        .append("<th style='border: 1px solid #ddd; padding: 8px;'>Amount</th>")
                        .append("<th style='border: 1px solid #ddd; padding: 8px;'>Date</th></tr>");

                for (ExpenseDto expense : todaysExpenses) {
                    body.append("<tr>")
                            .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(expense.getName()).append("</td>")
                            .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(expense.getCategoryName()).append("</td>")
                            .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(expense.getAmount()).append("</td>")
                            .append("<td style='border: 1px solid #ddd; padding: 8px;'>").append(expense.getDate()).append("</td>")
                            .append("</tr>");
                }

                body.append("</table><br>")
                        .append("Thank you for using our Money Manager App!<br><br>")
                        .append("Best regards,<br>")
                        .append("Money Manager Team");

                emailService.sendEmail(profile.getEmail(), "Daily Expense Summary", body.toString());
            }

        }
    }


}
