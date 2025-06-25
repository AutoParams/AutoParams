Execute the next uncompleted task from a project backlog file - /run backlog: <file>

## Syntax

```
/run backlog: <file>
```

**Parameters:**
- `file` (required): Path to the target backlog file

## Examples

```
/run backlog: work/backlogs/documentation.md
/run backlog: work/backlogs/feature-requests.md
```

## Execution Workflow

Execute these steps in order:

1. **Read Guidelines**: First read [run guidelines](../../work/contexts/run-guidelines.md) to review task processing rules
2. **Parse Backlog**: Read the specified backlog file to locate tasks
3. **Find Next Task**: Identify the first uncompleted task marked with `- [ ]`
4. **Execute Task**: Follow task requirements and any sub-requirements listed
5. **Provide Updates**: Give clear progress updates during execution
6. **Request Confirmation**: Ask user to confirm task completion
7. **Update Status**: Mark task as `- [x]` only after user confirmation
8. **Report Progress**: Identify next available task and report completion status
