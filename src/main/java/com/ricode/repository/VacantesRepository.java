package com.ricode.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricode.model.Vacante;
import java.util.List;


public interface VacantesRepository extends JpaRepository<Vacante, Integer> {
	List<Vacante> findByEstatus(String estatus);
	
	List<Vacante> findByDestacadoAndEstatusOrderByIdDesc(int destacado, String estatus);
	
	List<Vacante> findBySalarioBetweenOrderBySalarioDesc(double s1, double s2);
	
	List<Vacante> findByEstatusIn(String[] estatus); //Buscar por más de un estatus
}
