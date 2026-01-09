package com.brasil.transparente.processor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@Entity
@Table(name = "repasse")
@NoArgsConstructor
@AllArgsConstructor
public class Repasse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "repasse_id")
    private Long id;

    @Column(name = "ano")
    private String ano;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

}
