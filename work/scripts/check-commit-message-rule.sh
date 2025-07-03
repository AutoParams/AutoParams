#!/bin/bash

# Check if a commit message follows the 50/72 rule
# 50/72 rule: Subject line ≤50 characters, body lines ≤72 characters
# Usage: ./check-commit-message-rule.sh [commit-hash]
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

if [ $exit_code -eq 0 ]; then
    echo "[PASS] Commit message follows 50/72 rule"
else
    echo "[FAIL] Commit message violates 50/72 rule"
fi

exit $exit_code
