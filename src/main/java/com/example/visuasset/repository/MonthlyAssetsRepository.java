package com.example.visuasset.repository;

import com.example.visuasset.entity.MonthlyAssets;
import com.example.visuasset.entity.MonthlyAssetsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyAssetsRepository extends JpaRepository<MonthlyAssets, MonthlyAssetsId> {
    // 指定した年月のデータを取得（Optionalでラップ）
    Optional<MonthlyAssets> findByTargetYearAndTargetMonth(int targetYear, int targetMonth);

    // 指定した範囲の年月のデータを取得
    List<MonthlyAssets> findByTargetYearBetweenAndTargetMonthBetween(int fromYear, int toYear, int fromMonth, int toMonth);

    // 指定した年のデータをリストで取得
    List<MonthlyAssets> findByTargetYear(int targetYear);
}
