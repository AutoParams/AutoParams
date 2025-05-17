package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import jakarta.validation.constraints.Max;

class MaxAnnotation {

    public static Max findMaxAnnotation(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? findMaxAnnotation((ParameterQuery) query)
            : null;
    }

    private static Max findMaxAnnotation(ParameterQuery query) {
        return query.getParameter().getAnnotation(Max.class);
    }
}
