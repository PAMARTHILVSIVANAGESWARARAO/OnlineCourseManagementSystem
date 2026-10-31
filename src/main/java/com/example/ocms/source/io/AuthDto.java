package com.example.ocms.source.io;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthDto {
    private String email;
    private String password;
    private String role;
}
