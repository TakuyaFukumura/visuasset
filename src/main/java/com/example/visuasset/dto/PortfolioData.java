package com.example.visuasset.dto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * ポートフォリオデータクラス
 */
public record PortfolioData(int year, List<BigDecimal> amounts, List<BigDecimal> percentages, BigDecimal totalAssets) {

    /**
     * Chart.js用のラベルリストを取得
     */
    public List<String> getLabels() {
        return Arrays.asList("現預金", "有価証券", "暗号資産");
    }
}
