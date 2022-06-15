package com.training.helpdesk.mail;

import com.training.helpdesk.user.domain.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MailDetails {

    private final User user;
    private final Long ticketId;
}
