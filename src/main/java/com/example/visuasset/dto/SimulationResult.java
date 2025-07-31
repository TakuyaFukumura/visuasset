package com.example.visuasset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * シミュレーション結果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResult {
    /**
     * 経過月数
     */
    private Integer month;

    /**
     * 総資産額
     */
    private BigDecimal totalAmount;

    /**
     * 投資元本
     */
    private BigDecimal investedAmount;

    /**
     * 運用利益
     */
    private BigDecimal returnAmount;
}
