package com.accolite.controller;

import java.security.Principal;
import java.util.Objects;

import com.accolite.entity.AdminUser;
import com.accolite.entity.JwtRequest;
import com.accolite.entity.JwtResponse;
import com.accolite.repository.AdminUserRepository;
import com.accolite.util.JwtTokenUtil;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> generateAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {
        String adminUserName = authenticationRequest.getUsername();
        String adminPassword = authenticationRequest.getPassword();
        if(null != adminUserName && null != adminPassword){
            AdminUser adminUser = adminUserRepository.getAdminUserByCredentials(adminUserName, adminPassword);
            if(null != adminUser){
                final UserDetails userDetails = jwtUserDetailsService
                        .loadUserByUsername(adminUser.getAdminUserName()+":"+adminUser.getAdminPassword());
                final String token = jwtTokenUtil.generateToken(userDetails);
                return ResponseEntity.ok(new JwtResponse(token));
            }else {
                throw new UsernameNotFoundException("User not found with username: " +authenticationRequest.getUsername());
            }
        }
        throw new UsernameNotFoundException("Admin UserName or Admin Password can not be null or empty");
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
    
  
    
}
