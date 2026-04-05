---
name: tdd-refactor
description: Refactor implementation while keeping all tests green. Use after tdd-pass to improve code structure.
---

# TDD Refactor — Improve the Design

Optionally refactor to improve the implementation design without changing behavior.

## Input

The user describes what to refactor, or asks for suggestions.

## Workflow

### 1. Refactor

- Improve code structure without changing external behavior
- Remove duplication
- Improve naming
- Simplify logic
- Do NOT add new features or change public API behavior

### 2. Verify

- Run the entire test class to ensure all tests still pass
- Run code style checks if available
- If any test fails, revert the refactoring change and try a different approach

## Important

- Always STOP and request review after this step
- If the refactoring is risky or large, break it into smaller steps
