package com.example.visuasset.controller

import com.example.visuasset.dto.SimulationResult
import com.example.visuasset.entity.SimulationCondition
import com.example.visuasset.service.SimulationService
import org.springframework.ui.Model
import spock.lang.Specification

class SimulationControllerSpec extends Specification {

    def service = Mock(SimulationService)
    def controller = new SimulationController(service)
    def model = Mock(Model)

    def "simulation デフォルト条件でページを表示してシミュレーションを実行する"() {
        given:
        def currentTotalAssets = 10000000 as BigDecimal
        def simulationResults = [
                new SimulationResult(0, 10000000 as BigDecimal, 10000000 as BigDecimal, BigDecimal.ZERO),
                new SimulationResult(12, 12000000 as BigDecimal, 11000000 as BigDecimal, 1000000 as BigDecimal)
        ]
        def savedConditions = [
                new SimulationCondition(id: 1L, conditionName: "保存済み条件1")
        ]
        def monthLabels = ["開始", "1年"]
        def totalAmountList = [10000000 as BigDecimal, 12000000 as BigDecimal]
        def investedAmountList = [10000000 as BigDecimal, 11000000 as BigDecimal]
        def returnAmountList = [BigDecimal.ZERO, 1000000 as BigDecimal]

        when:
        def result = controller.simulation(model)

        then:
        1 * service.getCurrentTotalAssets() >> currentTotalAssets
        1 * service.getAllSimulationConditions() >> savedConditions
        1 * service.runSimulation(_ as SimulationCondition) >> simulationResults
        1 * service.getMonthLabels(simulationResults) >> monthLabels
        1 * service.getTotalAmountList(simulationResults) >> totalAmountList
        1 * service.getInvestedAmountList(simulationResults) >> investedAmountList
        1 * service.getReturnAmountList(simulationResults) >> returnAmountList

        1 * model.addAttribute("simulationCondition", { SimulationCondition condition ->
            condition.conditionName == "デフォルト条件" &&
                    condition.monthlyInvestment == 70000 as BigDecimal &&
                    condition.annualReturnRate == 0.05 as BigDecimal &&
                    condition.investmentPeriodYears == 20 &&
                    condition.initialAmount == currentTotalAssets
        })
        1 * model.addAttribute("savedConditions", savedConditions)
        1 * model.addAttribute("simulationResults", simulationResults)
        1 * model.addAttribute("monthLabels", monthLabels)
        1 * model.addAttribute("totalAmountList", totalAmountList)
        1 * model.addAttribute("investedAmountList", investedAmountList)
        1 * model.addAttribute("returnAmountList", returnAmountList)
        1 * model.addAttribute("finalTotalAmount", 12000000 as BigDecimal)
        1 * model.addAttribute("finalInvestedAmount", 11000000 as BigDecimal)
        1 * model.addAttribute("finalReturnAmount", 1000000 as BigDecimal)
        1 * model.addAttribute("returnPercentage", { BigDecimal percentage ->
            percentage.compareTo(9.09 as BigDecimal) == 0 // 1000000/11000000 * 100 = 9.09
        })

        result == "simulation"
    }

    def "runSimulation 指定された条件でシミュレーションを実行する"() {
        given:
        def simulationCondition = new SimulationCondition(
                conditionName: "カスタム条件",
                monthlyInvestment: 80000 as BigDecimal,
                annualReturnRate: 0.06 as BigDecimal,
                investmentPeriodYears: 15,
                initialAmount: 5000000 as BigDecimal
        )
        def simulationResults = [
                new SimulationResult(0, 5000000 as BigDecimal, 5000000 as BigDecimal, BigDecimal.ZERO),
                new SimulationResult(180, 20000000 as BigDecimal, 19400000 as BigDecimal, 600000 as BigDecimal)
        ]
        def savedConditions = []

        when:
        def result = controller.runSimulation(simulationCondition, model)

        then:
        1 * service.runSimulation(simulationCondition) >> simulationResults
        1 * service.getAllSimulationConditions() >> savedConditions
        1 * service.getMonthLabels(simulationResults) >> []
        1 * service.getTotalAmountList(simulationResults) >> []
        1 * service.getInvestedAmountList(simulationResults) >> []
        1 * service.getReturnAmountList(simulationResults) >> []

        1 * model.addAttribute("simulationCondition", simulationCondition)
        1 * model.addAttribute("savedConditions", savedConditions)
        1 * model.addAttribute("simulationResults", simulationResults)

        result == "simulation"
    }

    def "runSimulation 初期額がnullの場合は現在の総資産額を使用する"() {
        given:
        def currentTotalAssets = 8000000 as BigDecimal
        def simulationCondition = new SimulationCondition(
                conditionName: "条件",
                monthlyInvestment: 50000 as BigDecimal,
                annualReturnRate: 0.04 as BigDecimal,
                investmentPeriodYears: 10,
                initialAmount: null
        )
        def simulationResults = []

        when:
        controller.runSimulation(simulationCondition, model)

        then:
        1 * service.getCurrentTotalAssets() >> currentTotalAssets
        simulationCondition.initialAmount == currentTotalAssets
        1 * service.runSimulation(simulationCondition) >> simulationResults
        1 * service.getAllSimulationConditions() >> []
        1 * service.getMonthLabels(simulationResults) >> []
        1 * service.getTotalAmountList(simulationResults) >> []
        1 * service.getInvestedAmountList(simulationResults) >> []
        1 * service.getReturnAmountList(simulationResults) >> []
    }

