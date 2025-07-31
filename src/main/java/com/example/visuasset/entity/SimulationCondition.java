package com.example.visuasset.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * シミュレーション条件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "simulation_conditions")
public class SimulationCondition {

    /**
     * 主キーID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * シミュレーション条件名
     */
    @Column(name = "condition_name", nullable = false, length = 100)
    private String conditionName;

    /**
     * 毎月の追加投資金額
     */
    @Column(name = "monthly_investment", nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyInvestment;

    /**
     * 年間運用利回り（例: 0.05 = 5%）
     */
    @Column(name = "annual_return_rate", nullable = false, precision = 5, scale = 4)
    private BigDecimal annualReturnRate;

    /**
     * 投資期間（年）
     */
    @Column(name = "investment_period_years", nullable = false)
    private Integer investmentPeriodYears;

    /**
     * 初期投資額
     */
    @Column(name = "initial_amount", precision = 15, scale = 2)
    private BigDecimal initialAmount;
}
