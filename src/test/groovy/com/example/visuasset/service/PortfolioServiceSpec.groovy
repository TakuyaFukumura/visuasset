package com.example.visuasset.service

import com.example.visuasset.entity.YearlyAssets
import com.example.visuasset.service.PortfolioService.PortfolioData
import spock.lang.Specification

import java.time.Year

class PortfolioServiceSpec extends Specification {

    YearlyAssetsService yearlyAssetsService = Mock()
    PortfolioService portfolioService = new PortfolioService(yearlyAssetsService)

    def "getPortfolioData 正常なデータでポートフォリオデータを返す"() {
        given:
        def year = 2023
        def assets = new YearlyAssets(year, BigDecimal.valueOf(1000), BigDecimal.valueOf(2000), BigDecimal.valueOf(1000))
        yearlyAssetsService.getAssetsByYear(year) >> Optional.of(assets)

        when:
        def result = portfolioService.getPortfolioData(year)

        then:
        result != null
        result.year == year
        result.totalAssets == BigDecimal.valueOf(4000)
        result.amounts == [BigDecimal.valueOf(1000), BigDecimal.valueOf(2000), BigDecimal.valueOf(1000)]
        result.percentages == [BigDecimal.valueOf(25.0), BigDecimal.valueOf(50.0), BigDecimal.valueOf(25.0)]
        result.labels == ["現預金", "有価証券", "暗号資産"]
    }

    def "getPortfolioData データが存在しない場合nullを返す"() {
        given:
        def year = 2023
        yearlyAssetsService.getAssetsByYear(year) >> Optional.empty()

        when:
        def result = portfolioService.getPortfolioData(year)

        then:
        result == null
    }

    def "getPortfolioData 総資産が0の場合は全て0%のデータを返す"() {
        given:
        def year = 2023
        def assets = new YearlyAssets(year, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)
        yearlyAssetsService.getAssetsByYear(year) >> Optional.of(assets)

        when:
        def result = portfolioService.getPortfolioData(year)

        then:
        result != null
        result.year == year
        result.totalAssets == BigDecimal.ZERO
        result.amounts == [BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO]
        result.percentages == [BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO]
    }

    def "validateYear nullの場合は現在年を返す"() {
        when:
        def result = portfolioService.validateYear(null)

        then:
        result == Year.now().getValue()
    }

    def "validateYear 現在年より大きい場合は現在年を返す"() {
        given:
        def currentYear = Year.now().getValue()
        def futureYear = currentYear + 10

        when:
        def result = portfolioService.validateYear(futureYear)

        then:
        result == currentYear
    }

    def "validateYear 1900年より小さい場合は1900年を返す"() {
        given:
        def pastYear = 1800

        when:
        def result = portfolioService.validateYear(pastYear)

        then:
        result == 1900
    }

    def "validateYear 正常な年の場合はそのまま返す"() {
        given:
        def validYear = 2020

        when:
        def result = portfolioService.validateYear(validYear)

        then:
        result == validYear
    }
}