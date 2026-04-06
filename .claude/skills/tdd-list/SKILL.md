---
name: tdd-list
description: Write a list of test scenarios for TDD. Use when starting TDD for a new feature or adding scenarios to an existing list.
---

# TDD List — Write Test Scenarios

Prepare a list of test scenarios to cover a feature.

## Syntax

```
/tdd-list [feature: <description or url>]
```

**Parameters:**
- `feature` (optional): Description of the feature or URL of the issue (GitHub, Jira, Notion, etc.). If not provided, use conversation context or ask the user.

## Workflow

### 1. Understand the Feature

- Read the feature description, issue, or relevant code
- Identify the system under test (sut)
- Identify the behaviors to verify

### 2. Write Scenarios

Write test scenarios following these rules:

- Write one scenario per line as a checklist item (`- [ ]`)
- Write in English, present tense
- Start with lowercase (usable as test method name)
- Refer to the system under test as 'sut'
- Write as concisely as possible while preserving meaning
- Use only characters that are valid in Java identifiers (letters, digits, and underscores) when spaces are replaced with `_`. Do not use special characters such as `'`, `-`, `.`, `,`, `(`, `)`, `/`, `@`, `<`, `>`, `"`, `:`, `;`, `!`, `?`, `#`, `%`, `&`, `*`, `+`, `=`, `{`, `}`, `[`, `]`, `|`, `\`, `~`, `^`, or backticks.
- Do not start with a digit, because the scenario text becomes a Java method name when spaces are replaced with `_`
- Order from most important to least important
- Start with the simplest, most fundamental behavior

### 3. Present for Review

Show the scenario list to the user and ask for feedback. Do NOT write the list to any external system until the user approves.

### 4. Persist the List

After user approval, write the list to the location the user specifies (GitHub issue, Jira, Notion, local file, etc.) using available tools.

## Important

- Do NOT write tests or code. Only produce the scenario list.
- Do NOT proceed to implementation. Stop after persisting the list.
