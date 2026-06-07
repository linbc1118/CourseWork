## 6. AI Usage Summary
- Used AI tool: Claude Code (Claude 3.5 Sonnet)
- Roles: Architect Agent, Implementation Agent, Testing/Reviewer Agent
- All important prompts are recorded in `ai/prompts.md`
- Agent interactions recorded in `ai/agent-log.md`
- Reflection answers in `ai/reflection.md`
- Git commits include prefixes: [AI-Architect], [AI-Implementation], [AI-Review], [Human], [Docs], [Fix]

## 7. Testing Summary
- Manual test cases: 10 (see docs/test-cases.md)
- All core functions have been tested and pass.

## 8. Known Limitations
- No GUI (only console)
- Passwords are stored in plain text (no encryption)
- File persistence only on `future` branch; `main` branch uses hardcoded data.
