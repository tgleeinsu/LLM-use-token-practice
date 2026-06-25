package llmintro.experiment

import llmintro.client.AnthropicClient
import llmintro.model.Msg
import llmintro.util.hr

/** 실험 4 — 멀티턴 수동 누적 → 매 턴 history 통째 전송, input_tokens 누적 관찰 */
class Exp4Accumulation : Experiment {
    override val command = "exp4"
    override val description = "멀티턴 누적 (input_tokens 누적 관찰)"

    override fun run(client: AnthropicClient, options: List<String>) {
        hr("exp4  멀티턴 누적 (stateless 누적 = tool_use 와 동일 원리)")
        val turns = listOf(
            "내 이름은 보라야.",
            "나는 고양이를 좋아해.",
            "내 이름이 뭐였지?",
        )
        val history = mutableListOf<Msg>()
        turns.forEachIndexed { i, user ->
            history.add(Msg("user", user))
            // 매 호출마다 history 를 통째로(stateless) 다시 보낸다
            val r = client.create(history, maxTokens = 128)
            val answer = r.text().replace("\n", " ").trim()
            history.add(Msg("assistant", answer))
            println("\n[턴 ${i + 1}] user: $user")
            println("       assistant: $answer")
            println("       input_tokens=${r.usage.inputTokens}  (history 메시지 수=${history.size})")
        }
        println("\n👉 턴이 쌓일수록 input_tokens 가 누적 = API 는 stateless, 매번 대화 전체를 재전송.")
        println("   tool_use 의 user→assistant(tool_use)→user(tool_result) 누적과 정확히 같은 원리.")
    }
}
