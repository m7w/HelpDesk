package com.training.helpdesk.feedback.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.training.helpdesk.ticket.domain.Ticket;
import com.training.helpdesk.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feedbacks")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    private User user;

    @Column(name = "rate")
    private Long rate;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "text")
    private String text;

    @OneToOne
    private Ticket ticket;

    @Override
    public int hashCode() {
        return Objects.hash(date, id, rate, text, ticket, user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Feedback))
            return false;
        Feedback other = (Feedback) obj;
        return Objects.equals(date, other.date) && Objects.equals(id, other.id)
            && Objects.equals(rate, other.rate) && Objects.equals(text, other.text)
            && Objects.equals(ticket, other.ticket) && Objects.equals(user, other.user);
    }

    @Override
    public String toString() {
        return "Feedback [id=" + id
            + ", date=" + date 
            + ", rate=" + rate 
            + ", text=" + text 
            + ", ticket=" + ticket
            + ", user=" + user + "]";
    }
}
