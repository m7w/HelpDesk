package com.training.helpdesk.attachment.repository;

import java.util.List;
import java.util.Optional;

import com.training.helpdesk.attachment.domain.Attachment;

public interface AttachmentRepository {

    Optional<Attachment> findById(Long id);

    List<Attachment> findByTicketId(Long id);

    Long save(Attachment attachment);
}
