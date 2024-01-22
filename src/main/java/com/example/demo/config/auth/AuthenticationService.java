package com.example.demo.config.auth;

import com.example.demo.config.jwt.JwtService;
import com.example.demo.constant.MessageConstant;
import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {

        Assert.isTrue(
                !userAccountRepository.existsByEmail(registerRequest.getEmail()),
                MessageConstant.USER_EXISTS
        );

        UserAccount userAccount = UserAccount.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        String jwtToken = jwtService.generateToken(userAccount);
        userAccountRepository.save(userAccount);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        var userAccount = userAccountRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new NoSuchElementException(MessageConstant.USER_DOES_NOT_EXISTS));
        String jwtToken = jwtService.generateToken(userAccount);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}