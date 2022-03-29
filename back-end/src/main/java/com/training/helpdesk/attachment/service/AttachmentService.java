package com.training.helpdesk.attachment.service;

import java.io.IOException;
import java.util.List;

import com.training.helpdesk.attachment.domain.Attachment;
import com.training.helpdesk.attachment.dto.AttachmentDto;

import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    Attachment findById(Long id);

    List<AttachmentDto> findByTicketId(Long id);

    Long save(Long ticketId, MultipartFile file) throws IOException;
}
