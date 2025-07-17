package com.example.visuasset.service;

import com.example.visuasset.entity.YearlyAssets;
import com.example.visuasset.repository.YearlyAssetsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class YearlyAssetsService {

    private final YearlyAssetsRepository repository;

    public YearlyAssetsService(YearlyAssetsRepository repository) {
        this.repository = repository;
    }

    /**
     * 指定した年の資産データを取得する
     *
     * @param year 指定した年
     * @return 資産データ
     */
    public Optional<YearlyAssets> getAssetsByYear(int year) {
        return repository.findByTargetYear(year);
    }

    /**
     * 指定年範囲の資産データを取得する
     *
     * @param from 開始年
     * @param to   終了年
     * @return 指定年範囲の資産データ一覧
     */
    public List<YearlyAssets> getAssetsBetweenYears(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("開始年 (from) は終了年 (to) 以下である必要があります。");
        }

        int currentYear = Year.now().getValue();
        if (from < 1900 || currentYear < to) {
            throw new IllegalArgumentException("指定された年の範囲が不正です。");
        }

        List<YearlyAssets> yearlyAssetsList = repository.findByTargetYearBetween(from, to);

        // 欠けている年がある場合はデフォルト値ゼロの資産データで埋める
        for (int year = from; year <= to; year++) {
            Integer targetYear = year;
            boolean isExist = yearlyAssetsList.stream().anyMatch(a -> a.getTargetYear().equals(targetYear));
            if (!isExist) {
                YearlyAssets empty = new YearlyAssets(year, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                yearlyAssetsList.add(empty);
            }
        }

        yearlyAssetsList.sort(Comparator.comparing(YearlyAssets::getTargetYear));

        return yearlyAssetsList;
    }

    /**
     * 年次資産リストから現預金の金額一覧を取得します。
     *
     * @param yearlyAssetsList 年次資産エンティティのリスト
     * @return 各年の現預金金額一覧
     */
    public List<BigDecimal> getCashList(List<YearlyAssets> yearlyAssetsList) {
        return yearlyAssetsList.stream()
                .map(YearlyAssets::getCash)
                .toList();
    }

    /**
     * 年次資産リストから有価証券の金額一覧を取得します。
     *
     * @param yearlyAssetsList 年次資産エンティティのリスト
     * @return 各年の有価証券金額一覧
     */
    public List<BigDecimal> getSecuritiesList(List<YearlyAssets> yearlyAssetsList) {
        return yearlyAssetsList.stream()
                .map(YearlyAssets::getSecurities)
                .toList();
    }

    /**
     * 年次資産リストから暗号資産の金額一覧を取得します。
     *
     * @param yearlyAssetsList 年次資産エンティティのリスト
     * @return 各年の暗号資産金額一覧
     */
    public List<BigDecimal> getCryptoList(List<YearlyAssets> yearlyAssetsList) {
        return yearlyAssetsList.stream()
                .map(YearlyAssets::getCrypto)
                .toList();
    }

    /**
     * 年次資産リストから年のラベルリストを取得します。
     *
     * @param yearlyAssetsList 年次資産エンティティのリスト
     * @return 各年のラベルリスト（例: ["2020年", "2021年", ...]）
     */
    public List<String> getYearLabels(List<YearlyAssets> yearlyAssetsList) {
        return yearlyAssetsList.stream()
                .map(YearlyAssets::getTargetYear)
                .sorted()
                .map(year -> year + "年")
                .toList();
    }

    // 年別資産データの保存（登録・更新）
    public void saveYearlyAssets(YearlyAssets yearlyAssets) {
        repository.save(yearlyAssets);
    }

    // 年別資産データの削除
    public void deleteYearlyAssets(int year) {
        repository.findByTargetYear(year).ifPresent(repository::delete);
    }

    /**
     * 年別資産データの全件取得
     *
     * @return 全ての年別資産データ一覧
     */
    public List<YearlyAssets> getAllYearlyAssets() {
        return repository.findAll();
    }
}
