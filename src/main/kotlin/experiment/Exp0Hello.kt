package llmintro.experiment

import llmintro.client.AnthropicClient
import llmintro.util.hr
import llmintro.util.prompt
import llmintro.util.usageLine

/** 실험 0 — 입력→출력 + usage 계량기 확인 */
class Exp0Hello : Experiment {
    override val command = "exp0"
    override val description = "입력→출력 + usage 계량기"

    override fun run(client: AnthropicClient, options: List<String>) {
        hr("exp0  입력→출력 + usage 계량기")
        val question = prompt("질문을 입력하세요.", default = "한 문장으로 인사해줘.")
        val r = client.ask(question)
        println("출력: ${r.text()}")
        println(usageLine(r))
        println("👉 LLM 호출 = 입력을 넣으면 토큰을 하나씩 뽑아 출력. usage 가 비용의 단위.")
    }
}
