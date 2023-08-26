package com.kurt.gym.infrastructure.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResonse {
    private final String jwt;
}
