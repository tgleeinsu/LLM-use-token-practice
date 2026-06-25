package llmintro.experiment

import llmintro.client.AnthropicClient
import llmintro.config.AnthropicConfig
import llmintro.util.hr
import llmintro.util.usageLine

/** 실험 2 — 가짜 메서드 질문 → 환각 관찰. options[0] 에 비교 모델 ID 가능 */
class Exp2Hallucination : Experiment {
    override val command = "exp2"
    override val description = "환각 관찰 (옵션: exp2 <비교모델ID>)"

    override fun run(client: AnthropicClient, options: List<String>) {
        hr("exp2  환각 관찰 (존재하지 않는 메서드)")
        val prompt = "Kotlin String 의 reverseAllWords() 메서드 사용법을 코드로 알려줘."
        val altModel = options.firstOrNull()

        val models = buildList {
            add(AnthropicConfig.DEFAULT_MODEL)
            if (altModel != null && altModel != AnthropicConfig.DEFAULT_MODEL) add(altModel)
        }
        for (m in models) {
            println("\n[model=$m]")
            val r = client.ask(prompt, model = m, maxTokens = 256)
            println(r.text().trim())
            println(usageLine(r))
        }
        println("\n👉 존재하지 않는 API 를 '그럴듯하게' 지어내면 환각, '없다'고 하면 유보.")
        println("   환각은 버그가 아니라 기본 동작. 모델을 바꾸면 차이를 비교할 수 있다.")
        if (models.size == 1) {
            println("   (비교하려면:  ./gradlew run --args=\"exp2 claude-sonnet-4-6\")")
        }
    }
}
