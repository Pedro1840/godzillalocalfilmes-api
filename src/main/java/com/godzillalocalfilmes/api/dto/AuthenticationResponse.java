package com.godzillalocalfilmes.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private boolean auth;
    private ClienteDTO usuario;
    private String token;
}
