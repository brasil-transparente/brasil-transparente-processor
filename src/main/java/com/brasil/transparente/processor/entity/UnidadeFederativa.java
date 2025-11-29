package com.brasil.transparente.processor.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "unidade_federativa")
public class UnidadeFederativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unidadeFederativaId;

    private String nameUnidadeFederativa;

    private double totalValueSpent;

    @OneToMany(mappedBy = "unidadeFederativa", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Poder> listPoder;

    public UnidadeFederativa(String nameUnidadeFederativa) {
        this.nameUnidadeFederativa = nameUnidadeFederativa;
        this.listPoder = new ArrayList<>();
        this.totalValueSpent = 0.0;
    }

}
