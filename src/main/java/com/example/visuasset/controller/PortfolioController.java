package com.example.visuasset.controller;

import com.example.visuasset.dto.PortfolioData;
import com.example.visuasset.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Year;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public String portfolio(@RequestParam(name = "year", required = false) Integer year,
                           Model model) {
        
        // 年の妥当性チェック
        int targetYear = portfolioService.validateYear(year);
        int currentYear = Year.now().getValue();
        
        // ポートフォリオデータを取得
        PortfolioData portfolioData = portfolioService.getPortfolioData(targetYear);
        
        // 前年・翌年ボタンの有効/無効判定
        boolean canGoPrevious = targetYear > 1900;
        boolean canGoNext = targetYear < currentYear;
        
        model.addAttribute("targetYear", targetYear);
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("canGoPrevious", canGoPrevious);
        model.addAttribute("canGoNext", canGoNext);
        
        if (portfolioData != null) {
            model.addAttribute("portfolioData", portfolioData);
            model.addAttribute("amounts", portfolioData.getAmounts());
            model.addAttribute("percentages", portfolioData.getPercentages());
            model.addAttribute("labels", portfolioData.getLabels());
            model.addAttribute("totalAssets", portfolioData.getTotalAssets());
            model.addAttribute("hasData", true);
        } else {
            model.addAttribute("hasData", false);
        }
        
        return "portfolio";
    }
}
