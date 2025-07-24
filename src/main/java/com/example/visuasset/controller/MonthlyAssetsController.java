package com.example.visuasset.controller;

import com.example.visuasset.entity.MonthlyAssets;
import com.example.visuasset.service.MonthlyAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

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

    // 月別資産データ一覧画面（年で絞り込み）
    @GetMapping("monthly/list")
    public String monthlyAssetsList(@RequestParam(name = "targetYear", required = false) Integer targetYear, Model model) {
        int currentYear = Year.now().getValue();
        if (targetYear == null) {
            targetYear = currentYear;
        }
        // 未来年にならないようにバリデーション
        if (targetYear > currentYear) {
            targetYear = currentYear;
        }
        
        List<MonthlyAssets> list = monthlyAssetsService.getAssetsByYear(targetYear);
        model.addAttribute("monthlyAssetsList", list);
        model.addAttribute("targetYear", targetYear);
        return "monthly_list";
    }

    // 月別資産データ新規登録画面
    @GetMapping("monthly/new")
    public String showCreateForm(@RequestParam(name = "targetYear", required = false) Integer targetYear, Model model) {
        MonthlyAssets monthlyAssets = new MonthlyAssets();
        
        // デフォルトの年を設定
        int currentYear = LocalDate.now().getYear();
        if (targetYear == null) {
            targetYear = currentYear;
        }
        if (targetYear > currentYear) {
            targetYear = currentYear;
        }
        
        monthlyAssets.setTargetYear(targetYear);
        model.addAttribute("monthlyAssets", monthlyAssets);
        model.addAttribute("targetYear", targetYear);
        return "monthly_form";
    }

    // 月別資産データ新規登録処理
    @PostMapping("monthly/insert")
    public String createMonthlyAssets(@ModelAttribute MonthlyAssets monthlyAssets) {
        monthlyAssetsService.saveMonthlyAssets(monthlyAssets);
        return "redirect:/monthly/list?targetYear=" + monthlyAssets.getTargetYear();
    }

    // 月別資産データ編集画面
    @GetMapping("monthly/edit/{year}/{month}")
    public String showEditForm(@PathVariable("year") int year, @PathVariable("month") int month, Model model) {
        MonthlyAssets monthlyAssets = monthlyAssetsService.getAssetsByYearAndMonth(year, month)
                .orElse(new MonthlyAssets());
        model.addAttribute("monthlyAssets", monthlyAssets);
        model.addAttribute("targetYear", year);
        return "monthly_form";
    }

    // 月別資産データ更新処理
    @PostMapping("monthly/update")
    public String updateMonthlyAssets(@ModelAttribute MonthlyAssets monthlyAssets) {
        monthlyAssetsService.saveMonthlyAssets(monthlyAssets);
        return "redirect:/monthly/list?targetYear=" + monthlyAssets.getTargetYear();
    }

    // 月別資産データ削除
    @PostMapping("monthly/delete/{year}/{month}")
    public String deleteMonthlyAssets(@PathVariable("year") int year, @PathVariable("month") int month) {
        monthlyAssetsService.deleteMonthlyAssets(year, month);
        return "redirect:/monthly/list?targetYear=" + year;
    }
}
