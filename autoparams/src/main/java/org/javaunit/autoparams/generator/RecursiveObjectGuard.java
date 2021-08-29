package org.javaunit.autoparams.generator;

import java.lang.reflect.Type;
import java.util.Stack;

final class RecursiveObjectGuard implements ObjectGenerator {
    private final Stack<Type> objectGenerationStack = new Stack<>();

    private final ObjectGenerator original;
    private final long maxRecursionCount;

    public RecursiveObjectGuard(ObjectGenerator original, long maxRecursionCount) {
        this.original = original;
        this.maxRecursionCount = maxRecursionCount;
    }

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        if (!checkNumberOfRecursiveExecutions(query)) {
            return new ObjectContainer(null);
        }

        objectGenerationStack.push(query.getType());
        ObjectContainer result = this.original.generate(query, context);
        objectGenerationStack.pop();
        return result;
    }

    private boolean checkNumberOfRecursiveExecutions(ObjectQuery query) {
        long currentRecursionCount = objectGenerationStack.stream()
            .filter(query.getType()::equals)
            .count();
        return currentRecursionCount < maxRecursionCount;
    }

}
