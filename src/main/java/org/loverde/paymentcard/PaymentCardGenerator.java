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

import java.util.List;
import java.util.Map;
import java.util.Set;


public interface PaymentCardGenerator {

   /**
    * Generates a card number for a given card type.  The prefix and length are
    * randomly selected from the values defined in {@linkplain CardType}.
    *
    * @param cardType The type of card number of generate
    *
    * @return A card number for the specified card type
    */
   public String generateByCardType( CardType cardType );

   /**
    * Generates multiple card numbers for a given card type.  The prefix and length
    * are randomly selected from the values defined in {@linkplain CardType}.
    *
    * @param howMany How many card numbers to generate for the specified card type
    * @param cardType The type of card numbers to generate
    *
    * @return A list of card numbers for the specified card type
    */
   public List<String> generateListByCardType( int howMany, CardType cardType );

   /**
    * Generates card numbers of given card types.  The prefix and length are
    * randomly selected from the values defined in {@linkplain CardType}.
    *
    * @param howManyOfEach How many card numbers to generate for each card type
    * @param cardTypes Vararg of card types
    *
    * @return A map where the key is the card type and the value is a list of card numbers for that card type
    */
   public Map<CardType, List<String>> generateMapByCardTypes( int howManyOfEach, CardType ... cardTypes );

   /**
    * Generates numbers based on a specified prefix and length.  This method does not validate the prefix
    * or length arguments to determine whether they apply to a known {@linkplain CardType}.  This is to
    * account for the possibility that the Wikipedia articles this software is based on are incorrect, or
    * that this software could be outdated.  Known prefix and length options are available in the
    * {@linkplain CardType} enum, if you wish to use them.  Essentially, this method is a general-purpose
    * Luhn number generator.
    *
    * @param howManyOfEachPrefix How many card numbers to generate for each prefix
    * @param lengths Generated card numbers will be of lengths specified by this list
    * @param prefixes Generated card numbers will start with values from this set
    *
    * @return A map where the key is the prefix and the value is a list of card numbers for that prefix
    */
   public Map<Long, List<String>> generateByPrefix( int howManyOfEachPrefix, List<Integer> lengths, Set<Long> prefixes );

   /**
    * Determines whether a number passes Luhn validation
    *
    * @param num A numeric string ending with a check digit
    *
    * @return {@code true} if the number is valid, {@code false} if not
    */
   public boolean passesLuhnCheck( String num );
}
