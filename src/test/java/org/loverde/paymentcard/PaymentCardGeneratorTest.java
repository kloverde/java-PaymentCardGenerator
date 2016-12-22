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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;


public class PaymentCardGeneratorTest {

   private static final int HOW_MANY_OF_EACH = 30;

   private PaymentCardGenerator generator = new PaymentCardGeneratorImpl();


   @Test( expected = IllegalArgumentException.class )
   public void generateByCardType_null() {
      generator.generateByCardType( null );
   }

   @Test
   public void generateByCardType_amex() {
      final CardType cardType = CardType.AMERICAN_EXPRESS;
      final String cardNum = generator.generateByCardType( cardType );

      validateCardNumber( cardType, cardNum );
   }

   @Test
   public void generateByCardType_visa() {
      final CardType cardType = CardType.VISA;
      final String cardNum = generator.generateByCardType( cardType );

      validateCardNumber( cardType, cardNum );
   }

   @Test
   public void generateByCardType_mastercard() {
      final CardType cardType = CardType.MASTERCARD;
      final String cardNum = generator.generateByCardType( cardType );

      validateCardNumber( cardType, cardNum );
   }

   @Test
   public void generateByCardType_discover() {
      final CardType cardType = CardType.DISCOVER;
      final String cardNum = generator.generateByCardType( cardType );

      validateCardNumber( cardType, cardNum );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateListByCardType_null() {
      generator.generateListByCardType( 2, null );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateListByCardType_zero() {
      generator.generateListByCardType( 0, CardType.AMERICAN_EXPRESS );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateListByCardType_negative() {
      generator.generateListByCardType( -1, CardType.DISCOVER );
   }

   @Test
   public void generateListByCardType_amex() {
      final CardType cardType = CardType.AMERICAN_EXPRESS;
      final List<String> cardNums = generator.generateListByCardType( HOW_MANY_OF_EACH, cardType );

      generateListByCardType_validate( cardType, cardNums );
   }

   @Test
   public void generateListByCardType_visa() {
      final CardType cardType = CardType.VISA;
      final List<String> cardNums = generator.generateListByCardType( HOW_MANY_OF_EACH, cardType );

      generateListByCardType_validate( cardType, cardNums );
   }

   @Test
   public void generateListByCardType_mastercard() {
      final CardType cardType = CardType.MASTERCARD;
      final List<String> cardNums = generator.generateListByCardType( HOW_MANY_OF_EACH, cardType );

      generateListByCardType_validate( cardType, cardNums );
   }

   @Test
   public void generateListByCardType_discover() {
      final CardType cardType = CardType.DISCOVER;
      final List<String> cardNums = generator.generateListByCardType( HOW_MANY_OF_EACH, cardType );

      assertEquals( HOW_MANY_OF_EACH, cardNums.size() );

      generateListByCardType_validate( cardType, cardNums );
   }

   private void generateListByCardType_validate( final CardType cardType, final List<String> cards ) {
      for( final String cardNum : cards ) {
         validateCardNumber( cardType, cardNum );
      }
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateMapByCardTypes_nullCardTypes() {
      CardType cardTypes[] = null;
      generator.generateMapByCardTypes( 2, cardTypes );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateMapByCardTypes_emptyCardTypes() {
      generator.generateMapByCardTypes( 2, new CardType[]{} );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateMapByCardTypes_zero() {
      generator.generateMapByCardTypes( 0, CardType.AMERICAN_EXPRESS );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateMapByCardTypes_negative() {
      generator.generateMapByCardTypes( -1, CardType.AMERICAN_EXPRESS );
   }

   @Test
   public void generateMapByCardTypes() {
      final Map<CardType, List<String>> cardMap = generator.generateMapByCardTypes( HOW_MANY_OF_EACH, CardType.values() );

      assertEquals( "Didn't generate all of the specified card types", CardType.values().length, cardMap.size() );

      int howManyCards = 0;

      for( final List<String> list : cardMap.values() ) {
         howManyCards += list.size();
      }

      assertEquals( "Didn't generate the correct number of cards", HOW_MANY_OF_EACH * CardType.values().length, howManyCards );

      for( final Entry<CardType, List<String>> entry : cardMap.entrySet() ) {
         final List<String> cardNumbers = entry.getValue();

         for( final String num : cardNumbers ) {
            validateCardNumber( entry.getKey(), num );
         }
      }
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateByPrefix_makeZero() {
      generator.generateByPrefix( 0, CardType.AMERICAN_EXPRESS.getLengths(), CardType.AMERICAN_EXPRESS.getPrefixes() );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateByPrefix_makeNegative() {
      generator.generateByPrefix( -1, CardType.AMERICAN_EXPRESS.getLengths(), CardType.AMERICAN_EXPRESS.getPrefixes() );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateByPrefix_nullLengths() {
      generator.generateByPrefix( 1, null, CardType.AMERICAN_EXPRESS.getPrefixes() );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateByPrefix_emptyLengths() {
      generator.generateByPrefix( 1, new ArrayList<Integer>(), CardType.AMERICAN_EXPRESS.getPrefixes() );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateByPrefix_invalidLengths_zero() {
      final List<Integer> lengths = new ArrayList<Integer>();
      lengths.addAll( CardType.AMERICAN_EXPRESS.getLengths() );
      lengths.add( 0 );

      generator.generateByPrefix( 1, lengths, CardType.AMERICAN_EXPRESS.getPrefixes() );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateByPrefix_invalidLengths_negative() {
      final List<Integer> lengths = new ArrayList<Integer>();
      lengths.addAll( CardType.AMERICAN_EXPRESS.getLengths() );
      lengths.add( -1 );

      generator.generateByPrefix( 1, lengths, CardType.AMERICAN_EXPRESS.getPrefixes() );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateByPrefix_invalidLengths_lessThan2() {
      final List<Integer> lengths = new ArrayList<Integer>();
      lengths.addAll( CardType.AMERICAN_EXPRESS.getLengths() );
      lengths.add( 1 );

      // Ensure that we hit the correct validation failure:  the prefix is not longer than the length
      final Set<Long> prefixes = new HashSet<>();
      prefixes.add( 1L );

      generator.generateByPrefix( 1, lengths, prefixes );
   }

   @Test
   public void generateByPrefix_minimumLengthOf2() {
      final List<Integer> lengths = new ArrayList<Integer>();
      lengths.add( 2 );

      final Set<Long> prefixes = new HashSet<>();
      prefixes.add( 1L );

      generator.generateByPrefix( 1, lengths, prefixes );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateByPrefix_nullPrefixes() {
      generator.generateByPrefix( 1, CardType.AMERICAN_EXPRESS.getLengths(), null );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateByPrefix_emptyPrefixes() {
      generator.generateByPrefix( 1, CardType.AMERICAN_EXPRESS.getLengths(), new HashSet<Long>() );
   }

   @Test( expected = IllegalArgumentException.class )
   public void generateByPrefix_prefixIsLongerThanLength() {
      final List<Integer> lengths = new ArrayList<>();
      lengths.add( 4 );

      final Set<Long> prefixes = new HashSet<Long>();
      prefixes.add( 12345L );

      generator.generateByPrefix( 1, lengths, prefixes );
   }

   @Test
   public void generateByPrefix_amex() {
      final CardType cardType = CardType.AMERICAN_EXPRESS;
      final Map<Long, List<String>> cards = generator.generateByPrefix( HOW_MANY_OF_EACH, cardType.getLengths(), cardType.getPrefixes() );

      generateByPrefix_validate( HOW_MANY_OF_EACH * cardType.getPrefixes().size(), cardType.getLengths(), cardType.getPrefixes(), cards );
   }

   @Test
   public void generateByPrefix_visa() {
      final CardType cardType = CardType.VISA;
      final Map<Long, List<String>> cards = generator.generateByPrefix( HOW_MANY_OF_EACH, cardType.getLengths(), cardType.getPrefixes() );

      generateByPrefix_validate( HOW_MANY_OF_EACH * cardType.getPrefixes().size(), cardType.getLengths(), cardType.getPrefixes(), cards );
   }

   @Test
   public void generateByPrefix_mastercard() {
      final CardType cardType = CardType.MASTERCARD;
      final Map<Long, List<String>> cards = generator.generateByPrefix( HOW_MANY_OF_EACH, cardType.getLengths(), cardType.getPrefixes() );

      generateByPrefix_validate( HOW_MANY_OF_EACH * cardType.getPrefixes().size(), cardType.getLengths(), cardType.getPrefixes(), cards );
   }

   @Test
   public void generateByPrefix_discover() {
      final CardType cardType = CardType.AMERICAN_EXPRESS;
      final Map<Long, List<String>> cards = generator.generateByPrefix( HOW_MANY_OF_EACH, cardType.getLengths(), cardType.getPrefixes() );

      generateByPrefix_validate( HOW_MANY_OF_EACH * cardType.getPrefixes().size(), cardType.getLengths(), cardType.getPrefixes(), cards );
   }

   @Test
   public void generateByPrefix_someFutureFormat() {
      final List<Integer> lengths = new ArrayList<>();

      lengths.add( 15 );
      lengths.add( 17 );

      final Set<Long> prefixes = new HashSet<>();

      prefixes.add( 987L );

      final Map<Long, List<String>> cards = generator.generateByPrefix( HOW_MANY_OF_EACH, lengths, prefixes );

      generateByPrefix_validate( HOW_MANY_OF_EACH, lengths, prefixes, cards );
   }

   private void generateByPrefix_validate( final int expectedCards, final List<Integer> validLengths, final Set<Long> validPrefixes, final Map<Long, List<String>> cardsMap ) {
      int howManyCards = 0;

      for( final List<String> list : cardsMap.values() ) {
         howManyCards += list.size();
      }

      assertEquals( "Didn't generate the correct number of cards", expectedCards, howManyCards );

      for( final Entry<Long, List<String>> entry : cardsMap.entrySet() ) {
         final List<String> cardNumbers = entry.getValue();

         for( final String num : cardNumbers ) {
            assertTrue( "Card number " + num + " was put in the bucket for prefix " + entry.getKey(), num.startsWith(entry.getKey().toString()) );
            validateCardNumber( validLengths, validPrefixes, num );
         }
      }
   }

   @Test
   public void passesLuhnCheck() {
      assertTrue( generator.passesLuhnCheck("378282246310005") );
      assertTrue( generator.passesLuhnCheck("4111111111111111") );
      assertTrue( generator.passesLuhnCheck("5105105105105100") );
      assertTrue( generator.passesLuhnCheck("6011111111111117") );

      assertFalse( generator.passesLuhnCheck("378282246310004") );
      assertFalse( generator.passesLuhnCheck("411111111111111") );
   }

   private void validateCardNumber( final CardType cardType, final String cardNum ) {
      validateCardNumber( cardType.getLengths(), cardType.getPrefixes(), cardNum );
   }

   private void validateCardNumber( final List<Integer> validLengths, final Set<Long> validPrefixes, final String cardNum ) {
      assertTrue( "Card number " + cardNum + " has an invalid length", validLengths.contains(cardNum.length()) );
      assertTrue( "Card number " + cardNum + " doesn't pass Luhn validation", generator.passesLuhnCheck(cardNum) );

      boolean validPrefix = false;

      for( final Long prefix : validPrefixes ) {
         if( cardNum.startsWith(prefix.toString()) ) {
            validPrefix = true;
            break;
         }
      }

      assertTrue( "Card number " + cardNum + " has an invalid prefix", validPrefix );
   }
}
