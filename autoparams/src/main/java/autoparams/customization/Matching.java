package autoparams.customization;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.function.BiPredicate;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.internal.reflect.TypeLens;

public enum Matching implements BiPredicate<Parameter, ObjectQuery> {

    EXACT_TYPE {
        @Override
        public boolean test(Parameter parameter, ObjectQuery query) {
            Type parameterType = parameter.getParameterizedType();
            return query.getType().equals(parameterType);
        }
    },

    IMPLEMENTED_INTERFACES {
        @Override
        public boolean test(Parameter parameter, ObjectQuery query) {
            Type parameterType = parameter.getParameterizedType();
            TypeLens typeLens = new TypeLens(parameterType);
            return typeLens.implementsInterface(query.getType());
        }
    },

    PARAMETER_NAME {
        @Override
        public boolean test(Parameter parameter, ObjectQuery query) {
            return query instanceof ParameterQuery
                && test(parameter, (ParameterQuery) query);
        }

        private boolean test(Parameter parameter, ParameterQuery query) {
            String parameterName = query.getRequiredParameterName();
            return parameter.getName().equals(parameterName);
        }
    }
}
