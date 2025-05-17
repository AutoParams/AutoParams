package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import jakarta.validation.constraints.Min;

class MinAnnotation {

    public static Min findMinAnnotation(ObjectQuery query) {
        return query instanceof ParameterQuery
            ? findMinAnnotation((ParameterQuery) query)
            : null;
    }

    private static Min findMinAnnotation(ParameterQuery query) {
        return query.getParameter().getAnnotation(Min.class);
    }
}
