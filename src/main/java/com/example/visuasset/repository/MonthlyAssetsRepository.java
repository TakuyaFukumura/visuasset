package com.example.visuasset.repository;

import com.example.visuasset.entity.MonthlyAssets;
import com.example.visuasset.entity.MonthlyAssetsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyAssetsRepository extends JpaRepository<MonthlyAssets, MonthlyAssetsId> {

    /**
     * 指定した年の月次資産データをリストで取得します。
     *
     * @param targetYear 取得対象の年（西暦）
     * @return 指定年の月次資産リスト
     */
    List<MonthlyAssets> findByTargetYear(int targetYear);
}
