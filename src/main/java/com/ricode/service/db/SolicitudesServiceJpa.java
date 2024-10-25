package com.ricode.service.db;

import com.ricode.model.Solicitud;
import com.ricode.repository.SolicitudesRepository;
import com.ricode.service.ISolicitudesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudesServiceJpa implements ISolicitudesService {

    private SolicitudesRepository repository;

    @Override
    public void guardar(Solicitud solicitud) {
        repository.save(solicitud);
    }

    @Override
    public void eliminar(Integer idSolicitud) {
        repository.deleteById(idSolicitud);
    }

    @Override
    public List<Solicitud> buscarTodas() {
        return repository.findAll();
    }

    @Override
    public Solicitud buscarPorId(Integer idSolicitud) {
        Optional<Solicitud> optional = repository.findById(idSolicitud);
        return optional.orElse(null);
    }

    @Override
    public Page<Solicitud> buscarTodas(Pageable page) {
        return repository.findAll(page);
    }
}
