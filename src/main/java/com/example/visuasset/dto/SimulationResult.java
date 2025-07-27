package com.example.visuasset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResult {
    private Integer month;             // 経過月数
    private BigDecimal totalAmount;    // 総資産額
    private BigDecimal investedAmount; // 投資元本
    private BigDecimal returnAmount;   // 運用利益
}