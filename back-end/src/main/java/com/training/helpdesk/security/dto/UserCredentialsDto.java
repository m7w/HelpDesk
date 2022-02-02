package com.training.helpdesk.security.dto;

import java.io.Serializable;

import com.training.helpdesk.security.validator.EmailConstraint;
import com.training.helpdesk.security.validator.PasswordConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsDto implements Serializable {

    private static final long serialVersionUID = 1039722853809869180L;

    @EmailConstraint
    private String email;
    
    @PasswordConstraint
    private String password;
}
