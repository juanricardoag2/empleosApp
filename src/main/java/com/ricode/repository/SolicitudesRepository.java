package com.ricode.repository;

import com.ricode.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudesRepository extends JpaRepository<Solicitud, Integer> {
}
