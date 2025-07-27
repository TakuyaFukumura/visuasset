package com.example.visuasset.controller;

import com.example.visuasset.dto.SimulationResult;
import com.example.visuasset.entity.SimulationCondition;
import com.example.visuasset.service.SimulationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    /**
     * シミュレーション画面表示
     */
    @GetMapping
    public String simulation(Model model) {
        // デフォルト条件を設定
        SimulationCondition defaultCondition = new SimulationCondition();
        defaultCondition.setConditionName("デフォルト条件");
        defaultCondition.setMonthlyInvestment(new BigDecimal("70000"));
        defaultCondition.setAnnualReturnRate(new BigDecimal("0.05"));
        defaultCondition.setInvestmentPeriodYears(20);
        defaultCondition.setInitialAmount(simulationService.getCurrentTotalAssets());

        model.addAttribute("simulationCondition", defaultCondition);
        model.addAttribute("savedConditions", simulationService.getAllSimulationConditions());

        // デフォルト条件でシミュレーション実行
        List<SimulationResult> results = simulationService.runSimulation(defaultCondition);
        addSimulationResultsToModel(model, results);

        return "simulation";
    }

    /**
     * シミュレーション実行
     */
    @PostMapping("/run")
    public String runSimulation(@ModelAttribute SimulationCondition simulationCondition, Model model) {
        // 初期金額が設定されていない場合は現在の総資産額を使用
        if (simulationCondition.getInitialAmount() == null) {
            simulationCondition.setInitialAmount(simulationService.getCurrentTotalAssets());
        }

        List<SimulationResult> results = simulationService.runSimulation(simulationCondition);

        model.addAttribute("simulationCondition", simulationCondition);
        model.addAttribute("savedConditions", simulationService.getAllSimulationConditions());
        addSimulationResultsToModel(model, results);

        return "simulation";
    }

    /**
     * シミュレーション条件保存
     */
    @PostMapping("/save")
    public String saveSimulationCondition(@ModelAttribute SimulationCondition simulationCondition) {
        // 初期金額が設定されていない場合は現在の総資産額を使用
        if (simulationCondition.getInitialAmount() == null) {
            simulationCondition.setInitialAmount(simulationService.getCurrentTotalAssets());
        }

        simulationService.saveSimulationCondition(simulationCondition);
        return "redirect:/simulation";
    }

    /**
     * 保存済み条件でシミュレーション実行
     */
    @GetMapping("/load/{id}")
    public String loadSimulationCondition(@PathVariable Long id, Model model) {
        SimulationCondition condition = simulationService.getSimulationConditionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid condition id: " + id));

        // 現在の総資産額で初期金額を更新
        condition.setInitialAmount(simulationService.getCurrentTotalAssets());

        List<SimulationResult> results = simulationService.runSimulation(condition);

        model.addAttribute("simulationCondition", condition);
        model.addAttribute("savedConditions", simulationService.getAllSimulationConditions());
        addSimulationResultsToModel(model, results);

        return "simulation";
    }

    /**
     * シミュレーション条件削除
     */
    @PostMapping("/delete/{id}")
    public String deleteSimulationCondition(@PathVariable Long id) {
        simulationService.deleteSimulationCondition(id);
        return "redirect:/simulation";
    }

    /**
     * シミュレーション結果をモデルに追加する共通メソッド
     */
    private void addSimulationResultsToModel(Model model, List<SimulationResult> results) {
        model.addAttribute("simulationResults", results);
        model.addAttribute("monthLabels", simulationService.getMonthLabels(results));
        model.addAttribute("totalAmountList", simulationService.getTotalAmountList(results));
        model.addAttribute("investedAmountList", simulationService.getInvestedAmountList(results));
        model.addAttribute("returnAmountList", simulationService.getReturnAmountList(results));

        // 最終結果のサマリー
        if (!results.isEmpty()) {
            SimulationResult finalResult = results.get(results.size() - 1);
            model.addAttribute("finalTotalAmount", finalResult.getTotalAmount());
            model.addAttribute("finalInvestedAmount", finalResult.getInvestedAmount());
            model.addAttribute("finalReturnAmount", finalResult.getReturnAmount());

            // 利回り効果の計算
            if (finalResult.getInvestedAmount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal returnPercentage = finalResult.getReturnAmount()
                        .multiply(BigDecimal.valueOf(100))
                        .divide(finalResult.getInvestedAmount(), 2, java.math.RoundingMode.HALF_UP);
                model.addAttribute("returnPercentage", returnPercentage);
            } else {
                model.addAttribute("returnPercentage", BigDecimal.ZERO);
            }
        }
    }
}
