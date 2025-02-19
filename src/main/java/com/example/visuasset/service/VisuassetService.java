package com.example.visuasset.service;

import com.example.visuasset.entity.AnnualAssets;
import com.example.visuasset.repository.AnnualAssetsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    // 指定した範囲の年のデータを取得
    public List<AnnualAssets> getAssetsBetweenYears(int from, int to){
        return repository.findByTargetYearBetween(from, to);
    }

    public String getMessage() {
        log.info("getMessage was called"); // ログ出力例
        return "Hello World!";
    }
}
