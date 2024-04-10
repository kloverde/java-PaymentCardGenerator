package org.loverde.paymentcard.internal;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;


public class Objects {

    /**
     * Shorthand for IF statements that throw IllegalArgumentException
     * @param isFailed The result of the check
     * @param iaeMessage Exception message
     */
    public static void failIf(final boolean isFailed, final Supplier<String> iaeMessage) {
        if (isFailed) {
            throw new IllegalArgumentException(iaeMessage.get());
        }
    }

    /**
     * Return a random item from a set.
     * @param set The set to pull from
     * @return Random item from the set, or null if the set is empty
     * @param <T> Parameterized type of the set
     */
    public static <T> T randomItemFromSet(final Set<T> set) {
        failIf(set == null, () -> "Set is null");

        T t = null;
        Iterator<T> iter = set.iterator();

        final int stopAt = ThreadLocalRandom.current().nextInt(set.size());

        for (int i = 0; i <= stopAt; i++) {
            t = iter.next();
        }

        return t;
    }
}
