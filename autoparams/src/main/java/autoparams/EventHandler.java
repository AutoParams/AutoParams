package autoparams;

import java.util.ArrayList;
import java.util.List;

class EventHandler {

    private enum EventType {
        RESOLVING {
            @Override
            public String getSymbol() {
                return "▶";
            }
        },

        RESOLVED {
            @Override
            public String getSymbol() {
                return "✓";
            }
        };

        public abstract String getSymbol();
    }

    private static class Event {

        private final int depth;
        private final String message;
        private final EventType type;

        Event(int depth, String message, EventType type) {
            this.depth = depth;
            this.message = message;
            this.type = type;
        }
    }

    private final List<Event> events = new ArrayList<>();
    private int depth = 0;

    private void addEvent(EventType type, String message) {
        events.add(new Event(depth, message, type));
    }

    public void onResolving(ObjectQuery query) {
        addEvent(EventType.RESOLVING, "Resolving for: " + query);
        increaseDepth();
    }

    private void increaseDepth() {
        depth++;
    }

    public void onResolved(ObjectQuery query, Object value) {
        decreaseDepth();
        addEvent(EventType.RESOLVED, "Resolved: " + value + " for: " + query);
        flushEventsIfRootDepth();
    }

    private void decreaseDepth() {
        depth--;
    }

    private void flushEventsIfRootDepth() {
        if (depth == 0) {
            printEvents();
            events.clear();
        }
    }

    private void printEvents() {
        StringBuilder report = new StringBuilder();
        int depth = 0;
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            int previousDepth = depth;
            depth = event.depth;
            if (event.type.equals(EventType.RESOLVING)) {
                if (i != 0 && previousDepth == depth) {
                    report
                        .append(repeat("│   ", depth).trim())
                        .append(System.lineSeparator());
                }

                StringBuilder indentation = new StringBuilder();
                indentation.append(repeat("│   ", depth - 1));
                if (depth > 0) {
                    indentation.append("├── ");
                }

                report
                    .append(indentation)
                    .append(event.type.getSymbol())
                    .append(" ")
                    .append(event.message)
                    .append(System.lineSeparator());
            } else if (event.type.equals(EventType.RESOLVED)) {
                report
                    .append(repeat("│   ", depth))
                    .append(event.type.getSymbol())
                    .append(" ")
                    .append(event.message)
                    .append(System.lineSeparator());
            }
        }

        System.out.println(report);
    }

    @SuppressWarnings("SameParameterValue")
    private static String repeat(String s, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(s);
        }

        return result.toString();
    }
}
