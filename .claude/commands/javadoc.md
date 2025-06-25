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

## Execution Workflow

Execute these steps in order:

1. **Read Guidelines**: First read [JavaDoc guidelines](../../work/contexts/javadoc-guidelines.md) to review formatting standards
2. **Analyze Target**: Read the specified Java file to understand class/method structure
3. **Identify Scope**: If method parameter provided, focus on specific method; otherwise document entire class
4. **Draft Documentation**: Write JavaDoc comments following formatting conventions
5. **Apply Standards**: Ensure proper use of @param, @return, @throws tags as appropriate
6. **Review Completeness**: Verify all public APIs are documented according to guidelines
