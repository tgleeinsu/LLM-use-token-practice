package llmintro.experiment

import llmintro.client.AnthropicClient
import llmintro.util.hr
import llmintro.util.prompt

/** 실험 1 — temperature 0/0.3/0.7/1.0, 각 3회 → 같은 temp 내 변동 비교 */
class Exp1Temperature : Experiment {
    override val command = "exp1"
    override val description = "temperature 0/0.3/0.7/1.0 비교"

    override fun run(client: AnthropicClient, options: List<String>) {
        hr("exp1  temperature 0/0.3/0.7/1.0 (각 3회)")
        val question = prompt("질문을 입력하세요.", default = "가을을 딱 두 단어로 표현해줘. 단어만.")
        for (temp in listOf(0.0, 0.3, 0.7, 1.0)) {
            println("\n[temperature=$temp]")
            repeat(3) { i ->
                val r = client.ask(question, maxTokens = 32, temperature = temp)
                println("  ${i + 1}) ${r.text().replace("\n", " ").trim()}")
            }
        }
        println("\n👉 temp=0 은 거의 고정, temp=1 은 매번 다름 = 출력은 '분포'.")
        println("   한 번 테스트로 좋다/나쁘다 판단 불가 → 다음 단계에서 eval 이 필요해짐.")
    }
}
