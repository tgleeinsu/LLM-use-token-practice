# 캡스톤 플래닝 — LLM 기초(챕터 1) CLI 앱

> 목표 저장소 `LLM-use-token-practice`의 **실험 4개 + 체크리스트 6개**를 100% 충족하는 학습용 CLI.
> 참고 저장소 `LLM-tool-use-rag-practice`에서는 **스택(Kotlin/OkHttp/Gradle)·키 관리·회고 방식**만 빌려오고, tool_use/RAG/Voyage/eval은 챕터 1 범위 밖이라 의도적으로 제외.

---

## 🎯 한 줄 정의

같은 입력으로 LLM을 여러 방식(temperature·model·context 길이)으로 호출하고
**`usage` 토큰 계량기로 관찰**해서, "LLM은 다음 토큰 확률분포를 내놓는 함수"임을 **실험으로 체감**하는 CLI.

## 🧩 부품 결정

| 부품 | 넣나? | 이유 |
| --- | --- | --- |
| LLM 생성 | ✅ | 챕터 1의 전부 — 단일 호출 + usage 관찰 |
| tool_use | ❌ | 챕터 2 범위. "최소 경로" 유지 위해 제외 |
| RAG | ❌ | 챕터 3 범위. 임베딩/벡터 불필요 |
| eval | ❌ | 챕터 4 범위. exp1 결론("1회 판단 불가→eval 필요")로 동기만 체감 |

키도 **`ANTHROPIC_API_KEY` 1개**만 사용 (Voyage 불필요).

---

## 🗺️ 아키텍처

```
[Kotlin CLI]  ← ANTHROPIC_API_KEY (.env 또는 env)
   └─ exp0~exp4 / all 서브커맨드
        └─ AnthropicClient.create(model, system, messages, temperature, maxTokens)
               → POST https://api.anthropic.com/v1/messages
               → 응답 text + usage{input_tokens, output_tokens} 출력
```

## 🔢 모델

- 기본·전역 모델: **Claude Haiku 4.5** (`claude-haiku-4-5`) — 입력 $1 / 출력 $5 per MTok, 최저가.
- Haiku 4.5는 `temperature`/`top_p` 지원 → exp1 temperature 스윕에 적합.
- exp2는 선택적으로 비교 모델 ID를 인자로 받을 수 있으나 **기본값은 항상 Haiku 4.5**.
- 전체 실험 수십 회 호출 → 예상 비용 **$1 미만**.

---

## 🧪 서브커맨드 = 실험 매핑 (체크리스트 흡수)

| 커맨드 | README 실험 | 하는 일 | 흡수 체크리스트 |
| --- | --- | --- | --- |
| `exp0` | 실험 0 | "안녕" 1회 호출 → 출력 + `usage` 계량기 | LLM=함수 / usage 이해 |
| `exp1` | 실험 1 | temp **0/0.3/0.7/1.0** 각 3회 → 같은 temp 내 변동 비교 | temperature 비교, "1회 판단 불가→eval 필요" |
| `exp2` | 실험 2 | 가짜 메서드 질문 → 환각 관찰. 인자로 모델 교체 비교 | kModel 바꿔 환각 차이 기록 |
| `exp3` | 실험 3 | 짧은 "안녕?" vs 긴 잡음+"안녕?" → `input_tokens` 배수 | 컨텍스트가 비싼 이유 |
| `exp4` | (체크리스트) | 멀티턴 수동 누적 → 매 턴 history 통째 전송, `input_tokens` 누적 관찰 | messages 누적→input_tokens, "tool_use stateless 누적과 동일 원리" |
| `all` | — | exp0~exp4 순차 실행 | — |

> `exp4` 분리 이유: README 실험 3(긴 단일 입력)과 체크리스트의 "턴 쌓아 누적"은 다른 현상 → 둘 다 체크. 이게 **다음 단계(tool_use)의 stateless 누적과 같은 원리**라 회고 연결고리로 사용.

---

## 📁 구조 (단일 파일 + 서브커맨드)

```
LLM-use-token-practice/
├── build.gradle.kts          # OkHttp + kotlinx.serialization + application plugin
├── settings.gradle.kts
├── .env.example              # ANTHROPIC_API_KEY=sk-ant-...
├── .gitignore                # .env, build/, .gradle/
├── src/main/kotlin/
│   └── LlmIntro.kt           # ★ 전부 한 파일: main(when), AnthropicClient, 데이터클래스, exp0~4
└── NOTES.md                  # 회고 1페이지 (관찰 기록 → 다음단계 설계도)
```

`LlmIntro.kt` 내부 구역:
1. 데이터 클래스 — `Msg`, `CreateReq`, `CreateResp`, `Usage` (kotlinx.serialization)
2. AnthropicClient — `create(...)` (OkHttp, 타임아웃, 비-200 에러 바디 표면화)
3. 공용 헬퍼 — `loadApiKey()`(env→.env), `printUsage()`
4. exp0~exp4 함수
5. main — `when(args[0])` 분기 + 사용법

## ✅ 기본 UX / 가드레일
- 키 없으면 친절한 안내 후 종료 (하드코딩·커밋 금지)
- HTTP 타임아웃 + 비-200이면 **에러 바디 그대로 출력**
- `max_tokens` 필수값 명시, 잘림 시 `stop_reason` 표시

## ▶️ 실행
```bash
export ANTHROPIC_API_KEY="sk-ant-..."   # 또는 .env 파일
./gradlew run --args="exp0"
./gradlew run --args="exp1"
./gradlew run --args="all"
```

## 📝 NOTES.md (회고 1페이지) 골자
- "LLM은 **다음 토큰 확률분포**를 내놓는 함수다" 빈칸
- 용어 6개 한 줄 정의
- exp1/exp2 실측 표(temp별 변동, 모델별 환각)
- exp3/exp4 `input_tokens` 수치 → "컨텍스트=매번 통째 재전송"
- **다음 단계(tool_use) 설계도**: stateless 누적을 손으로 겪음 → 4단계 루프로 연결
