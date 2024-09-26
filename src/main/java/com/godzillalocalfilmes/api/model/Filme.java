package com.godzillalocalfilmes.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "filmes")
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmesId;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String diretor;

    @Column(nullable = false)
    private int estoque;

    @Column(nullable = false)
    private int ano;
}
