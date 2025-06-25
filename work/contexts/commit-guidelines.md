# Commit Guidelines

## Workflow for Writing Commit Messages
Follow the structured workflow below to write commit messages that adhere to the guidelines.

### 1. Review Staged Changes
- Execute `git diff --staged` to check the staged changes.

### 2. Review Commit Message Guidelines
- Refer to the section '[Commit Message Guidelines](#commit-message-guidelines)' for detailed rules on writing commit messages.

### 3. Draft Commit Message
- Write a commit message for the staged changes following the guidelines.

### 4. Create Commit
- Create the commit with the drafted message.

### 5. Validate and Fix Commit Message
- **Immediately after creating the commit, validate it using the validation script.**
- Run `work/scripts/check-commit-message.sh` to check if the commit message follows the 50/72 rule.
- **If the validation fails:**
  - Use `git commit --amend` to modify the commit message
  - Fix the issues identified by the script (subject line length, body line length, etc.)
  - Run the validation script again to confirm the fixes
  - Repeat the amend and validation process until the validation passes
  - Never leave a commit with a message that fails validation

## Commit Message Guidelines
When writing commit messages, please follow these guidelines:

1. Write in English.
2. Use the present tense in the subject line (e.g., "Add feature" not "Added feature").
3. Keep the subject line to 50 characters or less.
4. Add a blank line between the subject and body.
5. Keep the body to 72 characters or less per line.
6. Within a paragraph, only break lines when the text exceeds 72 characters.
7. Describe changes to public API features and do not include implementation details such as package-private code.
8. Do not mention test code in commit messages.
9. Do not use any prefix (such as "fix:", "update:", "docs:", "feat:", etc.) in the subject line.
10. Do not start the subject line with a lowercase letter unless the first word is a function name or another identifier that is conventionally lowercase and there is a clear, justifiable reason for the exception. Otherwise, always start with an uppercase letter.
