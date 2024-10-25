package com.ricode.controller;

import com.ricode.model.Usuario;
import com.ricode.service.IUsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    private IUsuariosService serviceUsuarios;

    @GetMapping("/indexPaginate")
    public String mostrarIndexPaginado(Model model, Pageable page){
        Page<Usuario> lista = serviceUsuarios.buscarTodos(page);
        model.addAttribute("listaUsuarios", lista);

        return "usuarios/listUsuarios";
    }

    @GetMapping("/index")
    public String mostrarIndex(Model model){
        List<Usuario> lista = serviceUsuarios.buscarTodos();
        model.addAttribute("listaUsuarios", lista);

        return "usuarios/listUsuarios";
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable("id") int idUsuario, RedirectAttributes attributes){
        serviceUsuarios.eliminar(idUsuario);
        attributes.addFlashAttribute("msg","El usuario fue eliminado correctamente.");

        return "redirect:/usuarios/index";
    }
}
