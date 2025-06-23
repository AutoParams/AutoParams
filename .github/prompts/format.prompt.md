---
mode: agent
---

# Java Code Formatting Agent

## Step 1: Analysis

### Configuration Check
- Read `.editorconfig` for formatting rules
- Review [Coding Guidelines](../../docs/coding-guidelines.md)

### File Inspection
- Read entire file and identify class members
- Run `git diff --check` for whitespace violations
- Count line lengths (100 character limit)

### Issue Detection
Create checklists for violations found:
- **Spacing**: Missing blank lines around methods/classes
- **Whitespace**: Trailing spaces, mixed tabs/spaces
- **Length**: Lines exceeding 100 characters

## Step 2: Apply Fixes

### Fix Order
1. **Whitespace issues** (use git commands to verify)
2. **Spacing violations** (1 blank line around methods/classes)
3. **Line length** (break at logical points with 4-space indent)

### Fix Strategies
- **Long lines**: Break at operators, method calls, or parameters
- **Trailing whitespace**: Remove all spaces/tabs at line ends
- **Method spacing**: Ensure exactly 1 blank line before/after methods

### Verification
After each fix:
- Re-run `git diff --check` for whitespace
- Verify line lengths under 100 characters
- Confirm spacing follows EditorConfig rules

## Step 3: Final Check

- Run complete git whitespace scan
- Verify all violations resolved
- Report summary of fixes applied

## Rules
- Use `replace_string_in_file` with 3-5 lines context
- Maintain code functionality
- Follow 4-space indentation for Java
- Stop after 3 fix iterations if issues persist

{{#if filePath}}
Focus formatting on the file: {{filePath}}
{{/if}}

{{#if region}}
Focus formatting on the specific region: {{region}}
{{/if}}
