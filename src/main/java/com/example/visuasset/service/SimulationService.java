package com.example.visuasset.service;

import com.example.visuasset.dto.SimulationResult;
import com.example.visuasset.entity.SimulationCondition;
import com.example.visuasset.entity.YearlyAssets;
import com.example.visuasset.repository.SimulationConditionRepository;
import com.example.visuasset.repository.YearlyAssetsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SimulationService {

    private final SimulationConditionRepository simulationConditionRepository;
    private final YearlyAssetsRepository yearlyAssetsRepository;

    public SimulationService(SimulationConditionRepository simulationConditionRepository,
                           YearlyAssetsRepository yearlyAssetsRepository) {
        this.simulationConditionRepository = simulationConditionRepository;
        this.yearlyAssetsRepository = yearlyAssetsRepository;
    }

    /**
     * シミュレーション条件を保存する
     */
    public SimulationCondition saveSimulationCondition(SimulationCondition condition) {
        return simulationConditionRepository.save(condition);
    }

    /**
     * 全てのシミュレーション条件を取得する
     */
    public List<SimulationCondition> getAllSimulationConditions() {
        return simulationConditionRepository.findAll();
    }

    /**
     * 指定IDのシミュレーション条件を取得する
     */
    public Optional<SimulationCondition> getSimulationConditionById(Long id) {
        return simulationConditionRepository.findById(id);
    }

    /**
     * シミュレーション条件を削除する
     */
    public void deleteSimulationCondition(Long id) {
        simulationConditionRepository.deleteById(id);
    }

    /**
     * 現在の総資産額を取得する（最新年のデータ）
     */
    public BigDecimal getCurrentTotalAssets() {
        int currentYear = LocalDate.now().getYear();
        
        // 現在年から過去に向かって検索
        for (int year = currentYear; year >= currentYear - 10; year--) {
            Optional<YearlyAssets> assets = yearlyAssetsRepository.findByTargetYear(year);
            if (assets.isPresent()) {
                YearlyAssets yearlyAssets = assets.get();
                return yearlyAssets.getCash()
                    .add(yearlyAssets.getSecurities())
                    .add(yearlyAssets.getCrypto());
            }
        }
        
        // データが見つからない場合は0を返す
        return BigDecimal.ZERO;
    }

    /**
     * 資産推移シミュレーションを実行する
     */
    public List<SimulationResult> runSimulation(SimulationCondition condition) {
        List<SimulationResult> results = new ArrayList<>();
        
        BigDecimal currentAmount = condition.getInitialAmount() != null 
            ? condition.getInitialAmount() 
            : getCurrentTotalAssets();
        
        BigDecimal monthlyInvestment = condition.getMonthlyInvestment();
        BigDecimal monthlyReturnRate = condition.getAnnualReturnRate()
            .divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP);
        
        int totalMonths = condition.getInvestmentPeriodYears() * 12;
        BigDecimal totalInvested = currentAmount; // 初期投資額も投資元本に含める
        
        // 初期状態を追加
        results.add(new SimulationResult(
            0, 
            currentAmount, 
            currentAmount, 
            BigDecimal.ZERO
        ));
        
        // 月次で複利計算
        for (int month = 1; month <= totalMonths; month++) {
            // 運用利益の計算（既存の総資産に対する利息）
            BigDecimal monthlyReturn = currentAmount.multiply(monthlyReturnRate);
            
            // 総資産の更新（既存資産 + 月次利益 + 新規投資）
            currentAmount = currentAmount.add(monthlyReturn).add(monthlyInvestment);
            
            // 投資元本の更新
            totalInvested = totalInvested.add(monthlyInvestment);
            
            // 運用利益の計算
            BigDecimal returnAmount = currentAmount.subtract(totalInvested);
            
            results.add(new SimulationResult(
                month,
                currentAmount.setScale(2, RoundingMode.HALF_UP),
                totalInvested,
                returnAmount.setScale(2, RoundingMode.HALF_UP)
            ));
        }
        
        return results;
    }

    /**
     * シミュレーション結果から月ラベルを生成する
     */
    public List<String> getMonthLabels(List<SimulationResult> results) {
        List<String> labels = new ArrayList<>();
        for (SimulationResult result : results) {
            if (result.getMonth() == 0) {
                labels.add("開始");
            } else if (result.getMonth() % 12 == 0) {
                labels.add(result.getMonth() / 12 + "年");
            } else {
                labels.add(result.getMonth() + "ヶ月");
            }
        }
        return labels;
    }

    /**
     * シミュレーション結果から総資産額リストを生成する
     */
    public List<BigDecimal> getTotalAmountList(List<SimulationResult> results) {
        return results.stream()
            .map(SimulationResult::getTotalAmount)
            .toList();
    }

    /**
     * シミュレーション結果から投資元本リストを生成する
     */
    public List<BigDecimal> getInvestedAmountList(List<SimulationResult> results) {
        return results.stream()
            .map(SimulationResult::getInvestedAmount)
            .toList();
    }

    /**
     * シミュレーション結果から運用利益リストを生成する
     */
    public List<BigDecimal> getReturnAmountList(List<SimulationResult> results) {
        return results.stream()
            .map(SimulationResult::getReturnAmount)
            .toList();
    }
}