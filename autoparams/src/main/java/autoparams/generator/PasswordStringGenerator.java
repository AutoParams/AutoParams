package autoparams.generator;

import java.security.SecureRandom;
import java.util.Random;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

final class PasswordStringGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return query instanceof ParameterQuery
            ? generate((ParameterQuery) query)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterQuery query) {
        return query
            .getParameterName()
            .filter(PasswordStringGenerator::parameterNameMatch)
            .map(name -> generatePassword())
            .map(ObjectContainer::new)
            .orElse(ObjectContainer.EMPTY);
    }

    private static boolean parameterNameMatch(String parameterName) {
        return parameterName.toLowerCase().endsWith("password");
    }

    private String generatePassword() {
        SecureRandom random = new SecureRandom();

        CharSequence[] characterSets = {
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "abcdefghijklmnopqrstuvwxyz",
            "0123456789",
            "!@#$%^&*[]{}()<>~`-=_+;:'\",./?\\| "
        };
        shuffle(random, characterSets);

        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            for (CharSequence characterSet : characterSets) {
                password.append(sample(random, characterSet));
            }
        }

        return password.toString();
    }

    private <T> void shuffle(Random random, T[] array) {
        for (int i = 0; i < array.length; i++) {
            int j = random.nextInt(array.length);
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    private static char sample(Random random, CharSequence characterSet) {
        return characterSet.charAt(random.nextInt(characterSet.length()));
    }
}
