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

                String message = prefix + entry.query.toLog(false);
                if (shouldIncludeValue(entry)) {
                    if (isLeafNode(entry)) {
                        message += " → " + entry.value;
                    } else {
                        message += " → " + getBranchValueDescription(entry);
                    }
                }
                message += " (" + timeString + ")";
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

    private boolean shouldIncludeValue(LogEntry entry) {
        if (isLeafNode(entry)) {
            return true;
        }

        Class<?> queryType = getRawClass(entry.query.getType());
        Class<?> valueType = entry.value.getClass();
        return !queryType.equals(valueType);
    }

    private boolean isLeafNode(LogEntry entry) {
        int entryIndex = entries.indexOf(entry);
        int currentDepth = entry.depth;

        for (int i = entryIndex + 1; i < entries.size(); i++) {
            LogEntry nextEntry = entries.get(i);
            if (nextEntry.depth == currentDepth + 1) {
                return false;
            }
            if (nextEntry.depth <= currentDepth) {
                break;
            }
        }
        return true;
    }

    private String getBranchValueDescription(LogEntry entry) {
        java.lang.reflect.Type queryType = entry.query.getType();
        Class<?> valueClass = entry.value.getClass();

        if (queryType instanceof java.lang.reflect.ParameterizedType) {
            java.lang.reflect.ParameterizedType parameterizedQuery =
                (java.lang.reflect.ParameterizedType) queryType;
            java.lang.reflect.Type[] typeArguments = parameterizedQuery.getActualTypeArguments();

            String valueClassName = valueClass.getSimpleName();
            if (typeArguments.length > 0) {
                StringBuilder sb = new StringBuilder(valueClassName);
                sb.append("<");
                for (int i = 0; i < typeArguments.length; i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(TypeFormatter.format(typeArguments[i], false));
                }
                sb.append(">");
                return sb.toString();
            }
        }

        return TypeFormatter.format(valueClass, false);
    }

    private Class<?> getRawClass(java.lang.reflect.Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof java.lang.reflect.ParameterizedType) {
            return (Class<?>) ((java.lang.reflect.ParameterizedType) type).getRawType();
        }
        return Object.class;
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
