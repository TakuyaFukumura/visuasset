package com.example.visuasset.repository;

import com.example.visuasset.entity.AssetGoals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetGoalsRepository extends JpaRepository<AssetGoals, Integer> {

    /**
     * 最新の目標金額を取得する
     *
     * @return 最新の目標金額設定
     */
    Optional<AssetGoals> findFirstByOrderByUpdatedAtDesc();
}
