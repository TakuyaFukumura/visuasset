package com.example.visuasset.controller;

import com.example.visuasset.entity.AnnualAssets;
import com.example.visuasset.service.MonthlyAssetsService;
import com.example.visuasset.service.VisuassetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/") // URLとの関連付け http://localhost:8080/ の時に呼ばれる
public class VisuassetController {

    private final VisuassetService service;
    private final MonthlyAssetsService monthlyAssetsService;

    @Autowired
    public VisuassetController(VisuassetService service, MonthlyAssetsService monthlyAssetsService) {
        this.service = service;
        this.monthlyAssetsService = monthlyAssetsService;
    }

    @GetMapping // Getされた時の処理 Postは別
    public String index(Model model) {
        return "redirect:/yearly";
    }

    @GetMapping("yearly")
    public String yearly(@RequestParam(name = "startYear", required = false) Integer startYear,
                        @RequestParam(name = "endYear", required = false) Integer endYear,
                        Model model) {
        if (startYear == null) {
            startYear = 2020; // デフォルト値
        }
        if (endYear == null) {
            endYear = 2025; // デフォルト値
        }
        int currentYear = java.time.LocalDate.now().getYear();
        // startYearが現在の年を超えないようにバリデーション
        if (startYear > currentYear) {
            startYear = currentYear;
        }
        if (endYear < startYear) {
            endYear = startYear;
        }
        // endYearが現在の年を超えないようにバリデーション
        if (endYear > currentYear) {
            endYear = currentYear;
        }

        List<AnnualAssets> annualAssetsList = service.getAssetsBetweenYears(startYear, endYear);

        model.addAttribute("startYear", startYear );
        model.addAttribute("endYear", endYear );

        String cashList = service.getCashListAsString(annualAssetsList);
        String securitiesList = service.getSecuritiesListAsString(annualAssetsList);
        String cryptoList = service.getCryptoListAsString(annualAssetsList);

        model.addAttribute("cashList", cashList);
        model.addAttribute("securitiesList", securitiesList);
        model.addAttribute("cryptoList", cryptoList);
        return "yearly";
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

    // 年別資産データ一覧画面
    @GetMapping("annualAssets")
    public String annualAssetsList(Model model) {
        List<AnnualAssets> list = service.getAllAnnualAssets();
        model.addAttribute("annualAssetsList", list);
        return "annual_assets";
    }

    // 年別資産データ新規登録画面
    @GetMapping("annualAssets/new")
    public String showCreateForm(Model model) {
        model.addAttribute("annualAssets", new AnnualAssets());
        return "annual_assets_form";
    }

    // 年別資産データ新規登録処理
    @PostMapping("annualAssets/insert")
    public String createAnnualAssets(@ModelAttribute AnnualAssets annualAssets) {
        service.saveAnnualAssets(annualAssets);
        return "redirect:/annualAssets";
    }

    // 年別資産データ編集画面
    @GetMapping("annualAssets/edit/{year}")
    public String showEditForm(@PathVariable("year") int year, Model model) {
        AnnualAssets assets = service
                .getAssetsByYear(year)
                .orElse(new AnnualAssets(
                                year,
                                java.math.BigDecimal.ZERO,
                                java.math.BigDecimal.ZERO,
                                java.math.BigDecimal.ZERO));
        model.addAttribute("annualAssets", assets);
        return "annual_assets_form";
    }

    // 年別資産データ更新処理
    @PostMapping("annualAssets/update")
    public String updateAnnualAssets(@ModelAttribute AnnualAssets annualAssets) {
        service.saveAnnualAssets(annualAssets);
        return "redirect:/annualAssets";
    }

    // 年別資産データ削除
    @PostMapping("annualAssets/delete/{year}")
    public String deleteAnnualAssets(@PathVariable("year") int year) {
        service.deleteAnnualAssets(year);
        return "redirect:/annualAssets";
    }
}
