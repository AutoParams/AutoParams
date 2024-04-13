package autoparams.generator;

import java.util.concurrent.ThreadLocalRandom;

import autoparams.ResolutionContext;

final class CharacterGenerator extends PrimitiveTypeGenerator<Character> {

    CharacterGenerator() {
        super(char.class, Character.class);
    }

    @Override
    protected Character generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return (char) random.nextInt(Character.MIN_VALUE, Character.MAX_VALUE);
    }
}
