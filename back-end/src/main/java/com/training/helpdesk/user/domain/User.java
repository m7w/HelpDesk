package com.training.helpdesk.user.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "role_id")
    private Role role;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, id, lastName, role);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof User))
            return false;
        User other = (User) obj;
        return Objects.equals(email, other.email)
            && Objects.equals(firstName, other.firstName) && Objects.equals(id, other.id)
            && Objects.equals(lastName, other.lastName) && role == other.role;
    }

    @Override
    public String toString() {
        return "User [id=" + id
            + ", firstName=" + firstName
            + ", lastName=" + lastName
            + ", roleId=" + role 
            + ", email=" + email +"]";
    }

}
