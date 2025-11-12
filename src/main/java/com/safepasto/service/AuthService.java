package com.safepasto.service;

import com.safepasto.model.dto.AuthResponse;
import com.safepasto.model.dto.LoginRequest;
import com.safepasto.model.dto.UserDTO;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse register(UserDTO userDTO);
}