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
@Table(name = "orgao")
public class Orgao implements UnidadeOrcamentaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orgaoId;

    private String nameOrgao;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "ministerio_id", referencedColumnName = "ministerioId")
    private Ministerio ministerio;

    @JsonManagedReference
    @OneToMany(mappedBy = "orgao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnidadeGestora> listUnidadeGestora;

    private double totalValueSpent;

    private double percentageOfTotal;

    public Orgao(String nameOrgao) {
        this.nameOrgao = nameOrgao;
        this.listUnidadeGestora = new ArrayList<>();
        this.totalValueSpent = 0.0;
    }

}
