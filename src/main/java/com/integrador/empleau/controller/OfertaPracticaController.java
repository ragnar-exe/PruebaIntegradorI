package com.integrador.empleau.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import com.integrador.empleau.service.OfertaPracticaService;

import java.util.List;

import com.integrador.empleau.model.OfertaPractica;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/ofertas")
public class OfertaPracticaController {

    private final OfertaPracticaService ofertaService;

    public OfertaPracticaController(OfertaPracticaService ofertaService) {
        this.ofertaService = ofertaService;
    }

    @GetMapping("/registroOferta")
    public String listarOfertasPorEmpresa(Model model) {
        List<OfertaPractica> ofertas = ofertaService.listarOfertasPorEmpresaId(1); // Cambia 1 por el ID de la empresa correspondiente
        model.addAttribute("ofertas", ofertas);
        return "ofertasEmpresa"; // nombre de tu vista HTML
    }

    @PostMapping("/registrarOferta")
    public String registrarOferta(@RequestBody OfertaPractica oferta) {
        ofertaService.crearOfertaPractica(oferta);
        return "redirect:/ofertas/registroOferta"; // Redirige a la lista de ofertas después de registrar
    }
    

}
