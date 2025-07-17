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
     * 現預金一覧をカンマ区切りの文字列に変換する
     *
     * @param yearlyAssetsList 資産データ一覧
     * @return 現預金のカンマ区切り文字列
     */
    public String getCashListAsString(List<YearlyAssets> yearlyAssetsList) {
        return yearlyAssetsList.stream()
                .map(YearlyAssets::getCash)
                .map(Object::toString)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElseThrow(() -> new RuntimeException("現預金一覧の文字列変換で例外が発生"));
    }

    /**
     * 有価証券一覧をカンマ区切りの文字列に変換する
     *
     * @param yearlyAssetsList 資産データ一覧
     * @return 有価証券のカンマ区切り文字列
     */
    public String getSecuritiesListAsString(List<YearlyAssets> yearlyAssetsList) {
        return yearlyAssetsList.stream()
                .map(YearlyAssets::getSecurities)
                .map(Object::toString)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElseThrow(() -> new RuntimeException("有価証券一覧の文字列変換で例外が発生"));
    }

    /**
     * 暗号資産一覧をカンマ区切りの文字列に変換する
     *
     * @param yearlyAssetsList 資産データ一覧
     * @return 暗号資産のカンマ区切り文字列
     */
    public String getCryptoListAsString(List<YearlyAssets> yearlyAssetsList) {
        return yearlyAssetsList.stream()
                .map(YearlyAssets::getCrypto)
                .map(Object::toString)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElseThrow(() -> new RuntimeException("暗号資産一覧の文字列変換で例外が発生"));
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
