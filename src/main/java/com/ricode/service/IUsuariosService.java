package com.ricode.service;

import com.ricode.model.Perfil;
import com.ricode.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUsuariosService {
    void guardar(Usuario usuario);
    void eliminar(Integer idUsuario);
    List<Usuario> buscarTodos();

    Page<Usuario> buscarTodos(Pageable page);

    Usuario buscarPorUsername(String username);

    //List<Perfil> getPerfilByUsername(String username);
}
