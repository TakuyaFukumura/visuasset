package com.example.visuasset.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "monthly_assets")
public class MonthlyAssets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "target_year", nullable = false)
    private Integer targetYear; // 対象年

    @Column(name = "target_month", nullable = false)
    private Integer targetMonth; // 対象月

    @Column(name = "cash", nullable = false, precision = 15, scale = 2)
    private BigDecimal cash; // 現預金

    @Column(name = "securities", nullable = false, precision = 15, scale = 2)
    private BigDecimal securities; // 有価証券

    @Column(name = "crypto", nullable = false, precision = 15, scale = 2)
    private BigDecimal crypto; // 暗号資産
}
