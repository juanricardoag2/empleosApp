package com.ricode.service.db;

import com.ricode.model.Perfil;
import com.ricode.model.Usuario;
import com.ricode.repository.UsuariosRepository;
import com.ricode.service.IUsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UsuariosServiceJpa implements IUsuariosService {

    @Autowired
    private UsuariosRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void guardar(Usuario usuario) {
        Perfil perfil = new Perfil();
        perfil.setId(3); //Perfil "USUARIO"

        usuario.agregarPerfil(perfil);

        usuario.setEstatus(1); //Activado por defecto
        usuario.setFechaRegistro(new Date()); //Fecha actual del servidor

        String pwdPlano = usuario.getPassword();
        String pwdEncriptada = passwordEncoder.encode(pwdPlano);
        usuario.setPassword(pwdEncriptada);

        repository.save(usuario);
    }

    @Override
    public void eliminar(Integer idUsuario) {
        repository.deleteById(idUsuario);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return repository.findAll();
    }

    @Override
    public Page<Usuario> buscarTodos(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        return repository.findByUsername(username);
    }
/*
    @Override
    public List<Perfil> getPerfilByUsername(String username) {
        return repository.getPerfilByUsername(username);
    }
*/
}
