package com.training.helpdesk.comment.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CommentDto implements Serializable {

    private static final long serialVersionUID = -780331728277277684L;
    private static final String VALIDATION_REGEXP = "[ a-zA-Z0-9~.\"(),:;<>@\\[\\]!#$%&'*+\\-/=?^_`{|}]*";

    @NotNull
    @Min(1)
    private Long userId;

    private String user;

    @Length(max = 500, message = "Comment must be maximum 500 characters.")
    @Pattern(regexp = VALIDATION_REGEXP, message = "Comment text is not valid.")
    private String text;

    private LocalDateTime date;

    @NotNull
    @Min(1)
    private Long ticketId;
}
