package com.brasil.transparente.processor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "renuncia_fiscal")
public class RenunciaFiscal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long renunciaFiscalId;

    private String ano;
    private String cnpj;
    private String beneficiario;
    private double valorRenunciado;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "unidade_federativa_id", referencedColumnName = "unidadeFederativaId")
    private UnidadeFederativa unidadeFederativa;

    public RenunciaFiscal() {
    }

    public RenunciaFiscal(String ano, String cnpj, String beneficiario, double valorRenunciado) {
        this.ano = ano;
        this.cnpj = cnpj;
        this.beneficiario = beneficiario;
        this.valorRenunciado = valorRenunciado;
    }
}