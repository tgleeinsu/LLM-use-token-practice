package llmintro

import llmintro.client.AnthropicClient
import llmintro.config.AnthropicConfig
import llmintro.config.ApiKeyLoader
import llmintro.config.MissingApiKeyException
import llmintro.experiment.ExperimentRegistry
import kotlin.system.exitProcess

/** 진입점: 인자 파싱 → 키 로딩 → 실험 디스패치. */
fun main(args: Array<String>) {
    val command = args.firstOrNull() ?: printUsage()
    if (command in setOf("-h", "--help", "help")) printUsage()  // 키 없이도 도움말은 보여준다

    val options = args.drop(1)
    val client = AnthropicClient(loadKeyOrExit())

    when (command) {
        "all" -> ExperimentRegistry.all.forEach { it.run(client, options) }
        else -> {
            val experiment = ExperimentRegistry.byCommand(command)
            if (experiment == null) {
                System.err.println("알 수 없는 커맨드: $command")
                printUsage()
            }
            experiment.run(client, options)
        }
    }
}

private fun loadKeyOrExit(): String = try {
    ApiKeyLoader.load()
} catch (e: MissingApiKeyException) {
    System.err.println(e.message)
    exitProcess(1)
}

private fun printUsage(): Nothing {
    println("사용법:  ./gradlew run --args=\"<command>\"\n")
    ExperimentRegistry.all.forEach {
        println("  ${it.command.padEnd(5)} ${it.description}")
    }
    println("  ${"all".padEnd(5)} exp0~exp4 순차 실행")
    println("\n모델: 기본 ${AnthropicConfig.DEFAULT_MODEL} (가장 저렴한 Haiku)")
    exitProcess(0)
}
