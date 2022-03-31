package com.training.helpdesk.attachment.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AttachmentDto implements Serializable {

    private static final long serialVersionUID = 1677846402910361449L;

    private Long id;
    private String name;
}
