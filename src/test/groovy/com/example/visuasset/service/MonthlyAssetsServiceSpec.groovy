package com.example.visuasset.service

import com.example.visuasset.entity.MonthlyAssets
import spock.lang.Specification

class MonthlyAssetsServiceSpec extends Specification {
    def "getMonthLabels 月のラベルリストを返す"() {
        given:
        def service = new MonthlyAssetsService(null)
        def monthlyAssetsList = [
                Stub(MonthlyAssets) { getTargetMonth() >> 1 },
                Stub(MonthlyAssets) { getTargetMonth() >> 3 },
                Stub(MonthlyAssets) { getTargetMonth() >> 2 },
                Stub(MonthlyAssets) { getTargetMonth() >> 1 }
        ]

        when:
        def result = service.getMonthLabels(monthlyAssetsList)

        then:
        result == ["1月", "2月", "3月"]
    }
}
