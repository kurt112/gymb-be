package com.kurt.gym.infrastructure.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kurt.gym.auth.model.services.user.UserService;
import com.kurt.gym.auth.model.user.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserService userService;
    private User user;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null ) throw new UsernameNotFoundException(email);
        this.user = user;
        return new UserPrincipal(user);
    }

    public User getUser() {
        return user;
    }
}
