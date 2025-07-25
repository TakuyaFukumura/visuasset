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

    static final int DIVISION_SCALE = 4; // 小数点以下の桁数を指定

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
        return repository.findFirstByOrderByUpdatedAtDesc();
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
     * @param goalAmount   目標金額
     * @return 目標達成率（パーセント）
     */
    public BigDecimal calculateAchievementRate(YearlyAssets yearlyAssets, BigDecimal goalAmount) {
        if (yearlyAssets == null || goalAmount == null || goalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalAssets = calculateTotalAssets(yearlyAssets);

        // 達成率を計算（パーセント）
        return totalAssets.divide(goalAmount, DIVISION_SCALE, RoundingMode.HALF_UP)
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
