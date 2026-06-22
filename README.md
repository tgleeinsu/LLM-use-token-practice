# LLM-use-token-practice

## 🎯 목표

이번 주 끝나면 이걸 할 수 있다.

- "LLM은 확률분포 함수"를 내 말로 설명
- 핵심 용어 6개 정의 + 호출 파라미터가 뭘 바꾸는지 이해
- 출력이 분포임을(temperature) / 환각이 기본 동작임을 / 긴 대화가 비싼 이유를 **실험으로 확인**

---

## 🧩 핵심 용어 6개

| 용어 | 한 줄 |
| --- | --- |
| **token** | 입출력 최소 단위(subword). 비용·컨텍스트의 단위. 한국어가 더 먹음 |
| **autoregressive** | 토큰 하나 뽑고 붙이고 반복 → 출력 길수록 느림 |
| **context window** | 매 호출마다 대화 전체를 다시 넣는 입력 공간 → 긴 대화 = 비쌈 |
| **hallucination** | "참"이 아니라 "그럴듯함"을 내놓음. 버그 아닌 기본 동작 |
| **temperature/샘플링** | 분포에서 고르는 방식. 0=고정, 높을수록 다양 |
| **usage** | API가 주는 토큰 계량기(input/output) |

---

## ⚙️ 호출 파라미터

**기본**

- `model` — 어떤 모델 (품질·환각·비용·속도가 달라짐)
- `messages` — 대화 배열. **히스토리를 매번 통째로** 보냄(stateless)
- `max_tokens` — 출력 상한. Anthropic은 **필수**. 작으면 잘림, 크면 느려질 수 있음
- `system` — 시스템 프롬프트. Anthropic은 messages가 아닌 **별도 최상위 필드**

**무작위성 (보통 하나만 만진다)**

- `temperature` — 분포를 뾰족/평평하게. 창의성↔일관성 손잡이
- `top_p` — 누적확률 상위 토큰만 후보
- `top_k` — 확률 상위 k개만 후보
- 👉 **시작은 `temperature` 하나만.** 같이 세게 만지면 해석 불가

**그 외**

- `stop_sequences` 생성 중단 / `stream` 실시간 / `tools` → 다음 단계(tool_use) 연결고리

---

## 🧪 실험 4개 (`dart run llm_intro.dart`)

> 키는 환경변수로: `export ANTHROPIC_API_KEY="sk-ant-..."`
> 
- **실험 0** 입력→출력 + `usage` 계량기 확인
- **실험 1** temp 0.0(거의 동일) vs 1.0(매번 다름) → "한 번 테스트로 판단 불가, eval 필요"
- **실험 2** 가짜 메서드 질문 → 환각 vs 유보 관찰 (모델 바꿔 비교)
- **실험 3** 짧은 "안녕?" vs 긴 잡음+"안녕?" → input_tokens 몇 배 차이 = 컨텍스트 본질

---

## ✅ 체크리스트

- [ ]  "LLM은 ___를 내놓는 함수다" 빈칸 채우기
- [ ]  용어 6개 한 줄씩 설명
- [ ]  temperature를 0/0.3/0.7/1.0로 바꿔 출력 비교
- [ ]  kModel 바꿔 환각 차이 기록
- [ ]  messages에 턴 쌓아 input_tokens 누적 관찰 *(= tool_use의 stateless 누적과 같은 원리)*
- [ ]  관찰을 1페이지로 정리 → 다음 단계(tool_use) 설계도

---

## ➡️ 다음

**1주차(LLM 기초) → tool_use → MCP/실제 호스트** 순서.
`messages` 누적을 손으로 겪어두면 tool_use가 훨씬 쉽다.
