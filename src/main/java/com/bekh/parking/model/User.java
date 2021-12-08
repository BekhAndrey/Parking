package com.bekh.parking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NotBlank(message = "First Name cannot be empty.")
    @Size(min = 2, max = 25, message = "First Name cannot be less than 2 and more than 25 symbols")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Last Name cannot be empty.")
    @Size(min = 2, max = 25, message = "Last Name cannot be less than 2 and more than 25 symbols")
    private String lastName;

    @Email(message = "Invalid email template.")
    @NotBlank(message = "Email cannot be empty.")
    private String email;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 8, message = "Password cannot be less than 8 symbols")
    private String password;

    @Transient
    private String confirmPassword;

    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "updated_at")
    private LocalDate updatedAt = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_USER;

    private Boolean approved = false;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<Vehicle> vehicles;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Order> orders;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

