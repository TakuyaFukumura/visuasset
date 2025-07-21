package com.example.visuasset.service;

import com.example.visuasset.entity.AssetGoals;
import com.example.visuasset.entity.YearlyAssets;
import com.example.visuasset.repository.AssetGoalsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Slf4j
@Service
public class AssetGoalsService {

    private final AssetGoalsRepository repository;

    public AssetGoalsService(AssetGoalsRepository repository) {
        this.repository = repository;
    }

    /**
     * 最新の目標金額を取得する
     *
     * @return 最新の目標金額設定
     */
    public Optional<AssetGoals> getLatestGoal() {
        return repository.findLatestGoal();
    }

    /**
     * 目標金額を保存する
     *
     * @param goalAmount 目標金額
     * @return 保存された目標金額設定
     */
    public AssetGoals saveGoal(BigDecimal goalAmount) {
        AssetGoals assetGoal = new AssetGoals();
        assetGoal.setGoalAmount(goalAmount);
        return repository.save(assetGoal);
    }

    /**
     * 指定年の資産データから目標達成率を計算する
     *
     * @param yearlyAssets 年別資産データ
     * @param goalAmount 目標金額
     * @return 目標達成率（パーセント）
     */
    public BigDecimal calculateAchievementRate(YearlyAssets yearlyAssets, BigDecimal goalAmount) {
        if (yearlyAssets == null || goalAmount == null || goalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        // 総資産額を計算
        BigDecimal totalAssets = yearlyAssets.getCash()
                .add(yearlyAssets.getSecurities())
                .add(yearlyAssets.getCrypto());

        // 達成率を計算（パーセント）
        return totalAssets.divide(goalAmount, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 年別資産データから総資産額を計算する
     *
     * @param yearlyAssets 年別資産データ
     * @return 総資産額
     */
    public BigDecimal calculateTotalAssets(YearlyAssets yearlyAssets) {
        if (yearlyAssets == null) {
            return BigDecimal.ZERO;
        }
        return yearlyAssets.getCash()
                .add(yearlyAssets.getSecurities())
                .add(yearlyAssets.getCrypto());
    }
}