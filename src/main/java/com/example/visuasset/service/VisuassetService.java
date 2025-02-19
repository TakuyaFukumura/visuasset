package com.example.visuasset.service;

import com.example.visuasset.entity.AnnualAssets;
import com.example.visuasset.repository.AnnualAssetsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VisuassetService {

    private final AnnualAssetsRepository repository;

    public VisuassetService(AnnualAssetsRepository repository) {
        this.repository = repository;
    }

    // 指定した年の資産データを取得（存在しない場合は Optional.empty()）
    public Optional<AnnualAssets> getAssetsByYear(int year) {
        return repository.findByTargetYear(year);
    }

    /**
     * 指定年範囲の資産データを取得する
     *
     * @param from 開始年
     * @param to   終了年
     * @return 指定年範囲の資産データ一覧
     */
    public List<AnnualAssets> getAssetsBetweenYears(int from, int to) {
        if (from > to) {
            throw new IllegalArgumentException("開始年 (from) は終了年 (to) 以下である必要があります。");
        }

        int currentYear = Year.now().getValue();
        if (from < 1900 || currentYear < to) {
            throw new IllegalArgumentException("指定された年の範囲が不正です。");
        }

        return repository.findByTargetYearBetween(from, to);
    }

    public String getMessage() {
        log.info("getMessage was called"); // ログ出力例
        return "Hello World!";
    }
}
