package com.example.visuasset.dto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * ポートフォリオデータクラス
 */
public class PortfolioData {
    private final int year;
    private final List<BigDecimal> amounts;
    private final List<BigDecimal> percentages;
    private final BigDecimal totalAssets;

    public PortfolioData(int year, List<BigDecimal> amounts, List<BigDecimal> percentages, BigDecimal totalAssets) {
        this.year = year;
        this.amounts = amounts;
        this.percentages = percentages;
        this.totalAssets = totalAssets;
    }

    public int getYear() {
        return year;
    }

    public List<BigDecimal> getAmounts() {
        return amounts;
    }

    public List<BigDecimal> getPercentages() {
        return percentages;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    /**
     * Chart.js用のラベルリストを取得
     */
    public List<String> getLabels() {
        return Arrays.asList("現預金", "有価証券", "暗号資産");
    }
}
