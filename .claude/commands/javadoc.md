Write JavaDoc documentation for Java code - /javadoc <file> [method: <method>]

## Syntax

```
/javadoc <file> [method: <method>]
```

**Parameters:**
- `file` (required): Path to the target Java file
- `method` (optional): Specific method to document

## Examples

```
/javadoc src/main/java/autoparams/Generator.java
/javadoc autoparams/ObjectGenerator.java method: generate
/javadoc autoparams/customization/Customizer.java method: customize
```

## Pre-Documentation Validation
1. **File Existence**: Confirm the specified Java file exists and is readable
2. **Valid Java**: Verify file contains valid Java code structure
3. **Target Identification**: If method specified, confirm method exists in the file
4. **Documentation Scope**: Determine if target is public API or internal implementation
5. **Package Context**: Check if target is in `autoparams.internal` package or subpackages

## Execution Workflow
1. **Analyze Target File**: Read and understand the complete Java file structure and context
2. **Validate Target**: Confirm target class/method exists and is appropriate for documentation
3. **Assess Current State**: Check existing JavaDoc and identify what needs to be added or improved
4. **Determine Scope**: If method parameter provided, focus on specific method; otherwise document appropriate scope
5. **Apply CLAUDE.md Guidelines**: Follow all CLAUDE.md JavaDoc guidelines precisely
6. **Draft Documentation**: Write JavaDoc comments with proper structure and required tags
7. **Format Properly**: Ensure correct HTML formatting, escaping, and tag usage per CLAUDE.md
   - Use `{@link }` syntax for type names and method references
   - Use `{@code }` syntax for parameter names in descriptions and @throws tags
8. **Review Completeness**: Verify documentation covers functionality without implementation details

## Error Handling
- **File Not Found**: Provide clear error if specified file doesn't exist
- **Invalid Target**: Report error if specified method doesn't exist in file
- **Non-Public Target**: Skip or warn about non-public types/members (per CLAUDE.md guidelines)
- **Malformed Java**: Handle cases where Java file has syntax issues

## Success Criteria
- Documentation follows all CLAUDE.md JavaDoc guidelines
- Proper HTML formatting and escaping applied per CLAUDE.md rules
- Type names and method references use `{@link }` syntax for proper cross-referencing
- Parameter names use `{@code }` syntax in descriptions and @throws tags
- Internal implementation warnings added for `autoparams.internal` types as specified in CLAUDE.md
