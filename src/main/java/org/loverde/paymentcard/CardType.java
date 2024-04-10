/*
 * PaymentCardGenerator
 * https://www.github.com/kloverde/java-PaymentCardGenerator
 *
 * Copyright (c) 2016 Kurtis LoVerde
 * All rights reserved
 *
 * Donations:  https://paypal.me/KurtisLoVerde/5
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *     2. Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *     3. Neither the name of the copyright holder nor the names of its
 *        contributors may be used to endorse or promote products derived from
 *        this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.loverde.paymentcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.loverde.paymentcard.internal.Objects.failIf;


/**
 * Defines various payment card types, along with a known list of valid lengths and prefixes, according to
 * <a href="https://en.wikipedia.org/wiki/Payment_card_number">https://en.wikipedia.org/wiki/Payment_card_number</a>
 * as of April 2024.
 */
public enum CardType {
    AMERICAN_EXPRESS(
        prefixesFromRange(
            new Range(34, 34),
            new Range(37, 37)),

        Set.of(15)
    ),

    VISA(
        prefixesFromRange(new Range(4, 4)),
        Set.of(13, 16, 19)
    ),

    MASTERCARD(
        prefixesFromRange(
            new Range(51, 55),
            new Range(2221, 2720)),

        Set.of(16)
    ),

    DISCOVER(
        prefixesFromRange(
            new Range(65, 65),
            new Range(644, 649),
            new Range(6011, 6011),
            new Range(622126, 622925)),

        Set.of(16, 19)
    );

    private final Set<Long> prefixes;
    private final Set<Integer> lengths;


    CardType(final Set<Long> prefixes, final Set<Integer> lengths) {
        this.prefixes = prefixes;
        this.lengths = lengths;
    }

    public Set<Long> getPrefixes() {
        return prefixes;
    }

    public Set<Integer> getLengths() {
        return lengths;
    }

    private static Set<Long> prefixesFromRange(final Range... ranges) {
        failIf(ranges == null || ranges.length == 0, () -> "Ranges is null or empty");

        final Set<Long> prefixes = new LinkedHashSet<>();

        for (final Range r : ranges) {
            if (r != null) {
                final List<Long> list = new ArrayList<>((int) (r.end() - r.start() + 1));

                for (long i = r.start(); i <= r.end(); i++) {
                    list.add(i);
                }

                prefixes.addAll(list);
            }
        }

        return Collections.unmodifiableSet(prefixes);
    }
}
