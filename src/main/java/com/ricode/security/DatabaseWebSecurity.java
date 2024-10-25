package com.ricode.security;

import com.ricode.exception.security.CustomAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {
    @Bean
    UserDetailsManager users(DataSource dataSource){ //Ya existe un DataSource configurado en application.properties (MySQL)
        //Para cargar la información del usuario y sus roles desde la BD
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        /* Utilizar tablas personalizadas (NO por defecto SpringSecurity) */
        //Cómo recuperar la información de un usuario específico
        users.setUsersByUsernameQuery("select username,password,estatus from Usuarios u where username=?");
        //Cómo recuperar las autoridades (roles) asociadas a un usuario
        //Uniendo las tablas UsuarioPerfil, Usuarios y Perfiles para obtener el rol (perfil) de cada usuario
        users.setAuthoritiesByUsernameQuery("select u.username, p.perfil from UsuarioPerfil up " +
                                            "inner join Usuarios u on u.id = up.idUsuario " +
                                            "inner join Perfiles p on p.id = up.idPerfil " +
                                            "where u.username=?");

        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                        //Los recursos estáticos no requieren autenticación
                        .requestMatchers("/bootstrap/**", "/images/**", "/tinymce/**", "/logos/**").permitAll()
                        //Las vistas públicas no requieren autenticación
                        .requestMatchers("/", "/signup", "/search", "/vacantes/view/**", "/bcrypt/**").permitAll()

                        //Asignar permisos a URLs por ROLES
                        .requestMatchers("/vacantes/**").hasAnyAuthority("SUPERVISOR", "ADMINISTRADOR")
                        .requestMatchers("/categorias/**").hasAnyAuthority("SUPERVISOR", "ADMINISTRADOR")
                        .requestMatchers("/usuarios/**").hasAnyAuthority("ADMINISTRADOR")

                        //Todas las demás URLs de la aplicación requieren autenticación
                        .anyRequest().authenticated());

        //El formulario de Login no requiere autenticación
        httpSecurity.formLogin(form -> form.loginPage("/login")
                .failureHandler(new CustomAuthenticationFailureHandler()) //Manejador de error de autenticación con Spring Security
                //.defaultSuccessUrl("/vacantes/indexPaginate") //Cruce con la redirección en el método "mostrarIndex" de HomeController
                .permitAll());

        return httpSecurity.build();
    }

    //SpringBoot al detectar este Bean, asume por defecto
    //que las contraseñas se enviarán desde la BD encriptadas con el algoritmo BCrypt.
    //Si no se encuentran encriptadas no serán válidas
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
