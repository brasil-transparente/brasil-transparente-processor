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
@Table(name = "ministerio")
public class Ministerio implements UnidadeOrcamentaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ministerioId;

    private String nameMinisterio;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "poder_id", referencedColumnName = "poderId")
    private Poder poder;

    @OneToMany(mappedBy = "ministerio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Orgao> listOrgao;

    private double totalValueSpent;

    private double percentageOfTotal;

    public Ministerio(String nameMinisterio) {
        this.nameMinisterio = nameMinisterio;
        this.listOrgao = new ArrayList<>();
        this.totalValueSpent = 0.0;
        this.percentageOfTotal = 0.0;
    }

}
