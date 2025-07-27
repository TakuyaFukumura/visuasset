package com.example.visuasset.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "simulation_conditions")
public class SimulationCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "condition_name", nullable = false, length = 100)
    private String conditionName; // シミュレーション条件名

    @Column(name = "monthly_investment", nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyInvestment; // 毎月の追加投資金額

    @Column(name = "annual_return_rate", nullable = false, precision = 5, scale = 4)
    private BigDecimal annualReturnRate; // 年間運用利回り（0.05 = 5%）

    @Column(name = "investment_period_years", nullable = false)
    private Integer investmentPeriodYears; // 投資期間（年）

    @Column(name = "initial_amount", precision = 15, scale = 2)
    private BigDecimal initialAmount; // 初期投資額（現在の総資産額）
}