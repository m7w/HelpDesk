package com.training.helpdesk.attachment.domain;

import java.util.Arrays;
import java.util.Objects;

import com.training.helpdesk.ticket.domain.Ticket;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attachments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "blob")
    private byte[] blob;

    @ManyToOne private Ticket ticket;

    @Column(name = "name")
    private String name;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(blob);
        result = prime * result + Objects.hash(id, name, ticket);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Attachment)) return false;
        Attachment other = (Attachment) obj;
        return Arrays.equals(blob, other.blob)
                && Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(ticket, other.ticket);
    }

    @Override
    public String toString() {
        return "Attachment [id=" + id + ", name=" + name + ", ticket=" + ticket.getName() + "]";
    }
}
