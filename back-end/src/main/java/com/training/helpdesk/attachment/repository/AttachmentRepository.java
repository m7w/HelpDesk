package com.training.helpdesk.attachment.repository;

import java.util.List;

import com.training.helpdesk.attachment.domain.Attachment;

public interface AttachmentRepository {

    List<Attachment> findByTicketId(Long id);

    Long save(Attachment attachment);
}
