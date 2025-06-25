Create commits following project message guidelines - /commit [topic: <topic>]

## Syntax

```
/commit [topic: <topic>]
```

**Parameters:**
- `topic` (optional): A specific topic or area of focus for the commit. If not provided, the commit will be general.

## Examples

```
/commit
/commit topic: Factory
/commit topic: documentation
```

## Execution Workflow

Execute these steps in order:

1. **Read Guidelines**: First read [commit guidelines](../../work/contexts/commit-guidelines.md) to review the full workflow
2. **Review Staged**: Execute `git diff --staged` to examine what will be committed
3. **Check Rules**: Verify commit message guidelines (50 char subject, 72 char body, present tense, no prefixes)
4. **Draft Message**: Write commit message following rules and optional topic focus
5. **Create Commit**: Execute `git commit -m "message"`
6. **Validate**: Run `work/scripts/check-commit-message-rule.sh` to verify 50/72 rule
7. **Fix if Failed**: Use `git commit --amend -m "new message"` if validation fails, repeat steps 6-7 until passes
