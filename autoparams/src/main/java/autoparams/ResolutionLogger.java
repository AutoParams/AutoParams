package autoparams;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class ResolutionLogger {

    private final LogWriter logWriter;
    private final List<LogEntry> entries = new ArrayList<>();
    private int depth = 0;
    private boolean enabled = false;

    public ResolutionLogger(LogWriter logWriter) {
        this.logWriter = logWriter;
    }

    public void enable() {
        this.enabled = true;
    }

    public void onResolving(ObjectQuery query) {
        if (!enabled) {
            return;
        }

        LogEntry entry = new LogEntry();
        entry.query = query;
        entry.depth = depth;
        entry.startTime = System.currentTimeMillis();
        entries.add(entry);
        depth++;
    }

    public void onResolved(ObjectQuery query, Object value) {
        if (!enabled) {
            return;
        }

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

    private List<TreeNode> buildTreeStructure() {
        List<TreeNode> nodes = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();

        for (int i = 0; i < entries.size(); i++) {
            LogEntry entry = entries.get(i);
            TreeNode node = new TreeNode();
            node.entry = entry;

            // Remove nodes from the stack that are not ancestors
            while (!stack.isEmpty() && stack.peek().entry.depth >= entry.depth) {
                stack.pop();
            }

            // Set parent-child relationship
            if (!stack.isEmpty()) {
                node.parent = stack.peek();
                stack.peek().children.add(node);
            }

            nodes.add(node);
            stack.push(node);
        }

        return nodes;
    }

    private void calculatePrefixes(List<TreeNode> nodes) {
        for (TreeNode node : nodes) {
            if (node.entry.depth == 0) {
                node.cachedPrefix = "";
            } else {
                node.cachedPrefix = generatePrefixOptimized(node);
            }
        }
    }

    private String generatePrefixOptimized(TreeNode node) {
        if (node.entry.depth == 0) {
            return "";
        }

        // Build prefix in reverse order using append (O(1) operations)
        StringBuilder prefix = new StringBuilder();
        TreeNode current = node;

        while (current.entry.depth > 0) {
            if (current.entry.depth == node.entry.depth) {
                // This is the current node - determine if it's the last child
                boolean isLastChild = isLastChildOptimized(current);
                prefix.append(isLastChild ? " ─└ " : " ─├ ");
            } else {
                // This is an ancestor - determine the prefix character
                boolean isAncestorLastChild = isLastChildOptimized(current);
                prefix.append(isAncestorLastChild ? "    " : "  │ ");
            }
            current = current.parent;
        }

        // Reverse once at the end (O(d) operation)
        return prefix.reverse().toString();
    }

    private boolean isLastChildOptimized(TreeNode node) {
        if (node.parent == null) {
            return false;
        }
        List<TreeNode> siblings = node.parent.children;
        return siblings.get(siblings.size() - 1) == node;
    }

    private void flushEntries() {
        if (entries.isEmpty()) {
            return;
        }

        // Build tree structure - O(n)
        List<TreeNode> nodes = buildTreeStructure();

        // Calculate prefixes - O(n)
        calculatePrefixes(nodes);

        // Output logs - O(n)
        for (TreeNode node : nodes) {
            if (shouldLog(node.entry.query, false)) {
                String timeString = node.entry.elapsed < 1 ? "< 1ms" : node.entry.elapsed + "ms";
                String message = node.cachedPrefix + node.entry.query.toLog(false);

                if (shouldIncludeValueOptimized(node)) {
                    if (isLeafNodeOptimized(node)) {
                        message += " → " + node.entry.value;
                    } else {
                        message += " → " + getBranchValueDescription(node.entry);
                    }
                }
                message += " (" + timeString + ")";
                logWriter.write(message);
            }
        }

        entries.clear();
    }

    private boolean shouldIncludeValueOptimized(TreeNode node) {
        if (isLeafNodeOptimized(node)) {
            return true;
        }

        if (node.entry.value == null) {
            return true;
        }

        Class<?> queryType = getRawClass(node.entry.query.getType());
        Class<?> valueType = node.entry.value.getClass();
        return !queryType.equals(valueType);
    }

    private boolean isLeafNodeOptimized(TreeNode node) {
        return node.children.isEmpty() ||
               node.children.stream().noneMatch(child -> shouldLog(child.entry.query, false));
    }

    private static class LogEntry {

        ObjectQuery query;
        Object value;
        int depth;
        long startTime;
        long elapsed;
    }

    private static class TreeNode {
        LogEntry entry;
        TreeNode parent;
        List<TreeNode> children = new ArrayList<>();
        String cachedPrefix;
    }

    private String getBranchValueDescription(LogEntry entry) {
        Type queryType = entry.query.getType();
        Class<?> valueClass = entry.value.getClass();

        if (queryType instanceof ParameterizedType) {
            ParameterizedType parameterizedQuery =
                (ParameterizedType) queryType;
            Type[] typeArguments = parameterizedQuery.getActualTypeArguments();

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

    private Class<?> getRawClass(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
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
