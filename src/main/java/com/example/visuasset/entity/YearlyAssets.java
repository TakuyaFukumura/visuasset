package com.example.visuasset.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "yearly_assets")
public class YearlyAssets {

    @Id
    @Column(name = "target_year", nullable = false)
    private Integer targetYear; // 記録する年

    @Column(name = "cash", nullable = false, precision = 15, scale = 2)
    private BigDecimal cash; // 現預金

    @Column(name = "securities", nullable = false, precision = 15, scale = 2)
    private BigDecimal securities; // 有価証券

    @Column(name = "crypto", nullable = false, precision = 15, scale = 2)
    private BigDecimal crypto; // 暗号資産
}
