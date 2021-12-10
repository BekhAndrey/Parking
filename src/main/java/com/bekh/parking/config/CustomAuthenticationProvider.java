package com.bekh.parking.config;

import com.bekh.parking.model.User;
import com.bekh.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        User user = (User) userService.loadUserByUsername(username);
        if (!bCryptPasswordEncoder.matches(password, user.getPassword()) || !user.getApproved()) {
            throw new BadCredentialsException("Authentication failed for " + username);
        }
        grantedAuths.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));
        return new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
