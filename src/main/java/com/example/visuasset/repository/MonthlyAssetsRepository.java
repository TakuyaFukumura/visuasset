package com.example.visuasset.repository;

import com.example.visuasset.entity.MonthlyAssets;
import com.example.visuasset.entity.MonthlyAssetsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlyAssetsRepository extends JpaRepository<MonthlyAssets, MonthlyAssetsId> {

    // 指定した年のデータをリストで取得
    List<MonthlyAssets> findByTargetYear(int targetYear);
}
