package org.example.controller;

import org.example.service.CompiladorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
}