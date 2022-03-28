package com.training.helpdesk.attachment.converter.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.training.helpdesk.attachment.converter.AttachmentConverter;
import com.training.helpdesk.attachment.domain.Attachment;
import com.training.helpdesk.attachment.dto.AttachmentDto;

import org.springframework.stereotype.Component;

@Component
public class AttachmentConverterImpl implements AttachmentConverter {

	@Override
	public AttachmentDto toDto(Attachment attachment) {
        AttachmentDto attachmentDto = new AttachmentDto();
        attachmentDto.setId(attachment.getId());
        attachmentDto.setName(attachment.getName());
		return attachmentDto;
	}

	@Override
	public List<AttachmentDto> toDto(List<Attachment> attachments) {
        return attachments.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
	}
}
