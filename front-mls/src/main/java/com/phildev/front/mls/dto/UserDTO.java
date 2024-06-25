package com.phildev.front.mls.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String firstname;

    private String lastname;

    private String role;
    private String structureName;

    private String email;
}
