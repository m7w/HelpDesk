package com.training.helpdesk.attachment.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.training.helpdesk.AbstractControllerTest;
import com.training.helpdesk.attachment.domain.Attachment;
import com.training.helpdesk.attachment.service.AttachmentService;
import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.ticket.util.AccessChecker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.aot.DisabledInAotMode;

@DisabledInAotMode
public class AttachmentControllerTest extends AbstractControllerTest {

    private static final String ATTACHMENT_FILENAME = "attachment; filename=file.pdf";
    private static final Long TICKET_ID = 1L;
    private static final Ticket TICKET = new Ticket();
    private static final Long ATTACHMENT_ID = 1L;
    private static final Attachment ATTACHMENT =
            new Attachment(ATTACHMENT_ID, "DEADBEEF".getBytes(), TICKET, "file.pdf");

    @MockBean private AttachmentService attachmentService;

    @MockBean private AccessChecker accessChecker;

    @Test
    public void testGetById() throws Exception {
        when(accessChecker.hasAccess(TICKET_ID)).thenReturn(true);
        when(attachmentService.findById(ATTACHMENT_ID)).thenReturn(ATTACHMENT);

        mockMvc.perform(get("/api/tickets/{ticketId}/attachments/{id}", TICKET_ID, ATTACHMENT_ID))
                .andExpect(status().isOk())
                .andExpect(
                        header().stringValues(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT_FILENAME))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes("DEADBEEF".getBytes()));

        verify(attachmentService).findById(ATTACHMENT_ID);
        verifyNoMoreInteractions(attachmentService);
    }

    @Test
    void testUpload_isOwner() throws Exception {
        final MockMultipartFile file =
                new MockMultipartFile("file", "file.pdf", "application/pdf", new byte[10]);
        when(accessChecker.isOwner(TICKET_ID)).thenReturn(true);
        when(attachmentService.save(TICKET_ID, file)).thenReturn(ATTACHMENT_ID);

        mockMvc.perform(multipart("/api/tickets/{ticketId}/attachments", TICKET_ID).file(file))
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(ATTACHMENT_ID)));

        verify(attachmentService).save(TICKET_ID, file);
        verifyNoMoreInteractions(attachmentService);
    }

    @Test
    void testUpload_isNotOwner() throws Exception {
        final MockMultipartFile file =
                new MockMultipartFile("file", "file.pdf", "application/pdf", new byte[10]);
        when(accessChecker.isOwner(TICKET_ID)).thenReturn(false);

        mockMvc.perform(multipart("/api/tickets/{ticketId}/attachments", TICKET_ID).file(file))
                .andExpect(status().isForbidden());

        verifyNoMoreInteractions(attachmentService);
    }

    @Test
    void testUpload_UnsupportedFileType() throws Exception {
        final MockMultipartFile file =
                new MockMultipartFile("file", "file.pdf", "text/plain", new byte[10]);
        when(accessChecker.isOwner(TICKET_ID)).thenReturn(true);

        mockMvc.perform(multipart("/api/tickets/{ticketId}/attachments", TICKET_ID).file(file))
                .andExpect(status().isUnsupportedMediaType());

        verifyNoInteractions(attachmentService);
    }
}
