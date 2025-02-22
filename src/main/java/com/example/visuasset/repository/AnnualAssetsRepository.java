package com.example.visuasset.repository;

import com.example.visuasset.entity.AnnualAssets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnualAssetsRepository extends JpaRepository<AnnualAssets, Integer> {

    // 指定した年のデータを取得（Optionalでラップ）
    Optional<AnnualAssets> findByTargetYear(int year);

    // 指定した範囲の年のデータを取得
    List<AnnualAssets> findByTargetYearBetween(int from, int to);
}
