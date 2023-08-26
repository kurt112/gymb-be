package com.kurt.gym.infrastructure.jwt;

import lombok.AllArgsConstructor;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kurt.gym.auth.model.user.User;

@AllArgsConstructor
public class UserPrincipal implements UserDetails{
    
    private final User user;
    
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNotExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialNotExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

}



