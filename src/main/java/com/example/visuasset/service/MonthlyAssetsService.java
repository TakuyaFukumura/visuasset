package com.example.visuasset.service;

import com.example.visuasset.entity.MonthlyAssets;
import com.example.visuasset.repository.MonthlyAssetsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MonthlyAssetsService {

    private final MonthlyAssetsRepository repository;

    public MonthlyAssetsService(MonthlyAssetsRepository repository) {
        this.repository = repository;
    }

    /**
     * 指定した年の資産データ一覧を取得します。
     *
     * @param year 年（例：2024）
     * @return 指定年のMonthlyAssetsリスト
     */
    public List<MonthlyAssets> getAssetsByYear(int year) {
        return repository.findByTargetYear(year);
    }

    /**
     * 現預金（cash）一覧をカンマ区切りの文字列に変換します。
     *
     * @param monthlyAssetsList MonthlyAssetsのリスト
     * @return 現預金の値をカンマ区切りで連結した文字列
     */
    public String getCashListAsString(List<MonthlyAssets> monthlyAssetsList) {
        return monthlyAssetsList.stream()
                .map(MonthlyAssets::getCash)
                .map(Object::toString)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("");
    }

    /**
     * 有価証券（securities）一覧をカンマ区切りの文字列に変換します。
     *
     * @param monthlyAssetsList MonthlyAssetsのリスト
     * @return 有価証券の値をカンマ区切りで連結した文字列
     */
    public String getSecuritiesListAsString(List<MonthlyAssets> monthlyAssetsList) {
        return monthlyAssetsList.stream()
                .map(MonthlyAssets::getSecurities)
                .map(Object::toString)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("");
    }

    /**
     * 暗号資産（crypto）一覧をカンマ区切りの文字列に変換します。
     *
     * @param monthlyAssetsList MonthlyAssetsのリスト
     * @return 暗号資産の値をカンマ区切りで連結した文字列
     */
    public String getCryptoListAsString(List<MonthlyAssets> monthlyAssetsList) {
        return monthlyAssetsList.stream()
                .map(MonthlyAssets::getCrypto)
                .map(Object::toString)
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("");
    }
}
