package com.brasil.transparente.processor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "poder")
public class Poder implements UnidadeOrcamentaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poderId;

    private String namePoder;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "unidade_federativa_id", referencedColumnName = "unidadeFederativaId")
    private UnidadeFederativa unidadeFederativa;

    @OneToMany(mappedBy = "poder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Ministerio> listMinisterio;

    private double totalValueSpent;

    private double percentageOfTotal;

    public Poder(String namePoder) {
        this.namePoder = namePoder;
        this.listMinisterio = new ArrayList<>();
        this.totalValueSpent = 0.0;
        this.percentageOfTotal = 0.0;
    }

}
