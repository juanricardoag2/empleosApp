package com.ricode.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.ricode.model.Perfil;
import com.ricode.model.Usuario;
import com.ricode.service.ICategoriasService;
import com.ricode.service.IUsuariosService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.ricode.model.Vacante;
import com.ricode.service.IVacantesService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {
	
	@Autowired
	private IVacantesService serviceVacantes;

	@Autowired
	private IUsuariosService serviceUsuarios;

	@Autowired
	private ICategoriasService serviceCategorias;

	@Autowired
	private PasswordEncoder passwordEncoder;

	
	@GetMapping("/tabla")
	public String mostrarTabla(Model model) {
		List<Vacante> lista = serviceVacantes.buscarTodas();
		
		model.addAttribute("listaVacantes", lista);
		
		return "tabla";
	}

	@GetMapping("/")
	public String mostrarHome() {
		//List<Vacante> lista = serviceVacantes.buscarTodas();
		//model.addAttribute("listaVacantes", lista);
		return "home";
	}

	@GetMapping("/signup")
	public String registrarse(Usuario usuario){
		return "/usuarios/formRegistro";
	}

	@PostMapping("/signup")
	public String guardarRegistro(Usuario usuario, BindingResult result, RedirectAttributes attributes){
		if(result.hasErrors()){
			for(ObjectError error : result.getAllErrors()){
				System.out.println("Ocurrió un error: " + error.getDefaultMessage());
			}
			return "/usuarios/formRegistro";
		}

		serviceUsuarios.guardar(usuario);
		attributes.addFlashAttribute("msg", "El usuario '" + usuario.getUsername() + "' ha sido registrado exitosamente.");
		System.out.println("Usuario guardado:" + usuario);

		return "redirect:/usuarios/index";
	}

	@GetMapping("/search")
	//@ModelAttribute para rellenar el objeto Vacante con los valores del formulario (Data Binding)
	//"searchVacante" Para que el objeto Vacante que se envía en el formulario se vincule al objeto creado y agregado al modelo en el método "setGenericos"
	public String buscar(@ModelAttribute("searchVacante") Vacante vacante, Model model){
		System.out.println("Buscando por: " + vacante);

		//ExampleMatcher -> Query: Palabra reservada "LIKE" (Búsqueda por porción correspondiente a la descripción)
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withMatcher("descripcion", ExampleMatcher.GenericPropertyMatchers.contains());

		//Objeto de muestra: vacante
		//Example SI NO INCLUYERA EL MATCHER -> Query: "=" (Búsqueda por descripción completa tal cual / imposible)
		Example<Vacante> example = Example.of(vacante, matcher);

		//Búsqueda usando Example
		List<Vacante> listaVacantesSearch = serviceVacantes.buscarByExample(example);

		//Actualiza la lista que se muestra por default en la sección "DESTACADOS"
		model.addAttribute("listaVacantes", listaVacantesSearch);

		return "home";
	}

	//Endpoint "/indexAuth" está asociado a Spring Security para redireccionar al usuario después de inicio de sesión exitoso
	//Método que verifica que exista una sesión activa, si no existe la crea
	@GetMapping("/indexAuth")
	public String mostrarIndex(Authentication auth, HttpSession session){
		String username = auth.getName();
		System.out.println("Usuario logeado: " + username);

		for(GrantedAuthority rol : auth.getAuthorities()){
			System.out.println("ROL: " + rol.getAuthority());
		}

		//Validando que NO exista una sesión activa con el nombre "usuarioSession"
		if(session.getAttribute("usuarioSession") == null) {
			Usuario usuario = serviceUsuarios.buscarPorUsername(username);

			usuario.setPassword(null); //Para que no se almacene la contraseña en la sesión del usuario
			System.out.println("Usuario: " + usuario);

			//Creando la sesión "usuarioSession" y agregando al Usuario como atributo de sesión
			session.setAttribute("usuarioSession", usuario);
		}

		//No redireccionar si se desea utilizar el método ".defaultSuccessUrl()" en la clase DatabaseWebSecurity
		//return "home";
		return "redirect:/";
	}

	@GetMapping("/login")
	public String mostrarLogin(HttpServletRequest request, Model model){
		//Obtener el mensaje de error, si existe
		String errorMsg = (String) request.getSession().getAttribute("error");

		//Si existe, agregarlo al modelo y luego eliminarlo de la sesión
		if (errorMsg != null) {
			model.addAttribute("error", errorMsg);
			request.getSession().removeAttribute("error");
		}

		return "formLogin";
	}

	@GetMapping("/logout")
	public String cerrarSesion(HttpServletRequest request){ //HttpServletRequest para que Spring obtenga la sesión HTTP actual
		//Implementación de SpringSecurity encargada de destruir la sesión
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		//Solo el primer parámetro (request) es utilizado por este método, los otros 2 pueden ser nulos
		logoutHandler.logout(request, null, null);

		return "redirect:/";
	}

	//Utilería para encriptar texto plano
	@GetMapping("/bcrypt/{pwdPlano}")
	@ResponseBody//Para renderizar el texto (resultado del método) en el navegador
	public String encriptarPwd(@PathVariable("pwdPlano") String pwd){
		return pwd + " encriptada con BCrypt: " + passwordEncoder.encode(pwd);
	}

	//En Data Binding: Settear el input "descripcion" o cualquier input de tipo String como null si llega vacío
	@InitBinder
	public void initBinder(WebDataBinder binder){
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
	
	@ModelAttribute //Agregar atributos al modelo para todo el controlador (otra manera de agregar atributos al modelo - Data Binding)
	public void setGenericos(Model model) {
		model.addAttribute("listaVacantes", serviceVacantes.buscarDestacadasAprobadas()); //Será actualizada en la vista al realizar una búsqueda
		model.addAttribute("listaCategorias", serviceCategorias.buscarTodas());

		Vacante vacante = new Vacante();

		//Hacer "null" el valor del atributo "imagen" del objeto Vacante, para que dicha propiedad no incluya su valor por defecto en la búsqueda (revisar el atributo en el modelo)
		vacante.resetDefaultImageValue();

		model.addAttribute("searchVacante", vacante);
	}
}
