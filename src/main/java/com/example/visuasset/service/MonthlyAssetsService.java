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
