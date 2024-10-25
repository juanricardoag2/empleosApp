package com.ricode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricode.model.Categoria;

public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {

}
