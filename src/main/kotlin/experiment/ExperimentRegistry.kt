package llmintro.experiment

/** command → Experiment 매핑 + 전체 실행 목록 관리. */
object ExperimentRegistry {
    val all: List<Experiment> = listOf(
        Exp0Hello(),
        Exp1Temperature(),
        Exp2Hallucination(),
        Exp3Context(),
        Exp4Accumulation(),
    )

    fun byCommand(command: String): Experiment? =
        all.firstOrNull { it.command == command }
}
