package llmintro.experiment

import llmintro.client.AnthropicClient

interface Experiment {
    /** CLI 커맨드 이름 (예: "exp0") */
    val command: String

    /** 도움말에 보이는 한 줄 설명 */
    val description: String

    /** options: 커맨드 뒤에 붙은 추가 인자 (예: exp2 의 비교 모델 ID) */
    fun run(client: AnthropicClient, options: List<String> = emptyList())
}
