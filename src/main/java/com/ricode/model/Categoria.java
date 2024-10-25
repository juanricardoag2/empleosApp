package com.ricode.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="Categorias")
public class Categoria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nombre;

	@Column(length = 5000)
	private String descripcion;
}
