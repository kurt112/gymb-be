package com.kurt.gym.auth.api;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGet {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String cellphone;
    private String gender;
    private Date birthDate;
}
