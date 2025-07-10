package com.example.visuasset.service;

import com.example.visuasset.entity.MonthlyAssets;
import com.example.visuasset.repository.MonthlyAssetsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class MonthlyAssetsService {

    private final MonthlyAssetsRepository repository;

    public MonthlyAssetsService(MonthlyAssetsRepository repository) {
        this.repository = repository;
    }

    /**
     * 指定した年の月次資産データ一覧を取得します。
     *
     * @param year 取得対象の年（例：2024）
     * @return 指定年のMonthlyAssetsエンティティのリスト
     */
    public List<MonthlyAssets> getAssetsByYear(int year) {
        return repository.findByTargetYear(year);
    }

    /**
     * 月次資産リストから現預金の金額一覧を取得します。
     *
     * @param monthlyAssetsList 月次資産エンティティのリスト
     * @return 各月の現預金金額一覧
     */
    public List<BigDecimal> getCashList(List<MonthlyAssets> monthlyAssetsList) {
        return monthlyAssetsList.stream()
                .map(MonthlyAssets::getCash)
                .toList();
    }

    /**
     * 月次資産リストから有価証券の金額一覧を取得します。
     *
     * @param monthlyAssetsList 月次資産エンティティのリスト
     * @return 各月の有価証券金額の一覧
     */
    public List<BigDecimal> getSecuritiesList(List<MonthlyAssets> monthlyAssetsList) {
        return monthlyAssetsList.stream()
                .map(MonthlyAssets::getSecurities)
                .toList();
    }

    /**
     * 月次資産リストから暗号資産の金額一覧を取得します。
     *
     * @param monthlyAssetsList 月次資産エンティティのリスト
     * @return 各月の暗号資産金額の一覧
     */
    public List<BigDecimal> getCryptoList(List<MonthlyAssets> monthlyAssetsList) {
        return monthlyAssetsList.stream()
                .map(MonthlyAssets::getCrypto)
                .toList();
    }

    /**
     * 月次資産リストからデータが存在する月のみのラベルリストを返す
     *
     * @param monthlyAssetsList 月次資産エンティティのリスト
     * @return データが存在する月のラベルリスト（例: ["1月", "3月", ...]）
     */
    public List<String> getMonthLabels(List<MonthlyAssets> monthlyAssetsList) {
        return monthlyAssetsList.stream()
                .map(MonthlyAssets::getTargetMonth)
                .distinct()
                .sorted()
                .map(i -> i + "月")
                .toList();
    }
}
