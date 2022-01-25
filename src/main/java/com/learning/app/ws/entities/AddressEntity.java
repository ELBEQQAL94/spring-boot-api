package com.learning.app.ws.entities;

import com.learning.app.ws.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "addresses")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String AddressId;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 50)
    private String country;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false, length = 20)
    private String postal_code;

    @Column(nullable = false, length = 20)
    private String type;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity user;
}
