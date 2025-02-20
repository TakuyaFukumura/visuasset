package com.example.visuasset.controller;

import com.example.visuasset.entity.AnnualAssets;
import com.example.visuasset.service.VisuassetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
        return "redirect:/yearly";
    }

    @GetMapping("yearly")
    public String yearly(Model model) {
        AnnualAssets annualAssets = service.getAssetsByYear(2024)
                .orElseThrow(() -> new RuntimeException("データが見つかりません"));
        model.addAttribute("annualAssets", annualAssets);
        List<AnnualAssets> annualAssetsList = service.getAssetsBetweenYears(2020, 2025);

        String cashList = service.getCashListAsString(annualAssetsList);
        String securitiesList = service.getSecuritiesListAsString(annualAssetsList);

        model.addAttribute("cashList", cashList);
        model.addAttribute("securitiesList", securitiesList);
//        model.addAttribute("cryptoList", cryptoList);
        return "yearly";
    }

    @GetMapping("monthly")
    public String monthly(Model model) {
        model.addAttribute("message", service.getMessage());
        return "monthly";
    }
}
