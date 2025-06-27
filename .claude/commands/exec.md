Execute the next uncompleted task from a project backlog file - /exec backlog: <file>

## Syntax

```
/exec backlog: <file>
```

**Parameters:**
- `file` (required): Path to the target backlog file

## Examples

```
/exec backlog: work/backlogs/documentation.md
/exec backlog: work/backlogs/feature-requests.md
```

## Pre-Execution Validation
1. **File Existence**: Confirm the specified backlog file exists and is readable
2. **File Format**: Verify file follows proper markdown task format (`- [ ]` and `- [x]`)
3. **Task Structure**: Ensure tasks have clear descriptions and sub-requirements are properly indented
4. **Context Understanding**: Read file header/context to understand backlog purpose

## Execution Workflow
1. **Parse Backlog**: Read and analyze the specified backlog file thoroughly
2. **Validate Format**: Confirm backlog follows proper task format with checkboxes
3. **Identify Next Task**: Locate the first uncompleted task marked with `- [ ]` in sequential order
4. **Analyze Task**: Read task description and all sub-requirements carefully
5. **Execute Task**: Perform task following CLAUDE.md backlog processing guidelines
6. **Progress Updates**: Provide clear, detailed progress updates during execution
7. **Request Confirmation**: Ask user to explicitly confirm successful task completion
8. **Update Status**: Mark task as `- [x]` only after user confirmation (per CLAUDE.md guidelines)
9. **Report Progress**: Identify next available task and provide comprehensive completion status

## Error Handling
- **File Not Found**: Provide clear error message if backlog file doesn't exist
- **Invalid Format**: Report format issues and suggest corrections
- **Task Execution Failure**: Maintain original status and document failure reason (per CLAUDE.md)
- **No Uncompleted Tasks**: Report when all tasks are complete

## Success Criteria
- Task and all sub-requirements completed successfully
- User explicitly confirms completion
- Backlog file updated with proper formatting
- Progress clearly communicated following CLAUDE.md guidelines
