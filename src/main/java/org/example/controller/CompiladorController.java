package org.example.controller;

import org.example.service.CompiladorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Controller
public class CompiladorController {

    @Autowired
    private CompiladorService compiladorService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/compilar")
    @ResponseBody
    public String compilar(@RequestParam String codigo) {
        return compiladorService.compilar(codigo);
    }

    @PostMapping("/compilarWeb")
    public String compilarWeb(@RequestParam String codigo, Model model) {
        String resultado = compiladorService.compilar(codigo);
        model.addAttribute("codigo", codigo);
        model.addAttribute("resultado", resultado);
        return "index";
    }

    @PostMapping("/compilarArchivo")
    public String compilarArchivo(@RequestParam("archivo") MultipartFile archivo, Model model) {
        try {
            // Leer el contenido del archivo
            String codigo = "";
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                codigo = content.toString();
            }

            // Analizar el código
            String resultado = compiladorService.compilar(codigo);
            model.addAttribute("codigo", codigo);
            model.addAttribute("resultado", resultado);
        } catch (IOException e) {
            model.addAttribute("resultado", "Error al leer el archivo: " + e.getMessage());
        }

        return "index";
    }
}