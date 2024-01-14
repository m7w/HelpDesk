package com.training.helpdesk.history.domain;

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
@Table(name = "history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Ticket ticket;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "action")
    private String action;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "description")
    private String description;

    @Override
    public int hashCode() {
        return Objects.hash(action, date, description, id, ticket, user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof History))
            return false;
        History other = (History) obj;
        return Objects.equals(action, other.action) && Objects.equals(date, other.date)
                && Objects.equals(description, other.description) && Objects.equals(id, other.id)
                && Objects.equals(ticket, other.ticket) && Objects.equals(user, other.user);
    }

    @Override
    public String toString() {
        return "History [id=" + id
            + ", date=" + date 
            + ", user=" + user
            + ", action=" + action
            + ", description=" + description 
            + ", ticket=" + ticket + "]";
    }
}
