package com.integrador.empleau.controller;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.integrador.empleau.model.Alumno;
import com.integrador.empleau.model.Usuario;
import com.integrador.empleau.repository.AlumnoRepository;
import com.integrador.empleau.repository.UsuarioRepository;
import com.integrador.empleau.service.AlumnoService;
import com.integrador.empleau.service.UsuarioServiceImpl;
import org.springframework.ui.Model;

import org.springframework.security.core.Authentication;


@Controller
@RequestMapping("/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;
    private final AlumnoRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;

    public AlumnoController(AlumnoService alumnoService, AlumnoRepository alumnoRepository,
            UsuarioRepository usuarioRepository) {
        this.alumnoService = alumnoService;
        this.alumnoRepository = alumnoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Mostrar formulario de registro
    @GetMapping("/registroAlumno")
    public String mostrarFormularioRegistroAlumno(Model model) {
        Alumno alumno = new Alumno();
        alumno.setUsuario(new Usuario()); // Necesario para evitar null en el formulario
        model.addAttribute("alumno", alumno);
        return "registroAlumno";
    }

    // Procesar el registro
    @PostMapping("/registrarAlumno")
    public String procesarRegistroAlumno(@ModelAttribute("alumno") Alumno alumno,
            RedirectAttributes redirectAttributes) {
        try {
            alumnoService.registrarAlumno(alumno);
            redirectAttributes.addFlashAttribute("success", "Alumno registrado exitosamente");
            return "redirect:/login"; // Redirige al login después del registro
        } catch (UsuarioServiceImpl.EmailAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("error", "Correo ya está registrado");
            return "redirect:/alumnos/registroAlumno";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error interno: " + e.getMessage());
            return "redirect:/alumnos/registroAlumno";
        }
    }

    // Mostrar perfil del alumno
    @GetMapping("/perfilAlumno")
    public String verPerfilAlumno(Authentication authentication, Model model) {
        String email = authentication.getName(); // Email del usuario logueado
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Alumno alumno = alumnoRepository.findByUsuario(usuario);

        model.addAttribute("alumno", alumno);
        model.addAttribute("usuario", usuario);
        return "perfilAlumno";
    }

    // Método para actualizar el perfil del alumno 
    @PostMapping("/actualizarPerfil")
    public String actualizarPerfilAlumno(@ModelAttribute("alumno") Alumno alumnoForm,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            Alumno alumnoExistente = alumnoService.obtenerPorEmail(email);
            // Mantener ID y otros campos no visibles del form
            alumnoForm.setAlumnoId(alumnoExistente.getAlumnoId());
            if (alumnoForm.getUsuario() != null) {
                alumnoForm.getUsuario().setUsuarioId(alumnoExistente.getUsuario().getUsuarioId());
            }
            alumnoService.actualizarAlumno(alumnoForm);
            redirectAttributes.addFlashAttribute("success", "Perfil actualizado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el perfil: " + e.getMessage());
        }
        return "redirect:/alumnos/perfilAlumno";
    }


}
