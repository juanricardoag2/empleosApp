package com.ricode.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ricode.model.Vacante;
import com.ricode.service.ICategoriasService;
import com.ricode.service.IVacantesService;
import com.ricode.util.Utileria;

@Controller
@RequestMapping("/vacantes")
//@RequiredArgsConstructor
public class VacantesController {
	
	@Value("${empleosapp.ruta.imagenes}")
	private String ruta;
	
	@Autowired
	private IVacantesService serviceVacantes;
	
	@Autowired
	@Qualifier("categoriasServiceJpa")
	private ICategoriasService serviceCategorias;

	@GetMapping("/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page){
		Page<Vacante> lista = serviceVacantes.buscarTodas(page);
		model.addAttribute("listaVacantes", lista);

		return "vacantes/listVacantes";
	}
	
	@GetMapping("/index")
	public String mostrarIndex(Model model) {
		List<Vacante> lista = serviceVacantes.buscarTodas();
		model.addAttribute("listaVacantes", lista);
		
		return "vacantes/listVacantes";
	}
	
	@GetMapping("/create")
	public String crear(Vacante vacante, Model model) { //Data Binding
		return "vacantes/formVacante";
	}
	
	//Aplicando Data Binding con manejo de errores, redireccionamiento y flash attributes para almacenar atributos temporalmente en la sesion hasta despues del redirect
	//Se añade multipart para gestionar archivos (imagen)
	@PostMapping("/save")
	public String guardar(Vacante vacante, BindingResult result, RedirectAttributes attributes, @RequestParam("archivoImagen") MultipartFile multiPart) {
		if(result.hasErrors()) {
			for(ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			
			return "vacantes/formVacante";
		}
		
		if(!multiPart.isEmpty()) {
			//String ruta = "c:/empleos/img-vacantes/";
			String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			if(nombreImagen != null) { //Se subió la imagen
				//Se procesa la variable nombreImagen
				vacante.setImagen(nombreImagen);
			}
		}
		
		serviceVacantes.guardar(vacante);
		attributes.addFlashAttribute("msg", "Registro Guardado correctamente."); //Flash attributes
		System.out.println("Vacante: " + vacante);
		return "redirect:/vacantes/index"; //Patrón Post/Redirect/Get
	}
	
	/*
	 //Sin "Data Binding"
	@PostMapping("/save")
	public String guardar(@RequestParam("nombreParam") String nom, @RequestParam("descripcionParam") String desc,
			@RequestParam("estatusParam") String estatus, @RequestParam("fechaParam") String fecha, @RequestParam("destacadoParam") int destacado,
			@RequestParam("salarioParam") double salario, @RequestParam("detallesParam") String detalles) {
		System.out.println("Nombre vacante: " + nom);
		System.out.println("Descripcion: " + desc);
		System.out.println("Estatus: " + estatus);
		System.out.println("Fecha Publicación: " + fecha);
		System.out.println("Destacado: " + destacado);
		System.out.println("Salario ofrecido: " + salario);
		System.out.println("Detalles: " + detalles);
		return "vacantes/listVacantes";
	}
	*/
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") Integer idVacante, RedirectAttributes attributes) {
		System.out.println("Borrando vacante con id: " + idVacante);
		serviceVacantes.eliminar(idVacante);
		attributes.addFlashAttribute("msg","La vacante fue eliminada correctamente.");
		return "redirect:/vacantes/index";
	}
	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") Integer idVacante, Model model) {
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		model.addAttribute("vacante", vacante);
		
		return "vacantes/formVacante";
	}
	
	@GetMapping("/view/{id}")
	public String verDetalle(@PathVariable("id") Integer idVacante, Model model) {
		
		Vacante vacante = serviceVacantes.buscarPorId(idVacante);
		
		System.out.println("Vacante: " + vacante);
		model.addAttribute("vacante", vacante);
		
		//Buscar los detalles de la vacante en la BD...
		
		return "detalle";
	}
	
	//Metodo para agregar datos comunes al modelo
	@ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("listaCategorias", serviceCategorias.buscarTodas());
	}
	
	//
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
}
