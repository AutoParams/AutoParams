## Commit Message Guidelines

- Write in English.
- Follow the 50/72 rule.
- Explain what and why, not how.
- Do not use prefixes like "feat:", "fix:", etc.

## Javadoc

- Write in English.
- Never modify or remove source code.
- Focus on functionality, not implementation.
- Escape `@` in code examples with `&#64;`.
- Escape `<` with `&lt;`.
- Escape `>` with `&gt;`.
- Use `@link` for links to classes, methods, and fields.
- Do not use `{@code` in example code.
- Bold the title of the example code.
- Add a short description of the code example. Wrap the description so that it is no longer than 80 characters.
- Do not write `@author` tags.
- Add `@see` tags for closely related types.
- Close `<p>` with `</p>` correctly.
- Write Javadoc for one type or one member at a time. Do not write Javadoc for a type and its members at the same time.
- Do not mention non-public types or members in Javadoc.
- When writing Javadoc for the types in the `autoparams.internal` package and its subpackages, mention that the types are code for internal implementation purposes and are not safe for external use because their interface and behavior can change at any time.
- Keep Javadoc to a minimum for the types in the `autoparams.internal` package and subpackages.
- When linking to generic types, include the generic signature. Example: `{@link Function Function&lt;T, R&gt;}`.
