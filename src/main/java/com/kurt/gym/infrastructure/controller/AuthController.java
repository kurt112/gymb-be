package com.kurt.gym.infrastructure.controller;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kurt.gym.auth.model.services.user.UserService;
import com.kurt.gym.auth.model.user.User;
import com.kurt.gym.infrastructure.jwt.AuthenticationRequest;
import com.kurt.gym.infrastructure.jwt.Jwt;
import com.kurt.gym.infrastructure.jwt.UserDetailsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final Jwt jwt;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest credentials) {
        HashMap<String, Object> result = new HashMap<>();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getUsername(),
                            credentials.getPassword()));
        } catch (BadCredentialsException badCredentialsException) {
            result.put("message", "Account Not Found");
            return ResponseEntity.badRequest().body(result);
        } catch (DisabledException disabledException) {
            result.put("message", "Please Verify Your Email First");
            return ResponseEntity.badRequest().body(result);
        } catch (Exception e) {
            System.out.println("i am hereee");
            // e.printStackTrace();
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getUsername());

        final User user = userDetailsService.getUser();

        final String jwt = this.jwt.generateToken(user, true);
        result.put("token", jwt);
        result.put("message", "Login Successful");

        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }
}
