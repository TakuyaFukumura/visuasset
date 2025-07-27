package com.example.visuasset.service

import com.example.visuasset.dto.SimulationResult
import com.example.visuasset.entity.SimulationCondition
import com.example.visuasset.entity.YearlyAssets
import com.example.visuasset.repository.SimulationConditionRepository
import com.example.visuasset.repository.YearlyAssetsRepository
import spock.lang.Specification

import java.time.LocalDate

class SimulationServiceSpec extends Specification {

    def simulationConditionRepository = Mock(SimulationConditionRepository)
    def yearlyAssetsRepository = Mock(YearlyAssetsRepository)
    def service = new SimulationService(simulationConditionRepository, yearlyAssetsRepository)

    def "saveSimulationCondition シミュレーション条件を保存して返却する"() {
        given:
        def condition = new SimulationCondition(
                conditionName: "テスト条件",
                monthlyInvestment: 50000 as BigDecimal,
                annualReturnRate: 0.05 as BigDecimal,
                investmentPeriodYears: 10,
                initialAmount: 1000000 as BigDecimal
        )
        def savedCondition = new SimulationCondition(
                id: 1L,
                conditionName: "テスト条件",
                monthlyInvestment: 50000 as BigDecimal,
                annualReturnRate: 0.05 as BigDecimal,
                investmentPeriodYears: 10,
                initialAmount: 1000000 as BigDecimal
        )

        when:
        def result = service.saveSimulationCondition(condition)

        then:
        1 * simulationConditionRepository.save(condition) >> savedCondition
        result == savedCondition
    }

    def "getAllSimulationConditions 全ての条件を返却する"() {
        given:
        def conditions = [
                new SimulationCondition(id: 1L, conditionName: "条件1"),
                new SimulationCondition(id: 2L, conditionName: "条件2")
        ]

        when:
        def result = service.getAllSimulationConditions()

        then:
        1 * simulationConditionRepository.findAll() >> conditions
        result == conditions
    }

    def "getSimulationConditionById 存在する条件をIDで取得する"() {
        given:
        def id = 1L
        def condition = new SimulationCondition(id: id, conditionName: "テスト条件")

        when:
        def result = service.getSimulationConditionById(id)

        then:
        1 * simulationConditionRepository.findById(id) >> Optional.of(condition)
        result.isPresent()
        result.get() == condition
    }

    def "getSimulationConditionById 存在しない場合は空を返却する"() {
        given:
        def id = 999L

        when:
        def result = service.getSimulationConditionById(id)

        then:
        1 * simulationConditionRepository.findById(id) >> Optional.empty()
        !result.isPresent()
    }

    def "deleteSimulationCondition リポジトリの削除を呼び出す"() {
        given:
        def id = 1L

        when:
        service.deleteSimulationCondition(id)

        then:
        1 * simulationConditionRepository.deleteById(id)
    }

    def "getCurrentTotalAssets 現在年の総資産額を返却する"() {
        given:
        def currentYear = LocalDate.now().getYear()
        def yearlyAssets = new YearlyAssets(
                targetYear: currentYear,
                cash: 1000000 as BigDecimal,
                securities: 2000000 as BigDecimal,
                crypto: 500000 as BigDecimal
        )

        when:
        def result = service.getCurrentTotalAssets()

        then:
        1 * yearlyAssetsRepository.findByTargetYear(currentYear) >> Optional.of(yearlyAssets)
        result == 3500000
    }

    def "getCurrentTotalAssets データが見つからない場合はゼロを返却する"() {
        given:
        def currentYear = LocalDate.now().getYear()

        when:
        def result = service.getCurrentTotalAssets()

        then:
        (1..11) * yearlyAssetsRepository.findByTargetYear(_ as Integer) >> Optional.empty()
        result == BigDecimal.ZERO
    }

    def "getCurrentTotalAssets 今年のデータがない場合は過去年のデータを取得する"() {
        given:
        def currentYear = LocalDate.now().getYear()
        def lastYear = currentYear - 1
        def yearlyAssets = new YearlyAssets(
                targetYear: lastYear,
                cash: 800000 as BigDecimal,
                securities: 1500000 as BigDecimal,
                crypto: 300000 as BigDecimal
        )

        when:
        def result = service.getCurrentTotalAssets()

        then:
        1 * yearlyAssetsRepository.findByTargetYear(currentYear) >> Optional.empty()
        1 * yearlyAssetsRepository.findByTargetYear(lastYear) >> Optional.of(yearlyAssets)
        result == 2600000
    }

