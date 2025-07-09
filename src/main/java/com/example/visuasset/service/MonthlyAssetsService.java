package com.example.visuasset.service;

import com.example.visuasset.entity.MonthlyAssets;
import com.example.visuasset.repository.MonthlyAssetsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MonthlyAssetsService {

    private final MonthlyAssetsRepository repository;

    public MonthlyAssetsService(MonthlyAssetsRepository repository) {
        this.repository = repository;
    }

    /**
     * 指定した年月の資産データを取得する
     *
     * @param year  年
     * @param month 月
     * @return 資産データ
     */
    public Optional<MonthlyAssets> getAssetsByYearMonth(int year, int month) {
        return repository.findByTargetYearAndTargetMonth(year, month);
    }

    /**
     * 指定年月範囲の資産データを取得する
     *
     * @param fromYear  開始年
     * @param fromMonth 開始月
     * @param toYear    終了年
     * @param toMonth   終了月
     * @return 指定範囲の資産データ一覧
     */
    public List<MonthlyAssets> getAssetsBetweenYearMonths(int fromYear, int fromMonth, int toYear, int toMonth) {
        if (fromYear > toYear || (fromYear == toYear && fromMonth > toMonth)) {
            throw new IllegalArgumentException("開始年月は終了年月以前である必要があります。");
        }
        List<MonthlyAssets> monthlyAssetsList = repository.findByTargetYearBetweenAndTargetMonthBetween(fromYear, toYear, fromMonth, toMonth);
        List<MonthlyAssets> result = new ArrayList<>();
        YearMonth from = YearMonth.of(fromYear, fromMonth);
        YearMonth to = YearMonth.of(toYear, toMonth);
        for (YearMonth ym = from; !ym.isAfter(to); ym = ym.plusMonths(1)) {
            int y = ym.getYear();
            int m = ym.getMonthValue();
            boolean isExist = monthlyAssetsList.stream().anyMatch(a -> a.getTargetYear() == y && a.getTargetMonth() == m);
            if (!isExist) {
                MonthlyAssets empty = new MonthlyAssets(y, m, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                result.add(empty);
            } else {
                monthlyAssetsList.stream().filter(a -> a.getTargetYear() == y && a.getTargetMonth() == m).forEach(result::add);
            }
        }
        result.sort(Comparator.comparing(MonthlyAssets::getTargetYear).thenComparing(MonthlyAssets::getTargetMonth));
        return result;
    }

    /**
     * 指定年の資産データ一覧を取得
     *
     * @param year 年
     * @return 資産データ一覧
     */
    public List<MonthlyAssets> getAssetsByYear(int year) {
        return repository.findByTargetYear(year);
    }

    /**
     * 現預金一覧をカンマ区切りの文字列に変換する
     */
    public String getCashListAsString(List<MonthlyAssets> monthlyAssetsList) {
        return monthlyAssetsList.stream()
                .map(MonthlyAssets::getCash)
                .map(Object::toString)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("");
    }

    /**
     * 有価証券一覧をカンマ区切りの文字列に変換する
     */
    public String getSecuritiesListAsString(List<MonthlyAssets> monthlyAssetsList) {
        return monthlyAssetsList.stream()
                .map(MonthlyAssets::getSecurities)
                .map(Object::toString)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("");
    }

    /**
     * 暗号資産一覧をカンマ区切りの文字列に変換する
     */
    public String getCryptoListAsString(List<MonthlyAssets> monthlyAssetsList) {
        return monthlyAssetsList.stream()
                .map(MonthlyAssets::getCrypto)
                .map(Object::toString)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("");
    }
}
