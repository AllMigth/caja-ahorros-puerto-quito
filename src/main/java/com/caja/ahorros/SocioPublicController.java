package com.caja.ahorros;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SocioPublicController {
    
    @Autowired
    private SocioPublicService socioPublicService;
    
    @GetMapping("/s/{cedula}")
    public String verEstadoSocio(@PathVariable String cedula, Model model) {
        try {
            EstadoSocioDTO estado = socioPublicService.obtenerEstadoPorCedula(cedula);
            model.addAttribute("estado", estado);
            return "socio-estado";
        } catch (Exception e) {
            return "socio-no-encontrado";
        }
    }
}
