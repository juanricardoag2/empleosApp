package com.ricode.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Usuarios")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String username;
	private String nombre;
	private String email;
	private String password;
	private Integer estatus;
	private Date fechaRegistro;

	@ManyToMany(fetch = FetchType.EAGER) // Para que al consultar (GET O POST) un usuario, se obtengan también todos sus perfiles asociados
	@JoinTable(
			name = "UsuarioPerfil", //Tabla intermedia
			joinColumns = @JoinColumn(name = "idUsuario"), //1ra llave foránea (la conf se realiza
			inverseJoinColumns = @JoinColumn(name = "idPerfil") //2da llave foránea (tabla relacionada)
	)
	private List<Perfil> perfiles;

	 /*
	public Usuario(){
		this.estatus = 1;
		this.fechaRegistro = new Date();
		this.perfiles = setPerfiles(perfiles.add(new Perfil(3)));
	}
	 */

	public void agregarPerfil(Perfil perfil){
		perfiles = new LinkedList<>();
		perfiles.add(perfil);
	}
}
