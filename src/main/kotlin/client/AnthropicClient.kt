package llmintro.client

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import llmintro.config.AnthropicConfig
import llmintro.model.CreateReq
import llmintro.model.CreateResp
import llmintro.model.Msg
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Duration

/** Anthropic 생성 호출 한 곳. HTTP·직렬화만 책임진다. */
class AnthropicClient(private val apiKey: String) {

    private val json = Json {
        ignoreUnknownKeys = true   // 응답에 모르는 필드가 있어도 무시
        explicitNulls = false      // temperature=null 이면 JSON 에서 아예 뺀다
    }

    private val http = OkHttpClient.Builder()
        .callTimeout(Duration.ofSeconds(60))   // 기본 UX: 타임아웃
        .build()

    fun create(
        messages: List<Msg>,
        model: String = AnthropicConfig.DEFAULT_MODEL,
        maxTokens: Int = 256,
        system: String? = null,
        temperature: Double? = null,
    ): CreateResp {
        val req = CreateReq(model, maxTokens, messages, system, temperature)
        val body = json.encodeToString(req).toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(AnthropicConfig.API_URL)
            .header("x-api-key", apiKey)
            .header("anthropic-version", AnthropicConfig.ANTHROPIC_VERSION)
            .header("content-type", "application/json")
            .post(body)
            .build()

        http.newCall(request).execute().use { resp ->
            val raw = resp.body?.string().orEmpty()
            // 가드레일: 비-200 이면 에러 바디를 그대로 보여줘서 디버깅을 쉽게
            if (!resp.isSuccessful) error("API ${resp.code}\n$raw")
            return json.decodeFromString<CreateResp>(raw)
        }
    }

    /** 한 줄 사용자 메시지 단축 헬퍼 */
    fun ask(
        user: String,
        model: String = AnthropicConfig.DEFAULT_MODEL,
        maxTokens: Int = 256,
        temperature: Double? = null,
    ): CreateResp = create(listOf(Msg("user", user)), model, maxTokens, temperature = temperature)
}
