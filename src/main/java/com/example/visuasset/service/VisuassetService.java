package com.example.visuasset.service;

import com.example.visuasset.entity.AnnualAssets;
import com.example.visuasset.repository.AnnualAssetsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Comparator;
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

        List<AnnualAssets> annualAssetsList = repository.findByTargetYearBetween(from, to);

        // 欠けている年がある場合はデフォルト値ゼロの資産データで埋める
        for (int year = from; year <= to; year++) {
            Integer targetYear = year;
            boolean isExist = annualAssetsList.stream().anyMatch(a -> a.getTargetYear().equals(targetYear));
            if (!isExist) {
                AnnualAssets empty = new AnnualAssets(year, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                annualAssetsList.add(empty);
            }
        }

        // 年でソート
        annualAssetsList.sort(Comparator.comparing(AnnualAssets::getTargetYear));

//        int diff = assets.size() - (to - from + 1);
//        if (diff != 0) {
//            throw new RuntimeException("データが欠けています。不足数：" + diff);
//        }

        return annualAssetsList;
    }

    public String getMessage() {
        log.info("getMessage was called"); // ログ出力例
        return "Hello World!";
    }
}
