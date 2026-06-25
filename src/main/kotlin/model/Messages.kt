package llmintro.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Anthropic Messages API 의 요청/응답 JSON 모양. */

@Serializable
data class Msg(val role: String, val content: String)

@Serializable
data class CreateReq(
    val model: String,
    @SerialName("max_tokens") val maxTokens: Int,
    val messages: List<Msg>,
    val system: String? = null,
    val temperature: Double? = null,
)

@Serializable
data class TextBlock(val type: String = "text", val text: String = "")

@Serializable
data class Usage(
    @SerialName("input_tokens") val inputTokens: Int,
    @SerialName("output_tokens") val outputTokens: Int,
)

@Serializable
data class CreateResp(
    val content: List<TextBlock> = emptyList(),
    val usage: Usage,
    @SerialName("stop_reason") val stopReason: String? = null,
    val model: String = "",
) {
    /** 첫 text 블록만 꺼낸다 (챕터 1 에선 text 하나면 충분). */
    fun text(): String = content.firstOrNull { it.type == "text" }?.text ?: ""
}
