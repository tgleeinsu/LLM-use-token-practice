package llmintro.config

/** Anthropic 호출에 필요한 상수 한 곳. */
object AnthropicConfig {
    /** 전역 기본 모델 — 항상 가장 저렴한 Haiku 4.5 ($1 입력 / $5 출력 per MTok) */
    const val DEFAULT_MODEL = "claude-haiku-4-5"

    const val API_URL = "https://api.anthropic.com/v1/messages"
    const val ANTHROPIC_VERSION = "2023-06-01"
}
