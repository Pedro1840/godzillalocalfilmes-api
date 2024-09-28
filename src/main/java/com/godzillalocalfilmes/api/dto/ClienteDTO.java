package com.godzillalocalfilmes.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClienteDTO {
    private Long id;
    private String email;
    private String nome;
}
