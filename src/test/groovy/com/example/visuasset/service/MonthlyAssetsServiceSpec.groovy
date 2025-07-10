package com.example.visuasset.service

import com.example.visuasset.entity.MonthlyAssets
import spock.lang.Specification

class MonthlyAssetsServiceSpec extends Specification {

    def "getCashList 現預金金額リストを返す"() {
        given:
        def service = new MonthlyAssetsService(null)
        def monthlyAssetsList = [
                Stub(MonthlyAssets) { getCash() >> 100.0 },
                Stub(MonthlyAssets) { getCash() >> 200.0 },
                Stub(MonthlyAssets) { getCash() >> 300.0 }
        ]

        when:
        def result = service.getCashList(monthlyAssetsList)

        then:
        result == [100.0, 200.0, 300.0]
    }

    def "getSecuritiesList 有価証券金額リストを返す"() {
        given:
        def service = new MonthlyAssetsService(null)
        def monthlyAssetsList = [
                Stub(MonthlyAssets) { getSecurities() >> 10.5 },
                Stub(MonthlyAssets) { getSecurities() >> 20.5 },
                Stub(MonthlyAssets) { getSecurities() >> 30.5 }
        ]

        when:
        def result = service.getSecuritiesList(monthlyAssetsList)

        then:
        result == [10.5, 20.5, 30.5]
    }

    def "getCryptoList 暗号資産金額リストを返す"() {
        given:
        def service = new MonthlyAssetsService(null)
        def monthlyAssetsList = [
                Stub(MonthlyAssets) { getCrypto() >> 1.1 },
                Stub(MonthlyAssets) { getCrypto() >> 2.2 },
                Stub(MonthlyAssets) { getCrypto() >> 3.3 }
        ]

        when:
        def result = service.getCryptoList(monthlyAssetsList)

        then:
        result == [1.1, 2.2, 3.3]
    }

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
