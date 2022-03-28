package com.training.helpdesk.attachment.converter;

import java.util.List;

import com.training.helpdesk.attachment.domain.Attachment;
import com.training.helpdesk.attachment.dto.AttachmentDto;

public interface AttachmentConverter {

    public AttachmentDto toDto(Attachment attachment);

    public List<AttachmentDto> toDto(List<Attachment> attachments);
}
