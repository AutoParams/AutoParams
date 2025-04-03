package autoparams.generator;

import javax.validation.constraints.Min;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;

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
