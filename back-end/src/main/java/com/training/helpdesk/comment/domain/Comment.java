package com.training.helpdesk.comment.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "text", length = 500)
    private String text;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    private Ticket ticket;

    @Override
    public int hashCode() {
        return Objects.hash(date, id, text, ticket, user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Comment))
            return false;
        Comment other = (Comment) obj;
        return Objects.equals(date, other.date) && Objects.equals(id, other.id)
            && Objects.equals(text, other.text) && Objects.equals(ticket, other.ticket)
            && Objects.equals(user, other.user);
    }

    @Override
    public String toString() {
        return "Comment [date=" + date
            + ", id=" + id 
            + ", text=" + text 
            + ", ticket=" + ticket.getName()
            + ", user=" + user.getFirstName() + " " + user.getLastName() + "]";
    }
}
