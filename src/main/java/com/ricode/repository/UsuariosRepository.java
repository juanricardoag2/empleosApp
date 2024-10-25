package com.ricode.repository;

import com.ricode.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ricode.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer>{
    Usuario findByUsername(String username);

    @Query("SELECT u.perfiles FROM Usuario u WHERE u.username = :username")
    List<Perfil> getPerfilByUsername(@Param("username") String username);

    /*
    @Query(value = "select u.username, p.perfil " +
            "from usuarioperfil up " +
            "inner join usuarios u on up.idUsuario = u.id " +
            "inner join perfiles p on up.idPerfil = p.id", nativeQuery = true)
    List<Perfil> getPerfilesByUsername(String username);
    */
}