    def "runSimulation 複利計算を正しく実行する"() {
        given:
        def condition = new SimulationCondition(
                initialAmount: 1000000 as BigDecimal,
                monthlyInvestment: 100000 as BigDecimal,
                annualReturnRate: 0.12 as BigDecimal, // 12% annual = 1% monthly
                investmentPeriodYears: 1 // 12 months
        )

        when:
        def results = service.runSimulation(condition)

        then:
        results.size() == 13 // 初期 + 12ヶ月

        // 初期状態
        results[0].month == 0
        results[0].totalAmount == 1000000
        results[0].investedAmount == 1000000
        results[0].returnAmount == BigDecimal.ZERO

        // 1ヶ月後: (1,000,000 * 1.01) + 100,000 = 1,110,000
        results[1].month == 1
        results[1].totalAmount == 1110000.00
        results[1].investedAmount == 1100000
        results[1].returnAmount == 10000.00

        // 12ヶ月後の計算確認（複利効果）
        results[12].month == 12
        results[12].investedAmount == 2200000 // 1,000,000 + (100,000 * 12)
        results[12].returnAmount > 0 // 運用利益が発生している
        results[12].totalAmount > results[12].investedAmount // 総資産 > 投資元本
    }

    def "runSimulation 毎月投資額ゼロを処理する"() {
        given:
        def condition = new SimulationCondition(
                initialAmount: 1000000 as BigDecimal,
                monthlyInvestment: BigDecimal.ZERO,
                annualReturnRate: 0.12 as BigDecimal,
                investmentPeriodYears: 1
        )

        when:
        def results = service.runSimulation(condition)

        then:
        results.size() == 13
        results[12].investedAmount == 1000000 // 初期投資額のみ
        results[12].totalAmount > 1000000 // 運用利益で増加
    }

    def "runSimulation 利回りゼロを処理する"() {
        given:
        def condition = new SimulationCondition(
                initialAmount: 1000000 as BigDecimal,
                monthlyInvestment: 50000 as BigDecimal,
                annualReturnRate: BigDecimal.ZERO,
                investmentPeriodYears: 1
        )

        when:
        def results = service.runSimulation(condition)

        then:
        results.size() == 13
        results[12].investedAmount == 1600000 // 1,000,000 + (50,000 * 12)
        results[12].totalAmount == 1600000 // 運用利益なし
        results[12].returnAmount == BigDecimal.ZERO
    }

    def "runSimulation 初期額がnullの場合は現在の総資産額を使用する"() {
        given:
        def condition = new SimulationCondition(
                initialAmount: null,
                monthlyInvestment: 50000 as BigDecimal,
                annualReturnRate: 0.05 as BigDecimal,
                investmentPeriodYears: 1
        )
        def currentYear = LocalDate.now().getYear()
        def yearlyAssets = new YearlyAssets(
                targetYear: currentYear,
                cash: 500000 as BigDecimal,
                securities: 300000 as BigDecimal,
                crypto: 200000 as BigDecimal
        )

        when:
        def results = service.runSimulation(condition)

        then:
        1 * yearlyAssetsRepository.findByTargetYear(currentYear) >> Optional.of(yearlyAssets)
        results[0].totalAmount == 1000000 // 500,000 + 300,000 + 200,000
        results[0].investedAmount == 1000000
    }

    def "getMonthLabels 正しいラベルを返却する"() {
        given:
        def results = [
                new SimulationResult(0, null, null, null),
                new SimulationResult(1, null, null, null),
                new SimulationResult(6, null, null, null),
                new SimulationResult(12, null, null, null),
                new SimulationResult(24, null, null, null)
        ]

        when:
        def labels = service.getMonthLabels(results)

        then:
        labels == ["開始", "1ヶ月", "6ヶ月", "1年", "2年"]
    }

    def "getTotalAmountList 総資産額のリストを返却する"() {
        given:
        def results = [
                new SimulationResult(0, 1000000 as BigDecimal, null, null),
                new SimulationResult(1, 1100000 as BigDecimal, null, null),
                new SimulationResult(2, 1200000 as BigDecimal, null, null)
        ]

        when:
        def amounts = service.getTotalAmountList(results)

        then:
        amounts == [1000000 as BigDecimal, 1100000 as BigDecimal, 1200000 as BigDecimal]
    }

    def "getInvestedAmountList 投資元本のリストを返却する"() {
        given:
        def results = [
                new SimulationResult(0, null, 1000000 as BigDecimal, null),
                new SimulationResult(1, null, 1100000 as BigDecimal, null),
                new SimulationResult(2, null, 1200000 as BigDecimal, null)
        ]

        when:
        def amounts = service.getInvestedAmountList(results)

        then:
        amounts == [1000000 as BigDecimal, 1100000 as BigDecimal, 1200000 as BigDecimal]
    }

    def "getReturnAmountList 運用利益のリストを返却する"() {
        given:
        def results = [
                new SimulationResult(0, null, null, BigDecimal.ZERO),
                new SimulationResult(1, null, null, 10000 as BigDecimal),
                new SimulationResult(2, null, null, 25000 as BigDecimal)
        ]

        when:
        def amounts = service.getReturnAmountList(results)

        then:
        amounts == [BigDecimal.ZERO, 10000 as BigDecimal, 25000 as BigDecimal]
    }
}
