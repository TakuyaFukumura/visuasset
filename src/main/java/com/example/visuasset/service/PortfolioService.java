package com.example.visuasset.service;

import com.example.visuasset.dto.PortfolioData;
import com.example.visuasset.entity.YearlyAssets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PortfolioService {

    private final YearlyAssetsService yearlyAssetsService;

    public PortfolioService(YearlyAssetsService yearlyAssetsService) {
        this.yearlyAssetsService = yearlyAssetsService;
    }

    /**
     * 指定した年の資産ポートフォリオデータを取得する
     *
     * @param year 指定した年
     * @return PortfolioData または null（データが存在しない場合）
     */
    public PortfolioData getPortfolioData(int year) {
        Optional<YearlyAssets> assetsOpt = yearlyAssetsService.getAssetsByYear(year);
        
        if (assetsOpt.isEmpty()) {
            return null;
        }

        YearlyAssets assets = assetsOpt.get();
        
        // 総資産を計算
        BigDecimal totalAssets = assets.getCash()
                .add(assets.getSecurities())
                .add(assets.getCrypto());
        
        // 総資産が0の場合は空のポートフォリオを返す
        if (totalAssets.compareTo(BigDecimal.ZERO) == 0) {
            return new PortfolioData(
                    year,
                    Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO),
                    Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO),
                    BigDecimal.ZERO
            );
        }
        
        // パーセンテージを計算（小数点第1位まで）
        BigDecimal cashPercentage = calculatePercentage(assets.getCash(), totalAssets);
        BigDecimal securitiesPercentage = calculatePercentage(assets.getSecurities(), totalAssets);
        BigDecimal cryptoPercentage = calculatePercentage(assets.getCrypto(), totalAssets);

        List<BigDecimal> amounts = Arrays.asList(assets.getCash(), assets.getSecurities(), assets.getCrypto());
        List<BigDecimal> percentages = Arrays.asList(cashPercentage, securitiesPercentage, cryptoPercentage);
        
        return new PortfolioData(year, amounts, percentages, totalAssets);
    }

    /**
     * 年の妥当性をチェックし、適切な年を返す
     *
     * @param year チェックする年
     * @return 妥当な年（現在年以下）
     */
    public int validateYear(Integer year) {
        int currentYear = Year.now().getValue();
        
        if (year == null) {
            return currentYear;
        }
        
        // 上限は現在年
        if (year > currentYear) {
            return currentYear;
        }
        
        // 下限は1900年
        if (year < 1900) {
            return 1900;
        }
        
        return year;
    }

    /**
     * 指定された資産額が総資産に対して占める割合（パーセンテージ）を計算します。
     *
     * @param asset       個別の資産額
     * @param totalAssets 総資産額
     * @return 資産額が総資産に占める割合（小数点第1位まで四捨五入されたパーセンテージ）
     */
    private BigDecimal calculatePercentage(BigDecimal asset, BigDecimal totalAssets) {
        return asset.multiply(BigDecimal.valueOf(100))
                .divide(totalAssets, 1, RoundingMode.HALF_UP);
    }
}
