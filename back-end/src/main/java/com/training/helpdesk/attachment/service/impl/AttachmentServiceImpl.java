package com.training.helpdesk.attachment.service.impl;

import java.io.IOException;
import java.util.List;

import com.training.helpdesk.attachment.converter.AttachmentConverter;
import com.training.helpdesk.attachment.domain.Attachment;
import com.training.helpdesk.attachment.dto.AttachmentDto;
import com.training.helpdesk.attachment.repository.AttachmentRepository;
import com.training.helpdesk.attachment.service.AttachmentService;
import com.training.helpdesk.ticket.repository.TicketRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentConverter attachmentConverter;
    private final TicketRepository ticketRepository;

	@Override
    @Transactional(readOnly = true)
	public List<AttachmentDto> findByTicketId(Long id) {
        return attachmentConverter.toDto(attachmentRepository.findByTicketId(id));
	}

	@Override
    @Transactional
	public Long save(Long ticketId, MultipartFile file) throws IOException {
        Attachment attachment = new Attachment();
        attachment.setBlob(file.getBytes());
        attachment.setName(file.getOriginalFilename());
        attachment.setTicket(ticketRepository.findById(ticketId).
                orElseThrow(() -> new IllegalArgumentException("Exception during uploading file")));
		return attachmentRepository.save(attachment);
	}
}
