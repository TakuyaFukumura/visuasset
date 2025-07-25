package com.example.visuasset.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 月別資産情報
 * <p>
 * 対象年（targetYear）と対象月（targetMonth）を複合主キーとし、
 * 現預金（cash）、有価証券（securities）、暗号資産（crypto）の月別残高を管理します。
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "monthly_assets")
@IdClass(MonthlyAssetsId.class)
public class MonthlyAssets {

    /** 対象年 */
    @Id
    @Column(name = "target_year", nullable = false)
    private Integer targetYear;

    /** 対象月 */
    @Id
    @Column(name = "target_month", nullable = false)
    private Integer targetMonth;

    /** 現預金 */
    @Column(name = "cash", nullable = false, precision = 15, scale = 2)
    private BigDecimal cash;

    /** 有価証券 */
    @Column(name = "securities", nullable = false, precision = 15, scale = 2)
    private BigDecimal securities;

    /** 暗号資産 */
    @Column(name = "crypto", nullable = false, precision = 15, scale = 2)
    private BigDecimal crypto;
}
