package llmintro.experiment

import llmintro.client.AnthropicClient
import llmintro.util.hr

/** 실험 3 — 짧은 입력 vs 긴 잡음+입력 → input_tokens 배수 차이 */
class Exp3Context : Experiment {
    override val command = "exp3"
    override val description = "짧은 vs 긴 입력의 input_tokens 차이"

    override fun run(client: AnthropicClient, options: List<String>) {
        hr("exp3  컨텍스트 비용 (짧은 입력 vs 긴 잡음+입력)")
        val shortMsg = "안녕?"
        val noise = "이것은 의미 없는 긴 잡음 문장입니다. ".repeat(200)
        val longMsg = noise + "\n\n안녕?"

        val a = client.ask(shortMsg, maxTokens = 32)
        val b = client.ask(longMsg, maxTokens = 32)

        println("짧은  입력: input_tokens=${a.usage.inputTokens}")
        println("긴잡음 입력: input_tokens=${b.usage.inputTokens}")
        val ratio = if (a.usage.inputTokens > 0) b.usage.inputTokens.toDouble() / a.usage.inputTokens else 0.0
        println("배수: ${"%.1f".format(ratio)}x")
        println("👉 질문은 같아도 앞에 붙은 컨텍스트만큼 input_tokens 가 곱절 = 컨텍스트의 본질.")
    }
}
