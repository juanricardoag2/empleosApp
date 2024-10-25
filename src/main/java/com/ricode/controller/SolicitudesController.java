package com.ricode.controller;

import com.ricode.model.Solicitud;
import com.ricode.model.Vacante;
import com.ricode.service.IVacantesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudesController {

    @Autowired
    private IVacantesService serviceVacantes;

    @GetMapping("/create/{idVacante}")
    public String crear(Solicitud solicitud, @PathVariable("idVacante") Integer idVacante, Model model){
        System.out.println("idVacante: " + idVacante);

        Vacante vacante = serviceVacantes.buscarPorId(idVacante);
        model.addAttribute("vacante", vacante);

        return "solicitudes/formSolicitud";
    }
}
