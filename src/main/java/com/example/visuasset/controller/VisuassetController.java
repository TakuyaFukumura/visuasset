package com.example.visuasset.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.visuasset.service.VisuassetService;

@Controller
@RequestMapping("/") // URLとの関連付け http://localhost:8080/ の時に呼ばれる
public class VisuassetController {

    private final VisuassetService service;

    @Autowired
    public VisuassetController(VisuassetService service) {
        this.service = service;
    }

    @GetMapping // Getされた時の処理 Postは別
    public String index(Model model) {
        model.addAttribute("message", service.getMessage());
        return "visuasset";
    }
}
