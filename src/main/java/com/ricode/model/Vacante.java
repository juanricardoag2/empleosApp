package com.ricode.model;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="Vacantes")
public class Vacante {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nombre;
	private String descripcion;
	private Date fecha;
	private Double salario;
	private Integer destacado;
	private String imagen="no-image.png";
	private String estatus;

	@Column(length = 5000)
	private String detalles;
	
	//@Transient
	@ManyToOne
	@JoinColumn(name = "idCategoria")
	private Categoria categoria;

	public void resetDefaultImageValue(){
		this.imagen = null;
	}
}