    def "saveSimulationCondition 条件を保存してリダイレクトする"() {
        given:
        def currentTotalAssets = 6000000 as BigDecimal
        def simulationCondition = new SimulationCondition(
                conditionName: "新しい条件",
                monthlyInvestment: 60000 as BigDecimal,
                annualReturnRate: 0.07 as BigDecimal,
                investmentPeriodYears: 25,
                initialAmount: null
        )

        when:
        def result = controller.saveSimulationCondition(simulationCondition)

        then:
        1 * service.getCurrentTotalAssets() >> currentTotalAssets
        simulationCondition.initialAmount == currentTotalAssets
        1 * service.saveSimulationCondition(simulationCondition)

        result == "redirect:/simulation"
    }

    def "saveSimulationCondition 既存の初期額を上書きしない"() {
        given:
        def existingInitialAmount = 15000000 as BigDecimal
        def simulationCondition = new SimulationCondition(
                conditionName: "既存条件",
                monthlyInvestment: 40000 as BigDecimal,
                annualReturnRate: 0.03 as BigDecimal,
                investmentPeriodYears: 30,
                initialAmount: existingInitialAmount
        )

        when:
        def result = controller.saveSimulationCondition(simulationCondition)

        then:
        0 * service.getCurrentTotalAssets()
        simulationCondition.initialAmount == existingInitialAmount
        1 * service.saveSimulationCondition(simulationCondition)

        result == "redirect:/simulation"
    }

    def "loadSimulationCondition 条件を読み込んでシミュレーションを実行する"() {
        given:
        def conditionId = 1L
        def savedCondition = new SimulationCondition(
                id: conditionId,
                conditionName: "保存済み条件",
                monthlyInvestment: 90000 as BigDecimal,
                annualReturnRate: 0.08 as BigDecimal,
                investmentPeriodYears: 12,
                initialAmount: 5000000 as BigDecimal // 保存された初期金額をそのまま使用
        )
        def simulationResults = [
                new SimulationResult(0, 5000000 as BigDecimal, 5000000 as BigDecimal, BigDecimal.ZERO)
        ]
        def allConditions = [savedCondition]

        when:
        def result = controller.loadSimulationCondition(conditionId, model)

        then:
        1 * service.getSimulationConditionById(conditionId) >> Optional.of(savedCondition)
        savedCondition.initialAmount == 5000000 as BigDecimal // 保存された初期金額をそのまま使用
        1 * service.runSimulation(savedCondition) >> simulationResults
        1 * service.getAllSimulationConditions() >> allConditions
        1 * service.getMonthLabels(simulationResults) >> []
        1 * service.getTotalAmountList(simulationResults) >> []
        1 * service.getInvestedAmountList(simulationResults) >> []
        1 * service.getReturnAmountList(simulationResults) >> []

        1 * model.addAttribute("simulationCondition", savedCondition)
        1 * model.addAttribute("savedConditions", allConditions)

        result == "simulation"
    }

    def "loadSimulationCondition 条件が見つからない場合は例外をスローする"() {
        given:
        def conditionId = 999L

        when:
        controller.loadSimulationCondition(conditionId, model)

        then:
        1 * service.getSimulationConditionById(conditionId) >> Optional.empty()
        thrown(IllegalArgumentException)
    }

    def "deleteSimulationCondition 条件を削除してリダイレクトする"() {
        given:
        def conditionId = 1L

        when:
        def result = controller.deleteSimulationCondition(conditionId)

        then:
        1 * service.deleteSimulationCondition(conditionId)

        result == "redirect:/simulation"
    }

    def "simulation 利回り率ゼロを正しく処理する"() {
        given:
        def currentTotalAssets = 1000000 as BigDecimal
        def simulationResults = [
                new SimulationResult(0, 1000000 as BigDecimal, 1000000 as BigDecimal, BigDecimal.ZERO),
                new SimulationResult(12, 1600000 as BigDecimal, BigDecimal.ZERO, 1600000 as BigDecimal)
        ]

        when:
        controller.simulation(model)

        then:
        1 * service.getCurrentTotalAssets() >> currentTotalAssets
        1 * service.getAllSimulationConditions() >> []
        1 * service.runSimulation(_ as SimulationCondition) >> simulationResults
        1 * service.getMonthLabels(simulationResults) >> []
        1 * service.getTotalAmountList(simulationResults) >> []
        1 * service.getInvestedAmountList(simulationResults) >> []
        1 * service.getReturnAmountList(simulationResults) >> []

        1 * model.addAttribute("returnPercentage", BigDecimal.ZERO) // ゼロ除算を避ける
    }

    def "simulation 空の結果を正しく処理する"() {
        given:
        def currentTotalAssets = 1000000 as BigDecimal
        def simulationResults = []

        when:
        controller.simulation(model)

        then:
        1 * service.getCurrentTotalAssets() >> currentTotalAssets
        1 * service.getAllSimulationConditions() >> []
        1 * service.runSimulation(_ as SimulationCondition) >> simulationResults
        1 * service.getMonthLabels(simulationResults) >> []
        1 * service.getTotalAmountList(simulationResults) >> []
        1 * service.getInvestedAmountList(simulationResults) >> []
        1 * service.getReturnAmountList(simulationResults) >> []

        1 * model.addAttribute("simulationResults", simulationResults)
        0 * model.addAttribute("finalTotalAmount", _)
        0 * model.addAttribute("finalInvestedAmount", _)
        0 * model.addAttribute("finalReturnAmount", _)
        0 * model.addAttribute("returnPercentage", _)
    }
}
