package com.training.helpdesk.mail.service;

import com.training.helpdesk.mail.MailDetails;
import com.training.helpdesk.ticket.domain.State;
import com.training.helpdesk.ticket.domain.Ticket;

public interface MailService {

    void notify(Ticket ticket, State oldState);

    void sendMail(int templateNumber, String subject, MailDetails mailDetails);
}

