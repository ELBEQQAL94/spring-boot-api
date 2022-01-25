package com.learning.app.ws.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "FirstName is required.")
    @Size(min = 3, max = 50)
    private String firstName;

    @NotBlank(message = "LastName is required.")
    @Size(min = 3, max = 50)
    private String lastName;

    @NotEmpty(message = "Email is required.")
    @Email
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 50)
    private String password;

    private List<AddressRequest> addresses;

    private ContactRequest contact;
}
