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

    private int getChildIndex(int entryIndex) {
        LogEntry entry = entries.get(entryIndex);
        int childIndex = 0;
        for (int i = 0; i < entryIndex; i++) {
            if (entries.get(i).depth == entry.depth) {
                childIndex++;
            }
        }
        return childIndex;
    }

    private int getTotalChildrenAtDepth(int targetDepth) {
        int count = 0;
        for (LogEntry entry : entries) {
            if (entry.depth == targetDepth) {
                count++;
            }
        }
        return count;
    }

    private String generatePrefix(int entryIndex, int depth) {
        if (depth == 1) {
            int childIndex = getChildIndex(entryIndex);
            int totalChildren = getTotalChildrenAtDepth(depth);
            return (childIndex == totalChildren - 1) ? " └─ " : " ├─ ";
        } else if (depth == 2) {
            int childIndex = getChildIndexWithinParent(entryIndex);
            int totalChildren = getTotalChildrenWithinParent(entryIndex);
            boolean isLastChild = (childIndex == totalChildren - 1);

            boolean isParentLastChild = isParentLastChild(entryIndex);
            String verticalBar = isParentLastChild ? "    " : " │  ";
            String connector = isLastChild ? " └─ " : " ├─ ";

            return verticalBar + connector;
        }
        return "";
    }

    private boolean isParentLastChild(int entryIndex) {
        LogEntry entry = entries.get(entryIndex);
        int parentDepth = entry.depth - 1;

        for (int i = entryIndex - 1; i >= 0; i--) {
            LogEntry previousEntry = entries.get(i);
            if (previousEntry.depth == parentDepth) {
                int parentChildIndex = getChildIndex(i);
                int parentTotalChildren = getTotalChildrenAtDepth(parentDepth);
                return parentChildIndex == parentTotalChildren - 1;
            }
        }
        return false;
    }

    private int getChildIndexWithinParent(int entryIndex) {
        LogEntry entry = entries.get(entryIndex);
        int parentDepth = entry.depth - 1;
        int parentIndex = -1;

        for (int i = entryIndex - 1; i >= 0; i--) {
            if (entries.get(i).depth == parentDepth) {
                parentIndex = i;
                break;
            }
        }

        int childIndex = 0;
        for (int i = parentIndex + 1; i < entryIndex; i++) {
            if (entries.get(i).depth == entry.depth) {
                childIndex++;
            }
        }
        return childIndex;
    }

    private int getTotalChildrenWithinParent(int entryIndex) {
        LogEntry entry = entries.get(entryIndex);
        int parentDepth = entry.depth - 1;
        int parentIndex = -1;

        for (int i = entryIndex - 1; i >= 0; i--) {
            if (entries.get(i).depth == parentDepth) {
                parentIndex = i;
                break;
            }
        }

        int nextParentIndex = entries.size();
        for (int i = parentIndex + 1; i < entries.size(); i++) {
            if (entries.get(i).depth == parentDepth) {
                nextParentIndex = i;
                break;
            }
        }

        int count = 0;
        for (int i = parentIndex + 1; i < nextParentIndex; i++) {
            if (entries.get(i).depth == entry.depth) {
                count++;
            }
        }
        return count;
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
            LogVisible logVisible = type.getAnnotation(LogVisible.class);
            if (logVisible != null && logVisible.verboseOnly() && !verbose) {
                return false;
            }
        }
        return true;
    }
}
