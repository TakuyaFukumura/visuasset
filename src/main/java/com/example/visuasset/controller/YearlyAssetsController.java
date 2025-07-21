package com.example.visuasset.controller;

import com.example.visuasset.entity.YearlyAssets;
import com.example.visuasset.service.YearlyAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/") // URLとの関連付け http://localhost:8080/ の時に呼ばれる
public class YearlyAssetsController {

    private final YearlyAssetsService service;

    @Autowired
    public YearlyAssetsController(YearlyAssetsService service) {
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

        List<YearlyAssets> yearlyAssetsList = service.getAssetsBetweenYears(startYear, endYear);

        model.addAttribute("startYear", startYear);
        model.addAttribute("endYear", endYear);
        model.addAttribute("cashList", service.getCashList(yearlyAssetsList));
        model.addAttribute("securitiesList", service.getSecuritiesList(yearlyAssetsList));
        model.addAttribute("cryptoList", service.getCryptoList(yearlyAssetsList));
        model.addAttribute("labels", service.getYearLabels(yearlyAssetsList));
        return "yearly";
    }

    // 年別資産データ一覧画面
    @GetMapping("yearly/list")
    public String yearlyAssetsList(Model model) {
        List<YearlyAssets> list = service.getAllYearlyAssets();
        model.addAttribute("yearlyAssetsList", list);
        return "yearly_list";
    }

    // 年別資産データ新規登録画面
    @GetMapping("yearly/new")
    public String showCreateForm(Model model) {
        model.addAttribute("yearlyAssets", new YearlyAssets());
        return "yearly_form";
    }

    // 年別資産データ新規登録処理
    @PostMapping("yearly/insert")
    public String createYearlyAssets(@ModelAttribute YearlyAssets yearlyAssets) {
        service.saveYearlyAssets(yearlyAssets);
        return "redirect:/yearly/list";
    }

    // 年別資産データ編集画面
    @GetMapping("yearly/edit/{year}")
    public String showEditForm(@PathVariable("year") int year, Model model) {
        YearlyAssets yearlyAssets = service.getAssetsByYear(year).orElse(new YearlyAssets());
        model.addAttribute("yearlyAssets", yearlyAssets);
        return "yearly_form";
    }

    // 年別資産データ更新処理
    @PostMapping("yearly/update")
    public String updateYearlyAssets(@ModelAttribute YearlyAssets yearlyAssets) {
        service.saveYearlyAssets(yearlyAssets);
        return "redirect:/yearly/list";
    }

    // 年別資産データ削除
    @PostMapping("yearly/delete/{year}")
    public String deleteYearlyAssets(@PathVariable("year") int year) {
        service.deleteYearlyAssets(year);
        return "redirect:/yearly/list";
    }

    // 年別資産データのCSV出力
    @GetMapping("yearly/export/csv")
    public ResponseEntity<byte[]> exportYearlyAssetsToCSV() {
        try {
            String csvContent = service.exportToCSV();
            String fileName = service.generateCSVFileName();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvContent.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
