package com.ricode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${empleosapp.ruta.imagenes}")
	private String rutaImagenes;
	
	//Método para mapear el directorio físico a la ruta "/logos/.."
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry.addResourceHandler("/logos/**").addResourceLocations("file:c:/empleos/img-vacantes/");
		registry.addResourceHandler("/logos/**").addResourceLocations("file:"+rutaImagenes);
	}
}
