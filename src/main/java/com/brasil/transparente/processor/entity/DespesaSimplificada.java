package com.brasil.transparente.processor.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "despesa_simplificada")
public class DespesaSimplificada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long despesaSimplificadaId;
    private String name;
    private double totalValue;
    private double percentageOfTotal;

    public DespesaSimplificada(String name, double totalValue, double percentageOfTotal) {
        this.name = name;
        this.totalValue = totalValue;
        this.percentageOfTotal = percentageOfTotal;
    }

}
