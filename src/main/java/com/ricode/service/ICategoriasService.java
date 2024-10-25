package com.ricode.service;

import java.util.List;

import com.ricode.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoriasService {
	void guardar(Categoria categoria);
	List<Categoria> buscarTodas();
	Categoria buscarPorId(Integer idCategoria);
	void eliminar(Integer idCategoria);

	Page<Categoria> buscarTodas(Pageable page);
}
