package com.example.visuasset.controller

import com.example.visuasset.entity.MonthlyAssets
import com.example.visuasset.service.MonthlyAssetsService
import org.springframework.ui.Model
import spock.lang.Specification

class MonthlyAssetsControllerSpec extends Specification {

    def service = Mock(MonthlyAssetsService)
    def controller = new MonthlyAssetsController(service)
    def model = Mock(Model)

    def "monthly targetYearが指定されていない場合、現在年を使用する"() {
        given:
        def currentYear = java.time.LocalDate.now().getYear()
        def monthlyAssetsList = []
        
        when:
        def result = controller.monthly(null, model)
        
        then:
        1 * service.getAssetsByYear(currentYear) >> monthlyAssetsList
        1 * service.getCashList(monthlyAssetsList) >> []
        1 * service.getSecuritiesList(monthlyAssetsList) >> []
        1 * service.getCryptoList(monthlyAssetsList) >> []
        1 * service.getMonthLabels(monthlyAssetsList) >> []
        1 * model.addAttribute("targetYear", currentYear)
        1 * model.addAttribute("cashList", [])
        1 * model.addAttribute("securitiesList", [])
        1 * model.addAttribute("cryptoList", [])
        1 * model.addAttribute("labels", [])
        result == "monthly"
    }

    def "monthly targetYearが指定された場合、指定された年を使用する"() {
        given:
        def targetYear = 2023
        def monthlyAssetsList = []
        
        when:
        def result = controller.monthly(targetYear, model)
        
        then:
        1 * service.getAssetsByYear(targetYear) >> monthlyAssetsList
        1 * service.getCashList(monthlyAssetsList) >> []
        1 * service.getSecuritiesList(monthlyAssetsList) >> []
        1 * service.getCryptoList(monthlyAssetsList) >> []
        1 * service.getMonthLabels(monthlyAssetsList) >> []
        1 * model.addAttribute("targetYear", targetYear)
        1 * model.addAttribute("cashList", [])
        1 * model.addAttribute("securitiesList", [])
        1 * model.addAttribute("cryptoList", [])
        1 * model.addAttribute("labels", [])
        result == "monthly"
    }

    def "monthly targetYearが未来年の場合、現在年を使用する"() {
        given:
        def currentYear = java.time.LocalDate.now().getYear()
        def futureYear = currentYear + 1
        def monthlyAssetsList = []
        
        when:
        def result = controller.monthly(futureYear, model)
        
        then:
        1 * service.getAssetsByYear(currentYear) >> monthlyAssetsList
        1 * service.getCashList(monthlyAssetsList) >> []
        1 * service.getSecuritiesList(monthlyAssetsList) >> []
        1 * service.getCryptoList(monthlyAssetsList) >> []
        1 * service.getMonthLabels(monthlyAssetsList) >> []
        1 * model.addAttribute("targetYear", currentYear)
        1 * model.addAttribute("cashList", [])
        1 * model.addAttribute("securitiesList", [])
        1 * model.addAttribute("cryptoList", [])
        1 * model.addAttribute("labels", [])
        result == "monthly"
    }
}
