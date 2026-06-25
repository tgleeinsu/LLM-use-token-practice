package llmintro.config

import java.io.File

/** ANTHROPIC_API_KEY 를 환경변수 우선, 없으면 프로젝트 루트 .env 에서 읽는다. */
object ApiKeyLoader {
    private const val KEY = "ANTHROPIC_API_KEY"
    private const val PLACEHOLDER = "sk-ant-..."

    /** @throws MissingApiKeyException 키를 못 찾으면 */
    fun load(): String {
        System.getenv(KEY)?.takeIf { it.isNotBlank() }?.let { return it }
        readFromDotenv()?.let { return it }
        throw MissingApiKeyException()
    }

    private fun readFromDotenv(): String? {
        val dotenv = File(".env")
        if (!dotenv.exists()) return null
        return dotenv.readLines()
            .map { it.trim() }
            .firstOrNull { it.startsWith("$KEY=") }
            ?.substringAfter("=")
            ?.trim()
            ?.removeSurrounding("\"")
            ?.takeIf { it.isNotBlank() && it != PLACEHOLDER }
    }
}

/** 키가 없을 때의 도메인 예외 — 종료 처리는 호출자(Main)의 책임으로 남긴다. */
class MissingApiKeyException : RuntimeException(
    """
    ❌ ANTHROPIC_API_KEY 가 없습니다.
       1) export ANTHROPIC_API_KEY="sk-ant-..."   또는
       2) cp .env.example .env  후 실제 키 입력
    (키는 하드코딩·커밋 금지)
    """.trimIndent()
)
