package com.example.visuasset.repository;

import com.example.visuasset.entity.MonthlyAssets;
import com.example.visuasset.entity.MonthlyAssetsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyAssetsRepository extends JpaRepository<MonthlyAssets, MonthlyAssetsId> {

    /**
     * 指定した年の月別資産一覧を取得します。
     *
     * @param targetYear 取得対象の年（西暦）
     * @return 指定年の月別資産一覧
     */
    List<MonthlyAssets> findByTargetYear(int targetYear);
}
