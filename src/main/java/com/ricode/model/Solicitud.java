package com.ricode.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "Solicitudes")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate fecha; //Fecha en la que el usuario aplica a esta oferta
    private String comentarios;
    private String archivo; //El nombre del archivo PDF, DOCX del CV

    @OneToOne
    @JoinColumn(name = "idVacante")
    private Vacante vacante;

    @OneToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;

    public Solicitud(){
        this.fecha = LocalDate.now();
    }
}
