package com.training.helpdesk.mail.service.impl;

import java.util.List;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.training.helpdesk.mail.MailDetails;
import com.training.helpdesk.mail.service.MailService;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.service.UserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private static final String FRONTEND_HOST = "host";
    private static final String EMAIL_TEMPLATE = "mail/template";
    private static final String MAIL_DETAILS = "mailDetails";
    private static final String ENCODING = "UTF-8";

    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;
    private final UserService userService;

    @Value("${app.frontend-endpoint}")
    private String frontendHost;

    @Override
    public void notify(Ticket ticket, State oldState) {
        if ((oldState == State.DRAFT || oldState == State.DECLINED) && ticket.getState() == State.NEW) {
            List<User> managers = userService.findByRole(Role.ROLE_MANAGER);
            for (User manager: managers) {
                sendMail(1, "New ticket for approval", new MailDetails(manager, ticket.getId()));
            }
        } else if (oldState == State.NEW && ticket.getState() == State.APPROVED) {
            sendMail(2, "Ticket was approved", new MailDetails(ticket.getOwner(), ticket.getId()));
            List<User> engineers = userService.findByRole(Role.ROLE_ENGINEER);
            for (User engineer: engineers) {
                sendMail(2, "Ticket was approved", new MailDetails(engineer, ticket.getId()));
            }
        } else if (oldState == State.NEW && ticket.getState() == State.DECLINED) {
            sendMail(3, "Ticket was declined", new MailDetails(ticket.getOwner(), ticket.getId()));
        } else if (oldState == State.NEW && ticket.getState() == State.CANCELED) {
            sendMail(4, "Ticket was cancelled", new MailDetails(ticket.getOwner(), ticket.getId()));
        } else if (oldState == State.APPROVED && ticket.getState() == State.CANCELED) {
            sendMail(5, "Ticket was cancelled", new MailDetails(ticket.getOwner(), ticket.getId()));
            sendMail(5, "Ticket was cancelled", new MailDetails(ticket.getApprover(), ticket.getId()));
        } else if (oldState == State.IN_PROGRESS && ticket.getState() == State.DONE) {
            sendMail(6, "Ticket was done", new MailDetails(ticket.getOwner(), ticket.getId()));
        }
    }

    @Override
    public void sendMail(int templateNumber, String subject, MailDetails mailDetails) {
        
        final Context ctx = new Context();
        ctx.setVariable(MAIL_DETAILS, mailDetails);
        ctx.setVariable(FRONTEND_HOST, frontendHost);

        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, ENCODING);
        try {
            message.setSubject(subject);
            message.setTo(mailDetails.getUser().getEmail());

            final String content = htmlTemplateEngine.process(EMAIL_TEMPLATE + templateNumber, ctx);
            message.setText(content, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Error sending email to " + mailDetails.getUser().getEmail() + " " + e.getMessage());
        }
    }
}
