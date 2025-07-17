package com.example.visuasset.controller;

import com.example.visuasset.service.MonthlyAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MonthlyAssetsController {

    private final MonthlyAssetsService monthlyAssetsService;

    @Autowired
    public MonthlyAssetsController(MonthlyAssetsService monthlyAssetsService) {
        this.monthlyAssetsService = monthlyAssetsService;
    }

    @GetMapping("monthly")
    public String monthly(@RequestParam(name = "targetYear", required = false) Integer targetYear, Model model) {
        int currentYear = java.time.LocalDate.now().getYear();
        if (targetYear == null) {
            targetYear = currentYear;
        }
        // 未来年にならないようにバリデーション
        if (targetYear > currentYear) {
            targetYear = currentYear;
        }
        model.addAttribute("targetYear", targetYear);
        // 月別資産データ取得
        var monthlyAssetsList = monthlyAssetsService.getAssetsByYear(targetYear);
        model.addAttribute("cashList", monthlyAssetsService.getCashList(monthlyAssetsList));
        model.addAttribute("securitiesList", monthlyAssetsService.getSecuritiesList(monthlyAssetsList));
        model.addAttribute("cryptoList", monthlyAssetsService.getCryptoList(monthlyAssetsList));
        // ラベル（データがある月のみ）をサーバー側で生成
        model.addAttribute("labels", monthlyAssetsService.getMonthLabels(monthlyAssetsList));
        return "monthly";
    }
}
