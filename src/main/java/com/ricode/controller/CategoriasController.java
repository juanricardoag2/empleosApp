package com.ricode.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ricode.model.Categoria;
import com.ricode.service.ICategoriasService;

@Controller
@RequestMapping(value="/categorias")
public class CategoriasController {
	
	@Autowired
	@Qualifier("categoriasServiceJpa")
	private ICategoriasService serviceCategorias;

	@RequestMapping(value = "/indexPaginate", method = RequestMethod.GET)
	public String mostrarIndexPaginado(Model model, Pageable page){
		Page<Categoria> lista = serviceCategorias.buscarTodas(page);
		model.addAttribute("listaCategorias", lista);
		return "categorias/listCategorias";
	}
	
	// @GetMapping("/index")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String mostrarIndex(Model model) {
		List<Categoria> lista = serviceCategorias.buscarTodas();
		model.addAttribute("listaCategorias", lista);
		return "categorias/listCategorias";
	}

	// @GetMapping("/create")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String crear(Categoria categoria) { //Data Binding
		return "categorias/formCategoria";
	}

	// @PostMapping("/save")
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String guardar(Categoria categoria, BindingResult result, RedirectAttributes attributes) {
		
		if(result.hasErrors()) {
			for(ObjectError error : result.getAllErrors()) {
				System.out.println("Ocurrio un error: " + error.getDefaultMessage());
			}
			
			return "categorias/formCategoria";
		}
		
		serviceCategorias.guardar(categoria);
		attributes.addFlashAttribute("msg", "Categoría guardada correctamente."); //Flash attributes
		System.out.println("Categoria: " + categoria);
		return "redirect:/categorias/index"; //Patrón Post/Redirect/Get
	}

	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") Integer id, Model model){
		Categoria cat = serviceCategorias.buscarPorId(id);
		model.addAttribute("categoria", cat);

		return "categorias/formCategoria";
	}

	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") Integer id, RedirectAttributes attributes){
		Categoria cat = serviceCategorias.buscarPorId(id);
		System.out.println("Borrando categoría con id: " + id);
		serviceCategorias.eliminar(id);
		attributes.addFlashAttribute("msg", "La categoría '" + cat.getNombre() + "' ha sido eliminada correctamente.");

		return "redirect:/categorias/index";
	}
}
