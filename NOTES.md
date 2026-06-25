# 회고 1페이지 (실험하며 채우기)

> exp0~exp4 를 직접 돌려보고 관찰값을 적은 뒤, 마지막에 "다음 단계(tool_use)" 설계도로 잇는다.

## 빈칸 채우기
- LLM은 **__________________** 를 내놓는 함수다. *(힌트: 다음 토큰 확률분포)*

## 핵심 용어 6개 (한 줄씩)
| 용어 | 한 줄 |
| --- | --- |
| token |  |
| autoregressive |  |
| context window |  |
| hallucination |  |
| temperature/샘플링 |  |
| usage |  |

## 실측 기록
### exp1 — temperature 별 변동 (0/0.3/0.7/1.0)
| temp | 1회 | 2회 | 3회 | 관찰(고정↔다양) |
| --- | --- | --- | --- | --- |
| 0.0 |  |  |  |  |
| 0.3 |  |  |  |  |
| 0.7 |  |  |  |  |
| 1.0 |  |  |  |  |

### exp2 — 모델별 환각 차이
| model | 환각? 유보? | 메모 |
| --- | --- | --- |
| claude-haiku-4-5 |  |  |
| (비교 모델) |  |  |

### exp3 / exp4 — input_tokens
- exp3 짧은=____ / 긴잡음=____ → 배수 ____x
- exp4 턴별 input_tokens: 턴1=____ / 턴2=____ / 턴3=____

## 결론 → 다음 단계(tool_use) 설계도
- 출력이 분포라서 "한 번 호출"로는 품질 판단 불가 → **eval** 필요(챕터 4).
- API 는 stateless → 매 턴 history 통째 재전송 → input_tokens 누적.
- 이 누적이 곧 tool_use 의 `user→assistant(tool_use)→user(tool_result)` 누적과 같은 원리.
- 즉, exp4 에서 손으로 겪은 messages 누적이 **tool_use 4단계 루프**의 뼈대가 된다.
