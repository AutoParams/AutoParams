---
mode: agent
---

# Commit Message Workflow

## 1. Review Staged Changes
- Execute `git diff --staged` to check the staged changes.

## 2. Review Commit Message Guidelines
- Refer to the [Commit Message Instructions](../../docs/contexts/commit-message-instructions.md) for detailed rules.

## 3. Draft and Validate Commit Message
- Write a commit message for the staged changes following the guidelines.
- Before finalizing, check the subject line length using one of the following:
  Bash: `echo -n "<subject line>" | wc -c`
  PowerShell: `"<subject line>".Length`
- If the subject line exceeds 50 characters, rewrite it to be 50 characters or less and repeat the check.
{{#if topic}}
- Focus the commit message on changes related to: {{topic}}
{{/if}}

## 4. Review and Commit
- Proceed with the commit after the commit message is reviewed.
