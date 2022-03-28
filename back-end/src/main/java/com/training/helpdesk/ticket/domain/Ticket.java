package com.training.helpdesk.ticket.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.training.helpdesk.category.domain.Category;
import com.training.helpdesk.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tickets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "desired_resolution_date")
    private LocalDate desiredResolutionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(name = "state_id")
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column(name = "urgency_id")
    private Urgency urgency;

    @ManyToOne(fetch = FetchType.LAZY)
    private User approver;

    @Override
    public int hashCode() {
        return Objects.hash(approver, assignee, category, createdOn, description,
                desiredResolutionDate, id, name, owner, state, urgency);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Ticket))
            return false;
        Ticket other = (Ticket) obj;
        return Objects.equals(approver, other.approver) && Objects.equals(assignee, other.assignee)
                && Objects.equals(category, other.category) && Objects.equals(createdOn, other.createdOn)
                && Objects.equals(description, other.description)
                && Objects.equals(desiredResolutionDate, other.desiredResolutionDate)
                && Objects.equals(id, other.id) && Objects.equals(name, other.name)
                && Objects.equals(owner, other.owner) && state == other.state 
                && urgency == other.urgency;
    }

    @Override
    public String toString() {
        return "Ticket [id=" + id 
            + ", name=" + name 
            + ", description=" + description 
            + ", createdOn=" + createdOn 
            + ", desiredResolutionDate=" + desiredResolutionDate
            + ", assignee=" + assignee.getFirstName() + assignee.getLastName()
            + ", owner=" + owner.getFirstName() + owner.getLastName() 
            + ", state=" + state 
            + ", category=" + category.getName()
            + ", urgencyId=" + urgency
            + ", approver=" + approver.getFirstName() + approver.getLastName() + "]";
    }
}
