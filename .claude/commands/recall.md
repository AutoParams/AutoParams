Recall relevant project rules and guidelines for the most recent work performed - /recall [guide]

## Syntax

```
/recall [guide]
```

**Parameters:**
- `guide` (optional): Specific type of rules/guidelines to recall. If not provided, infers from recent work context.

## Workflow
1. **Determine guide type**: Use specified parameter or infer from recent work
2. **Extract relevant rules**: Read from CLAUDE.md based on guide type
3. **Analyze recent work**: Check for rule violations in conversation history
4. **Present results**: Show rules, violations, and corrective actions
5. **Apply corrections**: If violations found, automatically perform the necessary corrections
6. **Verify fixes**: Confirm corrections were applied successfully
