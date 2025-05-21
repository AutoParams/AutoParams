package autoparams.customization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta-annotation that identifies a source for an {@link ArgumentRecycler}.
 * <p>
 * This annotation is applied to other annotations to indicate that they
 * provide a mechanism for recycling a resolved argument. The {@link #value}
 * element specifies the {@link ArgumentRecycler} implementation responsible
 * for creating a {@link Customizer} from the argument.
 * </p>
 * <p>
 * When AutoParams encounters an annotation marked with
 * {@link RecycleArgument @RecycleArgument} on a parameter, it uses the
 * specified {@link ArgumentRecycler} to process the argument associated with
 * that parameter. The resulting {@link Customizer} can then influence the
 * generation of the following arguments.
 * </p>
 *
 * @see ArgumentRecycler
 * @see Customizer
 * @see FreezeBy
 * @see Freeze
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RecycleArgument {

    /**
     * Specifies the {@link ArgumentRecycler} implementation that will be
     * used to process the annotated argument.
     *
     * @return the {@link Class} of the {@link ArgumentRecycler}
     *         implementation.
     */
    Class<? extends ArgumentRecycler> value();
}
