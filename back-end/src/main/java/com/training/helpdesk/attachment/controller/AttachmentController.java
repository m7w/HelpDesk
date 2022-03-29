package com.training.helpdesk.attachment.controller;

import java.io.IOException;
import java.util.List;

import com.training.helpdesk.attachment.domain.Attachment;
import com.training.helpdesk.attachment.dto.AttachmentDto;
import com.training.helpdesk.attachment.service.AttachmentService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tickets")
public class AttachmentController {

    private static final String ATTACHMENT_FILENAME = "attachment; filename=";

    private final AttachmentService attachmentService;

    @GetMapping(value = "/{ticketId}/attachments/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getById(@PathVariable("ticketId") Long ticketId,
            @PathVariable("id") Long id) {

        Attachment attachment = attachmentService.findById(id);
        ByteArrayResource resource = new ByteArrayResource(attachment.getBlob());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT_FILENAME + attachment.getName());

        return ResponseEntity.ok().headers(headers)
            .contentLength(attachment.getBlob().length)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
    }

    @GetMapping(value = "/{ticketId}/attachments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AttachmentDto>> getByTicketId(@PathVariable("ticketId") Long ticketId) {

        return ResponseEntity.ok(attachmentService.findByTicketId(ticketId));
    }

    @PostMapping("/{ticketId}/attachments")
    public ResponseEntity<Long> upload(@PathVariable("ticketId") Long ticketId,
            @RequestParam("file") MultipartFile file) throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(attachmentService.save(ticketId, file));
    }
}
