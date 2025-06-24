# Commit Guidelines

## Workflow for Writing Commit Messages
Follow the structured workflow below to write commit messages that adhere to the guidelines.

### 1. Review Staged Changes
- Execute `git diff --staged` to check the staged changes.

### 2. Review Commit Message Guidelines
- Refer to the section '[Commit Message Guidelines](#commit-message-guidelines)' for detailed rules on writing commit messages.

### 3. Draft and Validate Commit Message
- Write a commit message for the staged changes following the guidelines.
- Before finalizing, check the subject line length using one of the following:
  Bash: `echo -n "<subject line>" | wc -c`
  PowerShell: `"<subject line>".Length`
- If the subject line exceeds 50 characters, rewrite it to be 50 characters or less and repeat the check.

### 4. Review and Commit
- Proceed with the commit after the commit message is reviewed.

## Commit Message Guidelines
When writing commit messages, please follow these guidelines:

1. Write in English.
2. Keep the subject line to 50 characters or less.
3. Add a blank line between the subject and body.
4. Keep the body to 72 characters or less per line.
5. Describe changes to public API features and do not include implementation details such as package-private code.
6. Do not mention test code in commit messages.
7. Do not use any prefix (such as "fix:", "update:", "docs:", "feat:", etc.) in the subject line.
