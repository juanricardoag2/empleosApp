package com.ricode.service;

import java.util.List;

import com.ricode.model.Vacante;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVacantesService {
	List<Vacante> buscarTodas();
	
	Vacante buscarPorId(Integer idVacante);
	
	void guardar(Vacante vacante);
	
	List<Vacante> buscarDestacadasAprobadas();
	
	void eliminar(Integer idVacante);

	//Query ByExample
	//Los filtros que irán en el "where" de la query se formarán dependiendo de los valores de los atributos que NO lleguen como nulos
	List<Vacante> buscarByExample(Example<Vacante> example);

	Page<Vacante> buscarTodas(Pageable page);
}
