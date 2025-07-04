---
mode: agent
---

You must perform the entire commit workflow yourself. This includes:
- Reviewing staged changes
- Drafting a commit message
- Validating the commit message
- Completing the commit

Follow all rules below throughout this process.

## Commit Message Guidelines
- Write in English.
- Use the present tense in the subject line (e.g., "Add feature" not "Added feature").
- Keep the subject line to 50 characters or less.
- Add a blank line between the subject and body.
- Keep the body to 72 characters or less per line.
- Within a paragraph, only break lines when the text exceeds 72 characters.
- Describe changes to public API features and do not include implementation details such as package-private code.
- Do not mention test code in commit messages.
- Do not use any prefix (such as "fix:", "update:", "docs:", "feat:", etc.) in the subject line.
- Do not start the subject line with a lowercase letter unless the first word is a function name or another identifier that is conventionally lowercase and there is a clear, justifiable reason for the exception. Otherwise, always start with an uppercase letter.

## Commit Workflow
1. Review staged changes with `git diff --staged`.
2. Draft a commit message following the above guidelines.
3. Create the commit with the drafted message.
4. Immediately validate the commit message using `work/scripts/check-commit-message-rules.sh`.
5. If validation fails:
   - Amend the commit message with `git commit --amend -m "<new commit message>"`
   - Fix the issues (subject/body length, etc.)
   - Re-run the validation script
   - Repeat until validation passes
   - Never leave a commit with a message that fails validation

{{#if topic}}
If a topic is provided, focus the commit message on changes related to: {{topic}}
{{/if}}

{{#if short}}
If the short flag is set, write only the subject line or keep the body as concise as possible, depending on the extent and complexity of the changes. Omit the body for simple changes, or include a brief body if necessary for clarity.
{{/if}}
