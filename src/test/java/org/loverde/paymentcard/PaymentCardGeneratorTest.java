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

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PaymentCardGeneratorTest {

    private static final int HOW_MANY_OF_EACH = 30;

    private final PaymentCardGenerator generator = new PaymentCardGeneratorImpl();


    @Test
    void generateByCardType_null() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateByCardType(null));
    }

    @Test
    void generateByCardType_amex() {
        final CardType cardType = CardType.AMERICAN_EXPRESS;
        final String cardNum = generator.generateByCardType(cardType);

        validateCardNumber(cardType, cardNum);
    }

    @Test
    void generateByCardType_visa() {
        final CardType cardType = CardType.VISA;
        final String cardNum = generator.generateByCardType(cardType);

        validateCardNumber(cardType, cardNum);
    }

    @Test
    void generateByCardType_mastercard() {
        final CardType cardType = CardType.MASTERCARD;
        final String cardNum = generator.generateByCardType(cardType);

        validateCardNumber(cardType, cardNum);
    }

    @Test
    void generateByCardType_discover() {
        final CardType cardType = CardType.DISCOVER;
        final String cardNum = generator.generateByCardType(cardType);

        validateCardNumber(cardType, cardNum);
    }

    @Test
    void generateListByCardType_null() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateListByCardType(2, null));
    }

    @Test
    void generateListByCardType_zero() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateListByCardType(0, CardType.AMERICAN_EXPRESS));
    }

    @Test
    void generateListByCardType_negative() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateListByCardType(-1, CardType.DISCOVER));
    }

    @Test
    void generateListByCardType_amex() {
        final CardType cardType = CardType.AMERICAN_EXPRESS;
        final List<String> cardNums = generator.generateListByCardType(HOW_MANY_OF_EACH, cardType);

        generateListByCardType_validate(cardType, cardNums);
    }

    @Test
    void generateListByCardType_visa() {
        final CardType cardType = CardType.VISA;
        final List<String> cardNums = generator.generateListByCardType(HOW_MANY_OF_EACH, cardType);

        generateListByCardType_validate(cardType, cardNums);
    }

    @Test
    void generateListByCardType_mastercard() {
        final CardType cardType = CardType.MASTERCARD;
        final List<String> cardNums = generator.generateListByCardType(HOW_MANY_OF_EACH, cardType);

        generateListByCardType_validate(cardType, cardNums);
    }

    @Test
    void generateListByCardType_discover() {
        final CardType cardType = CardType.DISCOVER;
        final List<String> cardNums = generator.generateListByCardType(HOW_MANY_OF_EACH, cardType);

        generateListByCardType_validate(cardType, cardNums);
    }

    private void generateListByCardType_validate(final CardType cardType, final List<String> cards) {
        assertEquals(HOW_MANY_OF_EACH, cards.size());

        for (final String cardNum : cards) {
            validateCardNumber(cardType, cardNum);
        }
    }

    @Test
    void generateMapByCardTypes_nullCardTypes() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateMapByCardTypes(2, (CardType[]) null));
    }

    @Test
    void generateMapByCardTypes_emptyCardTypes() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateMapByCardTypes(2, new CardType[]{}));
    }

    @Test
    void generateMapByCardTypes_zero() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateMapByCardTypes(0, CardType.AMERICAN_EXPRESS));
    }

    @Test
    void generateMapByCardTypes_negative() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateMapByCardTypes(-1, CardType.AMERICAN_EXPRESS));
    }

    @Test
    void generateMapByCardTypes() {
        final Map<CardType, List<String>> cardMap = generator.generateMapByCardTypes(HOW_MANY_OF_EACH, CardType.values());

        assertEquals(CardType.values().length, cardMap.size(), "Didn't generate all of the specified card types");

        int howManyCards = 0;

        for (final List<String> list : cardMap.values()) {
            howManyCards += list.size();
        }

        assertEquals(HOW_MANY_OF_EACH * CardType.values().length, howManyCards, "Didn't generate the correct number of cards");

        for (final Entry<CardType, List<String>> entry : cardMap.entrySet()) {
            final List<String> cardNumbers = entry.getValue();

            for (final String num : cardNumbers) {
                validateCardNumber(entry.getKey(), num);
            }
        }
    }

    @Test
    void generateByPrefix_makeZero() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateByPrefix(0, CardType.AMERICAN_EXPRESS.getLengths(), CardType.AMERICAN_EXPRESS.getPrefixes()));
    }

    @Test
    void generateByPrefix_makeNegative() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateByPrefix(-1, CardType.AMERICAN_EXPRESS.getLengths(), CardType.AMERICAN_EXPRESS.getPrefixes()));
    }

    @Test
    void generateByPrefix_nullLengths() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateByPrefix(1, null, CardType.AMERICAN_EXPRESS.getPrefixes()));
    }

    @Test
    void generateByPrefix_emptyLengths() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateByPrefix(1, Set.of(), CardType.AMERICAN_EXPRESS.getPrefixes()));
    }

    @Test
    void generateByPrefix_invalidLengths_zero() {
        final Set<Integer> lengths = Set.of(0);
        assertThrows(IllegalArgumentException.class, () -> generator.generateByPrefix(1, lengths, CardType.AMERICAN_EXPRESS.getPrefixes()));
    }

    @Test
    void generateByPrefix_invalidLengths_negative() {
        final Set<Integer> lengths = Set.of(-1);
        assertThrows(IllegalArgumentException.class, () -> generator.generateByPrefix(1, lengths, CardType.AMERICAN_EXPRESS.getPrefixes()));
    }

    @Test
    void generateByPrefix_invalidLengths_lessThan2() {
        final Set<Integer> lengths = Set.of(1);

        // Ensure that we hit the correct validation failure:  the prefix is not longer than the length
        final Set<Long> prefixes = new HashSet<>();
        prefixes.add(1L);

        assertThrows(IllegalArgumentException.class, () -> generator.generateByPrefix(1, lengths, prefixes));
    }

    @Test
    void generateByPrefix_minimumLengthOf2() {
        final Set<Integer> lengths = Set.of(2);

        final Set<Long> prefixes = new HashSet<>();
        prefixes.add(1L);

        generator.generateByPrefix(1, lengths, prefixes);
    }

    @Test
    void generateByPrefix_nullPrefixes() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateByPrefix(1, CardType.AMERICAN_EXPRESS.getLengths(), null));
    }

    @Test
    void generateByPrefix_emptyPrefixes() {
        assertThrows(IllegalArgumentException.class, () -> generator.generateByPrefix(1, CardType.AMERICAN_EXPRESS.getLengths(), new HashSet<>()));
    }

    @Test
    void generateByPrefix_prefixIsLongerThanLength() {
        final Set<Integer> lengths = Set.of(4);

        final Set<Long> prefixes = new HashSet<>();
        prefixes.add(12345L);

        assertThrows(IllegalArgumentException.class, () -> generator.generateByPrefix(1, lengths, prefixes));
    }

    @Test
    void generateByPrefix_amex() {
        final CardType cardType = CardType.AMERICAN_EXPRESS;
        final Map<Long, List<String>> cards = generator.generateByPrefix(HOW_MANY_OF_EACH, cardType.getLengths(), cardType.getPrefixes());

        generateByPrefix_validate(HOW_MANY_OF_EACH * cardType.getPrefixes().size(), cardType.getLengths(), cardType.getPrefixes(), cards);
    }

    @Test
    void generateByPrefix_visa() {
        final CardType cardType = CardType.VISA;
        final Map<Long, List<String>> cards = generator.generateByPrefix(HOW_MANY_OF_EACH, cardType.getLengths(), cardType.getPrefixes());

        generateByPrefix_validate(HOW_MANY_OF_EACH * cardType.getPrefixes().size(), cardType.getLengths(), cardType.getPrefixes(), cards);
    }

    @Test
    void generateByPrefix_mastercard() {
        final CardType cardType = CardType.MASTERCARD;
        final Map<Long, List<String>> cards = generator.generateByPrefix(HOW_MANY_OF_EACH, cardType.getLengths(), cardType.getPrefixes());

        generateByPrefix_validate(HOW_MANY_OF_EACH * cardType.getPrefixes().size(), cardType.getLengths(), cardType.getPrefixes(), cards);
    }

    @Test
    void generateByPrefix_discover() {
        final CardType cardType = CardType.AMERICAN_EXPRESS;
        final Map<Long, List<String>> cards = generator.generateByPrefix(HOW_MANY_OF_EACH, cardType.getLengths(), cardType.getPrefixes());

        generateByPrefix_validate(HOW_MANY_OF_EACH * cardType.getPrefixes().size(), cardType.getLengths(), cardType.getPrefixes(), cards);
    }

    @Test
    void generateByPrefix_someFutureFormat() {
        final Set<Integer> lengths = Set.of(15, 17);
        final Set<Long> prefixes = new HashSet<>();

        prefixes.add(987L);

        final Map<Long, List<String>> cards = generator.generateByPrefix(HOW_MANY_OF_EACH, lengths, prefixes);

        generateByPrefix_validate(HOW_MANY_OF_EACH, lengths, prefixes, cards);
    }

    private void generateByPrefix_validate(final int expectedCards, final Set<Integer> validLengths, final Set<Long> validPrefixes, final Map<Long, List<String>> cardsMap) {
        int howManyCards = 0;

        for (final List<String> list : cardsMap.values()) {
            howManyCards += list.size();
        }

        assertEquals(expectedCards, howManyCards, "Didn't generate the correct number of cards");

        for (final Entry<Long, List<String>> entry : cardsMap.entrySet()) {
            final List<String> cardNumbers = entry.getValue();

            for (final String num : cardNumbers) {
                assertTrue(num.startsWith(entry.getKey().toString()), "Card number " + num + " was put in the bucket for prefix " + entry.getKey());
                validateCardNumber(validLengths, validPrefixes, num);
            }
        }
    }

    @Test
    void passesLuhnCheck() {
        assertTrue(generator.passesLuhnCheck("378282246310005"));
        assertTrue(generator.passesLuhnCheck("4111111111111111"));
        assertTrue(generator.passesLuhnCheck("5105105105105100"));
        assertTrue(generator.passesLuhnCheck("6011111111111117"));

        assertFalse(generator.passesLuhnCheck("378282246310004"));
        assertFalse(generator.passesLuhnCheck("411111111111111"));
    }

    private void validateCardNumber(final CardType cardType, final String cardNum) {
        validateCardNumber(cardType.getLengths(), cardType.getPrefixes(), cardNum);
    }

    private void validateCardNumber(final Set<Integer> validLengths, final Set<Long> validPrefixes, final String cardNum) {
        assertTrue(validLengths.contains(cardNum.length()), "Card number " + cardNum + " has an invalid length");
        assertTrue(generator.passesLuhnCheck(cardNum), "Card number " + cardNum + " doesn't pass Luhn validation");

        boolean validPrefix = false;

        for (final Long prefix : validPrefixes) {
            if (cardNum.startsWith(prefix.toString())) {
                validPrefix = true;
                break;
            }
        }

        assertTrue(validPrefix, "Card number " + cardNum + " has an invalid prefix");
    }
}
