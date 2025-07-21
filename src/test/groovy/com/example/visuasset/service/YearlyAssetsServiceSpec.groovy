package com.example.visuasset.service

import com.example.visuasset.entity.YearlyAssets
import com.example.visuasset.repository.YearlyAssetsRepository
import spock.lang.Specification

class YearlyAssetsServiceSpec extends Specification {

    def repository = Mock(YearlyAssetsRepository)
    def service = new YearlyAssetsService(repository)

    def "getTotalAssetsList should return correct total amounts"() {
        given:
        def yearlyAssets1 = new YearlyAssets(2020, 1000000, 2000000, 500000)
        def yearlyAssets2 = new YearlyAssets(2021, 1500000, 2500000, 1000000)
        def yearlyAssetsList = [yearlyAssets1, yearlyAssets2]

        when:
        def result = service.getTotalAssetsList(yearlyAssetsList)

        then:
        result.size() == 2
        result[0] == 3500000  // 1000000 + 2000000 + 500000
        result[1] == 5000000  // 1500000 + 2500000 + 1000000
    }

    def "getTotalAssetsList should handle empty list"() {
        given:
        def yearlyAssetsList = []

        when:
        def result = service.getTotalAssetsList(yearlyAssetsList)

        then:
        result.size() == 0
    }

    def "getTotalAssetsList should handle zero values"() {
        given:
        def yearlyAssets = new YearlyAssets(2020, 0, 0, 0)
        def yearlyAssetsList = [yearlyAssets]

        when:
        def result = service.getTotalAssetsList(yearlyAssetsList)

        then:
        result.size() == 1
        result[0] == 0
    }
}