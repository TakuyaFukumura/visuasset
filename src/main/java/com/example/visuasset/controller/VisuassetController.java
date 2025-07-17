package com.example.visuasset.controller;

import com.example.visuasset.entity.AnnualAssets;
import com.example.visuasset.service.VisuassetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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
    public String yearly(@RequestParam(name = "startYear", required = false) Integer startYear,
                         @RequestParam(name = "endYear", required = false) Integer endYear,
                         Model model) {
        if (startYear == null) {
            startYear = 2020; // デフォルト値
        }
        if (endYear == null) {
            endYear = 2025; // デフォルト値
        }
        int currentYear = LocalDate.now().getYear();
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

        model.addAttribute("startYear", startYear);
        model.addAttribute("endYear", endYear);

        String cashList = service.getCashListAsString(annualAssetsList);
        String securitiesList = service.getSecuritiesListAsString(annualAssetsList);
        String cryptoList = service.getCryptoListAsString(annualAssetsList);

        model.addAttribute("cashList", cashList);
        model.addAttribute("securitiesList", securitiesList);
        model.addAttribute("cryptoList", cryptoList);
        return "yearly";
    }

    // 年別資産データ一覧画面
    @GetMapping("yearly/list")
    public String annualAssetsList(Model model) {
        List<AnnualAssets> list = service.getAllAnnualAssets();
        model.addAttribute("annualAssetsList", list);
        return "yearly_list";
    }

    // 年別資産データ新規登録画面
    @GetMapping("yearly/new")
    public String showCreateForm(Model model) {
        model.addAttribute("annualAssets", new AnnualAssets());
        return "yearly_form";
    }

    // 年別資産データ新規登録処理
    @PostMapping("yearly/insert")
    public String createAnnualAssets(@ModelAttribute AnnualAssets annualAssets) {
        service.saveAnnualAssets(annualAssets);
        return "redirect:/yearly/list";
    }

    // 年別資産データ編集画面
    @GetMapping("yearly/edit/{year}")
    public String showEditForm(@PathVariable("year") int year, Model model) {
        AnnualAssets annualAssets = service.getAssetsByYear(year).orElse(new AnnualAssets());
        model.addAttribute("annualAssets", annualAssets);
        return "yearly_form";
    }

    // 年別資産データ更新処理
    @PostMapping("yearly/update")
    public String updateAnnualAssets(@ModelAttribute AnnualAssets annualAssets) {
        service.saveAnnualAssets(annualAssets);
        return "redirect:/yearly/list";
    }

    // 年別資産データ削除
    @PostMapping("yearly/delete/{year}")
    public String deleteAnnualAssets(@PathVariable("year") int year) {
        service.deleteAnnualAssets(year);
        return "redirect:/yearly/list";
    }
}
