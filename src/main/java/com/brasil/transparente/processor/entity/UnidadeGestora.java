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
@Table(name = "unidade_gestora")
public class UnidadeGestora implements UnidadeOrcamentaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unidadeGestoraId;

    private String nameUnidadeGestora;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "orgao_id", referencedColumnName = "orgaoId")
    private Orgao orgao;

    @JsonManagedReference
    @OneToMany(mappedBy = "unidadeGestora", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElementoDespesa> listElementoDespesa;

    private double totalValueSpent;

    private double percentageOfTotal;

    public UnidadeGestora(String nameUnidadeGestora) {
        this.nameUnidadeGestora = nameUnidadeGestora;
        this.listElementoDespesa = new ArrayList<>();
        this.totalValueSpent = 0.0;
    }
}
