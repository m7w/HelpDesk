package com.training.helpdesk.attachment.converter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.training.helpdesk.attachment.domain.Attachment;
import com.training.helpdesk.attachment.dto.AttachmentDto;
import com.training.helpdesk.ticket.domain.Ticket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AttachmentConverterImplTest {

    private static final String FILENAME = "file.pdf";
    private static final Long ATTACHMENT_ID = 1L;
    private static final Attachment ATTACHMENT = new Attachment(ATTACHMENT_ID, "DEADBEEF".getBytes(),
            new Ticket(), FILENAME);

    @InjectMocks
    private AttachmentConverterImpl attachmentConverter;

    @Test
    public void testToDto() {
        final AttachmentDto expected = new AttachmentDto(ATTACHMENT_ID, FILENAME);

        final AttachmentDto actual = attachmentConverter.toDto(ATTACHMENT);

        assertEquals(expected, actual);
    }
}
