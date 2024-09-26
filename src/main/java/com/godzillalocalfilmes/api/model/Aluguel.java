package com.godzillalocalfilmes.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "alugueis", uniqueConstraints = @UniqueConstraint(columnNames = "cliente_id"))
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Um cliente por aluguel
    @OneToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private Cliente cliente;

    // Um filme por aluguel
    @ManyToOne
    @JoinColumn(name = "filme_id", referencedColumnName = "filmesId")
    private Filme filme;
}
