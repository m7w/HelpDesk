package com.training.helpdesk.attachment.dto;

import java.io.Serializable;

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
public class AttachmentDto implements Serializable {

    private static final long serialVersionUID = 1677846402910361449L;

    private Long id;
    private String name;
}
