package com.example.visuasset.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAssetsId implements Serializable {

    private Integer targetYear;
    private Integer targetMonth;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyAssetsId that = (MonthlyAssetsId) o;
        return Objects.equals(targetYear, that.targetYear) && Objects.equals(targetMonth, that.targetMonth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetYear, targetMonth);
    }
}
