package llmintro.util

import llmintro.model.CreateResp

/** 콘솔 출력 포맷 헬퍼 (표현 책임). */

fun hr(title: String) = println("\n──────── $title ────────")

/** 한 줄 입력받기. 비워서 엔터 치면 기본값 사용. */
fun prompt(label: String, default: String): String {
    print("$label\n  (엔터만 치면 기본값: \"$default\")\n  > ")
    System.out.flush()
    val line = readlnOrNull()?.trim()
    return if (line.isNullOrEmpty()) default else line
}

/** 정수 입력받기. 비우거나 숫자가 아니면 기본값 사용. */
fun promptInt(label: String, default: Int): Int {
    print("$label\n  (엔터만 치면 기본값: $default)\n  > ")
    System.out.flush()
    val line = readlnOrNull()?.trim()
    return line?.toIntOrNull() ?: default
}

/** 빈 줄 입력 전까지 여러 줄 입력받기. 아무것도 안 넣으면 기본 목록 사용. */
fun promptLines(label: String, default: List<String>): List<String> {
    println(label)
    println("  (한 줄에 하나씩 입력, 빈 줄 엔터로 종료. 아무것도 안 넣으면 기본값 사용)")
    val lines = mutableListOf<String>()
    while (true) {
        print("  ${lines.size + 1}> ")
        System.out.flush()
        val line = readlnOrNull()?.trim()
        if (line.isNullOrEmpty()) break
        lines.add(line)
    }
    return lines.ifEmpty { default }
}

fun usageLine(r: CreateResp): String =
    "usage: input=${r.usage.inputTokens}  output=${r.usage.outputTokens}" +
        "  (model=${r.model}, stop=${r.stopReason})"
