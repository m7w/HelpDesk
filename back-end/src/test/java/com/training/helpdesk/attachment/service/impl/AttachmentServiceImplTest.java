package com.training.helpdesk.attachment.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.training.helpdesk.attachment.converter.AttachmentConverter;
import com.training.helpdesk.attachment.domain.Attachment;
import com.training.helpdesk.attachment.dto.AttachmentDto;
import com.training.helpdesk.attachment.repository.AttachmentRepository;
import com.training.helpdesk.ticket.domain.Ticket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AttachmentServiceImplTest {

    private static final String FILENAME = "file.pdf";
    private static final Long ATTACHMENT_ID = 1L;
    private static final Attachment ATTACHMENT = new Attachment(ATTACHMENT_ID, "DEADBEEF".getBytes(),
            new Ticket(), FILENAME);
    private static final Long TICKET_ID = 1L;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private AttachmentConverter attachmentConverter;

    @Test
    public void testFindByTicketId() {
        final int expectedLength = 2;
        final AttachmentDto dto = new AttachmentDto(ATTACHMENT_ID, FILENAME);
        final List<Attachment> attachments = List.of(ATTACHMENT, ATTACHMENT);
        final List<AttachmentDto> dtos = List.of(dto, dto);

        when(attachmentRepository.findByTicketId(TICKET_ID)).thenReturn(attachments);
        when(attachmentConverter.toDto(attachments)).thenReturn(dtos);

        final List<AttachmentDto> actual = attachmentService.findByTicketId(TICKET_ID);

        assertEquals(expectedLength, actual.size());
        assertEquals(dto, actual.get(0));
        assertEquals(dto, actual.get(1));
        verify(attachmentRepository).findByTicketId(TICKET_ID);
        verify(attachmentConverter).toDto(attachments);
        verifyNoMoreInteractions(attachmentRepository, attachmentConverter);
    }

    @Test
    public void testFindById_NotFound() {
        when(attachmentRepository.findById(ATTACHMENT_ID)).thenReturn(Optional.empty());
        final String expected = "Attachment with id=1 not found";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> attachmentService.findById(ATTACHMENT_ID));
        
        assertEquals(expected, exception.getMessage());
        verify(attachmentRepository).findById(ATTACHMENT_ID);
        verifyNoMoreInteractions(attachmentRepository);
        verifyNoInteractions(attachmentConverter);
    }
}

