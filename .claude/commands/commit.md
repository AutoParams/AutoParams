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

## Pre-Commit Validation
1. **Staged Changes**: Ensure there are staged changes to commit
2. **Change Quality**: Verify changes are coherent and focused
3. **Topic Alignment**: If topic specified, confirm staged changes match the topic
4. **No Secrets**: Check that no sensitive information (keys, passwords) is being committed

## Execution Workflow
1. **Analyze Staged Changes**: Execute `git diff --staged` to examine what will be committed
2. **Categorize Changes**: Identify the nature of changes (new feature, enhancement, bug fix, refactoring, etc.)
3. **Check Sensitive Data**: Scan for any secrets, keys, or sensitive information in staged changes
4. **Draft Message**: Write commit message following CLAUDE.md commit guidelines
5. **Create Commit**: Execute commit using heredoc format for proper message formatting
6. **Validate**: Run `work/scripts/check-commit-message-rule.sh` as per CLAUDE.md guidelines
7. **Fix if Failed**: Use `git commit --amend` with heredoc format if validation fails
8. **Repeat**: Continue validation cycle until passes

## Error Handling
- **No Staged Changes**: Abort if no changes are staged for commit
- **Validation Failures**: Never leave commit with message that fails validation (per CLAUDE.md)
- **Sensitive Data**: Abort commit if sensitive information detected
- **Topic Mismatch**: Warn if staged changes don't align with specified topic

## Success Criteria
- Commit message passes validation and follows all CLAUDE.md commit guidelines
- Changes are coherent and appropriately scoped
- No sensitive information committed
