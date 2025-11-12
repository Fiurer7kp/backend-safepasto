package com.safepasto.service.impl;

import com.safepasto.model.dto.AuthResponse;
import com.safepasto.model.dto.LoginRequest;
import com.safepasto.model.dto.UserDTO;
import com.safepasto.model.User;
import com.safepasto.repository.UserRepository;
import com.safepasto.security.JwtUtil;
import com.safepasto.security.CustomUserDetailsService;
import com.safepasto.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder; // ← IMPORT AÑADIDO
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder; // ← INYECTADO

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        // Autenticar usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Obtener UserDetails para generar el token
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());

        // Generar token JWT
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        // Buscar el usuario en la base de datos para obtener más datos
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());

        // Retornar AuthResponse con UserDTO
        return new AuthResponse(jwt, userDTO);
    }

    @Override
    public AuthResponse register(UserDTO userDTO) {
        // Check duplicates
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists: " + userDTO.getUsername());
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already registered: " + userDTO.getEmail());
        }

        // Map DTO to entity
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Persist user
        User savedUser = userRepository.save(user);

        // Generar token JWT
        String jwt = jwtUtil.generateToken(savedUser.getUsername());

        // Build response DTO
        UserDTO responseUser = new UserDTO();
        responseUser.setId(savedUser.getId());
        responseUser.setUsername(savedUser.getUsername());
        responseUser.setEmail(savedUser.getEmail());
        responseUser.setFirstName(savedUser.getFirstName());
        responseUser.setLastName(savedUser.getLastName());

        return new AuthResponse(jwt, responseUser);
    }
}