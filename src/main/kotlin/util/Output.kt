package llmintro.util

import llmintro.model.CreateResp

/** 콘솔 출력 포맷 헬퍼 (표현 책임). */

fun hr(title: String) = println("\n──────── $title ────────")

fun usageLine(r: CreateResp): String =
    "usage: input=${r.usage.inputTokens}  output=${r.usage.outputTokens}" +
        "  (model=${r.model}, stop=${r.stopReason})"
