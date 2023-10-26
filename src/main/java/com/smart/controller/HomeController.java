package com.smart.controller;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.DTOclass.LoginDto;
import com.smart.DTOclass.SignUpDto;
import com.smart.repository.RoleRepository;
import com.smart.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class HomeController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User login successfully!...", HttpStatus.OK);
    }
    
    
        @PostMapping("/signup")
        public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
            // checking for username exists in a database
            if(userRepository.existsByUserName(signUpDto.getUsername())){
                return new ResponseEntity<>("Username is already exist!", HttpStatus.BAD_REQUEST);
            }
            // checking for email exists in a database
            if(userRepository.existsByEmail(signUpDto.getEmail())){
                return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
            }
            // creating user object
            User user = new User();
            user.setName(signUpDto.getName());
            user.setUserName(signUpDto.getUsername());
            user.setEmail(signUpDto.getEmail());
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            Role roles = roleRepository.findByName("ROLE_ADMIN").get();
            user.setRoles(Collections.singleton(roles));
            userRepository.save(user);
            return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
        }
}