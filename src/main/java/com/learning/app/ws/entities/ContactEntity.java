package com.learning.app.ws.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "contacts")
public class ContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 30)
    private String contactId;

    @NotBlank
    @Column(nullable = false)
    private String mobile;

    @OneToOne
    @JoinColumn(name = "users_id")
    private UserEntity user;
}
