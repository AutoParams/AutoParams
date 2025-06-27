package autoparams;

import java.util.ArrayList;
import java.util.List;

class ResolutionLogger {

    private final LogWriter logWriter;
    private final List<LogEntry> entries = new ArrayList<>();
    private int depth = 0;

    public ResolutionLogger(LogWriter logWriter) {
        this.logWriter = logWriter;
    }

    public void onResolving(ObjectQuery query) {
        LogEntry entry = new LogEntry();
        entry.query = query;
        entry.depth = depth;
        entry.startTime = System.currentTimeMillis();
        entries.add(entry);
        depth++;
    }

    public void onResolved(ObjectQuery query, Object value) {
        depth--;
        for (int i = entries.size() - 1; i >= 0; i--) {
            LogEntry entry = entries.get(i);
            if (entry.value == null && entry.depth == depth) {
                entry.value = value;
                entry.elapsed = System.currentTimeMillis() - entry.startTime;
                break;
            }
        }

        if (depth == 0) {
            flushEntries();
        }
    }

    private void flushEntries() {
        for (int i = 0; i < entries.size(); i++) {
            LogEntry entry = entries.get(i);
            if (shouldLog(entry.query, false)) {
                String timeString = entry.elapsed < 1 ? "< 1ms" : entry.elapsed + "ms";
                String prefix = "";

                if (entry.depth > 0) {
                    prefix = generatePrefix(i, entry.depth);
                }

                String message = prefix + entry.query.toLog(false) + " → " + entry.value
                    + " (" + timeString + ")";
                logWriter.write(message);
            }
        }
        entries.clear();
    }

    private String generatePrefix(int entryIndex, int depth) {
        if (depth == 0) {
            return "";
        }

        StringBuilder prefix = new StringBuilder();

        for (int currentDepth = 1; currentDepth <= depth; currentDepth++) {
            if (currentDepth == depth) {
                int childIndex = getChildIndexAtDepth(entryIndex, currentDepth);
                int totalChildren = getTotalChildrenAtDepth(entryIndex, currentDepth);
                boolean isLastChild = (childIndex == totalChildren - 1);
                prefix.append(isLastChild ? " └─ " : " ├─ ");
            } else {
                boolean isAncestorLastChild = isAncestorLastChild(entryIndex, currentDepth);
                prefix.append(isAncestorLastChild ? "    " : " │  ");
            }
        }

        return prefix.toString();
    }

    private boolean isAncestorLastChild(int entryIndex, int ancestorDepth) {
        for (int i = entryIndex - 1; i >= 0; i--) {
            LogEntry ancestorEntry = entries.get(i);
            if (ancestorEntry.depth == ancestorDepth) {
                int ancestorChildIndex = getChildIndexAtDepth(i, ancestorDepth);
                int ancestorTotalChildren = getTotalChildrenAtDepth(i, ancestorDepth);
                return ancestorChildIndex == ancestorTotalChildren - 1;
            }
        }
        return false;
    }

    private int getChildIndexAtDepth(int entryIndex, int targetDepth) {
        int parentIndex = findParentIndex(entryIndex, targetDepth - 1);
        int childIndex = 0;
        int startIndex = (parentIndex == -1) ? 0 : parentIndex + 1;

        for (int i = startIndex; i < entryIndex; i++) {
            if (entries.get(i).depth == targetDepth) {
                childIndex++;
            }
        }
        return childIndex;
    }

    private int getTotalChildrenAtDepth(int entryIndex, int targetDepth) {
        int parentIndex = findParentIndex(entryIndex, targetDepth - 1);
        int nextParentIndex = findNextParentIndex(parentIndex, targetDepth - 1);
        int count = 0;
        int startIndex = (parentIndex == -1) ? 0 : parentIndex + 1;

        for (int i = startIndex; i < nextParentIndex; i++) {
            if (entries.get(i).depth == targetDepth) {
                count++;
            }
        }
        return count;
    }

    private int findParentIndex(int entryIndex, int parentDepth) {
        for (int i = entryIndex - 1; i >= 0; i--) {
            if (entries.get(i).depth == parentDepth) {
                return i;
            }
        }
        return -1;
    }

    private int findNextParentIndex(int parentIndex, int parentDepth) {
        for (int i = parentIndex + 1; i < entries.size(); i++) {
            if (entries.get(i).depth == parentDepth) {
                return i;
            }
        }
        return entries.size();
    }

    private static class LogEntry {
        ObjectQuery query;
        Object value;
        int depth;
        long startTime;
        long elapsed;
    }

    private boolean shouldLog(ObjectQuery query, boolean verbose) {
        if (query.getType() instanceof Class<?>) {
            Class<?> type = (Class<?>) query.getType();
            LogVisibility logVisibility = type.getAnnotation(LogVisibility.class);
            if (logVisibility != null && logVisibility.verboseOnly() && !verbose) {
                return false;
            }
        }
        return true;
    }
}
