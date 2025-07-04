#!/bin/bash

# Check if a commit message follows project rules
# Rules: 50/72 formatting, no advertisements/branding
# Usage: ./check-commit-message-rules.sh [commit-hash]
# If no commit-hash is provided, checks the latest commit

# Determine which commit to check
if [ $# -eq 0 ]; then
    commit_ref="HEAD"
    echo "Checking latest commit..."
else
    commit_ref="$1"
    echo "Checking commit: $commit_ref"
fi

# Get the commit message
commit_message=$(git log -1 --pretty=format:"%s%n%b" "$commit_ref")

# Split into subject and body
subject=$(echo "$commit_message" | head -n1)
body=$(echo "$commit_message" | tail -n +2 | sed '/^$/d')

echo "Checking commit message format..."
echo "Subject: $subject"

# Check subject line length
subject_length=${#subject}
if [ $subject_length -gt 50 ]; then
    echo "[FAIL] Subject line too long: $subject_length characters (max 50)"
    exit_code=1
else
    echo "[PASS] Subject line length OK: $subject_length characters"
    exit_code=0
fi

# Check body line lengths if body exists
if [ -n "$body" ]; then
    echo "Checking body line lengths..."
    while IFS= read -r line; do
        line_length=${#line}
        if [ $line_length -gt 72 ]; then
            echo "[FAIL] Body line too long: $line_length characters (max 72)"
            echo "Line: $line"
            exit_code=1
        fi
    done <<< "$body"

    if [ $exit_code -eq 0 ]; then
        echo "[PASS] All body lines within 72 characters"
    fi
else
    echo "[INFO] No body content to check"
fi

# Check for advertisements, branding, or promotional content
echo "Checking for advertisements and branding..."
if echo "$commit_message" | grep -qi "generated with\|claude code\|anthropic\|co-authored-by.*claude\|🤖"; then
    echo "[FAIL] Commit message contains advertisements, branding, or promotional content"
    exit_code=1
else
    echo "[PASS] No advertisements or branding detected"
fi

if [ $exit_code -eq 0 ]; then
    echo "[PASS] Commit message follows all rules"
else
    echo "[FAIL] Commit message violates project rules"
fi

exit $exit_code
