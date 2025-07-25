package com.example.visuasset.controller;

import com.example.visuasset.entity.AssetGoals;
import com.example.visuasset.entity.YearlyAssets;
import com.example.visuasset.service.AssetGoalsService;
import com.example.visuasset.service.YearlyAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/goal-achievement")
public class GoalAchievementController {

    final static int DEFAULT_GOAL_AMOUNT = 15000000; // デフォルトの目標金額

    private final YearlyAssetsService yearlyAssetsService;
    private final AssetGoalsService assetGoalsService;

    @Autowired
    public GoalAchievementController(YearlyAssetsService yearlyAssetsService, AssetGoalsService assetGoalsService) {
        this.yearlyAssetsService = yearlyAssetsService;
        this.assetGoalsService = assetGoalsService;
    }

    @GetMapping
    public String goalAchievement(@RequestParam(name = "targetYear", required = false) Integer targetYear,
                                  Model model) {
        // 最新の目標金額を取得
        Optional<AssetGoals> latestGoal = assetGoalsService.getLatestGoal();
        BigDecimal goalAmount = latestGoal.map(AssetGoals::getGoalAmount).orElse(BigDecimal.valueOf(DEFAULT_GOAL_AMOUNT));

        // 対象年が指定されていない場合、データが存在する最新年を取得
        if (targetYear == null) {
            List<YearlyAssets> allAssets = yearlyAssetsService.getAllYearlyAssets();
            targetYear = allAssets.stream()
                    .mapToInt(YearlyAssets::getTargetYear)
                    .max()
                    .orElse(LocalDate.now().getYear());
        }

        // 指定年の資産データを取得
        Optional<YearlyAssets> yearlyAssetsOpt = yearlyAssetsService.getAssetsByYear(targetYear);
        YearlyAssets yearlyAssets = yearlyAssetsOpt.orElse(new YearlyAssets(targetYear, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));

        // 総資産と達成率を計算
        BigDecimal totalAssets = assetGoalsService.calculateTotalAssets(yearlyAssets);
        BigDecimal achievementRate = assetGoalsService.calculateAchievementRate(yearlyAssets, goalAmount);

        model.addAttribute("targetYear", targetYear);
        model.addAttribute("goalAmount", goalAmount);
        model.addAttribute("totalAssets", totalAssets);
        model.addAttribute("achievementRate", achievementRate);
        model.addAttribute("cash", yearlyAssets.getCash());
        model.addAttribute("securities", yearlyAssets.getSecurities());
        model.addAttribute("crypto", yearlyAssets.getCrypto());

        return "goal_achievement";
    }

    @PostMapping("/update-goal")
    public String updateGoal(@RequestParam("goalAmount") BigDecimal goalAmount,
                             @RequestParam(name = "targetYear", required = false) Integer targetYear) {
        assetGoalsService.saveGoal(goalAmount);

        // 現在の対象年を維持してリダイレクト
        if (targetYear != null) {
            return "redirect:/goal-achievement?targetYear=" + targetYear;
        }
        return "redirect:/goal-achievement";
    }
}