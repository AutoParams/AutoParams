# Run Command Guidelines

## Task Execution Process

### Workflow Steps

1. **Backlog Reading**: Parse the specified backlog file to locate tasks
2. **Task Identification**: Find the first uncompleted task marked with `- [ ]`
3. **Single Task Execution**: Execute exactly one task following its specific requirements
4. **User Confirmation**: Request user confirmation before marking task as complete
5. **Status Update**: Mark the completed task as `- [x]` in the file only after confirmation
6. **Progress Reporting**: Report completion status and identify next available task

### Task Processing Rules

- **One Task Per Invocation**: Execute only one task per command invocation
- **Sequential Processing**: Never skip tasks or execute out of order
- **Sub-requirement Compliance**: Follow any sub-requirements listed under the main task
- **Progress Transparency**: Provide clear progress updates during execution

### Status Management

- **Confirmation Required**: Request user confirmation before updating task status
- **Success-Based Completion**: Only mark tasks complete after successful execution
- **File Update Protocol**: Update backlog file only after user confirmation
- **Error Handling**: If task execution fails, maintain original status and report failure

### Backlog File Format

Tasks should be formatted as:
```markdown
- [ ] Task description
  - Sub-requirement 1
  - Sub-requirement 2
- [x] Completed task description
```

## Best Practices

- Read backlog files carefully to understand task context
- Execute tasks in the exact order they appear
- Maintain clear communication with user throughout process
- Preserve backlog file formatting when updating task status
