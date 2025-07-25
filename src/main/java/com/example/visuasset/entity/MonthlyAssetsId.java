package com.example.visuasset.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 月別資産ID
 * <p>
 * targetYear（対象年）とtargetMonth（対象月）の複合主キーとして利用されます。
 * JPAの@Embeddableクラスや@IdClassとして利用されることを想定しています。
 * </p>
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAssetsId implements Serializable {

    /**
     * 対象年（例：2025）
     */
    private Integer targetYear;
    /**
     * 対象月（1〜12）
     */
    private Integer targetMonth;

    /**
     * equalsメソッド。targetYearとtargetMonthが等しい場合にtrueを返します。
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyAssetsId that = (MonthlyAssetsId) o;
        return Objects.equals(targetYear, that.targetYear) && Objects.equals(targetMonth, that.targetMonth);
    }

    /**
     * hashCodeメソッド。targetYearとtargetMonthを元にハッシュ値を生成します。
     */
    @Override
    public int hashCode() {
        return Objects.hash(targetYear, targetMonth);
    }
}
