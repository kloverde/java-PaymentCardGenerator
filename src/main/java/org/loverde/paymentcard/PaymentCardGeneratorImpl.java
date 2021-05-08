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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/**
 * <p>
 *    This software aids in testing payment card processing systems by generating random payment card
 *    numbers which are mathematically valid, so that you don't have to use an actual card.
 *
 *    This class generates payment card numbers based on the criteria defined here:
 * </p>
 *
 * <ul>
 *    <li><a href="https://en.wikipedia.org/wiki/Luhn_algorithm">https://en.wikipedia.org/wiki/Luhn_algorithm</a></li>
 *    <li><a href="https://en.wikipedia.org/wiki/Payment_card_number">https://en.wikipedia.org/wiki/Payment_card_number</a> (as of December, 2016)</li>
 * </ul>
 *
 * <p>
 *    Most if not all of the payment card numbers generated by this software should not be tied to active
 *    accounts.  However, it is theoretically possible that, against all odds, this software could randomly
 *    generate a payment card number that's in use in the real world.  For this reason, you must ensure
 *    that these card numbers are only used in systems running in test mode, i.e. that a real transaction
 *    will not be attempted.
 * </p>
 *
 * <p>
 *    There's no point in trying to use this software for fraudulent purposes.  Not only will card numbers
 *    generated by this software likely not work, but if this software were by coincidence to generate an
 *    actual active account number, it would be illegal for you to attempt to use it.  Of course, you're
 *    smart enough to know this already.  You alone are responsible for what you do with this software.
 * </p>
 */
public class PaymentCardGeneratorImpl implements PaymentCardGenerator {

   @Override
   public String generateByCardType( final CardType cardType ) {
      if( cardType == null ) throw new IllegalArgumentException( "Card type is null" );

      return generateCardNumber( cardType );
   }

   @Override
   public List<String> generateListByCardType( final int howMany, final CardType cardType ) {
      if( howMany <= 0 ) throw new IllegalArgumentException( "How many must be greater than zero" );
      if( cardType == null ) throw new IllegalArgumentException( "Card type is null" );

      final List<String> cardNums = new ArrayList<>( howMany );

      for( int i = 0; i < howMany; i++ ) {
         cardNums.add( generateCardNumber(cardType) );
      }

      return cardNums;
   }

   @Override
   public Map<CardType, List<String>> generateMapByCardTypes( final int howManyOfEach, final CardType ... cardTypes ) {
      if( howManyOfEach <= 0 ) throw new IllegalArgumentException( "How many of each must be greater than zero" );
      if( cardTypes == null || cardTypes.length < 1 ) throw new IllegalArgumentException( "Card types is null or empty" );

      final Set<CardType> condensedCardTypes = removeVarargDuplicates( cardTypes );
      final Map<CardType, List<String>> cardNums = new HashMap<>( cardTypes.length );

      for( final CardType cardType : condensedCardTypes ) {
         cardNums.put( cardType, generateListByCardType(howManyOfEach, cardType) );
      }

      return cardNums;
   }

   @Override
   public Map<Long, List<String>> generateByPrefix( final int howManyOfEachPrefix, final List<Integer> lengths, final Set<Long> prefixes) {
      final Map<Long, List<String>> cardNums;
      final Random random;

      if( howManyOfEachPrefix <= 0 ) throw new IllegalArgumentException( "How many of each must be greater than zero" );
      if( lengths == null || lengths.size() == 0 ) throw new IllegalArgumentException( "No lengths were specified" );
      if( prefixes == null || prefixes.size() == 0 ) throw new IllegalArgumentException( "No prefixes were specified" );

      for( final Integer length : lengths ) {
         if( length == null || length < 2 ) throw new IllegalArgumentException( "Invalid length: " + length );

         for( final Long prefix : prefixes ) {
            if( prefix.toString().length() > length ) throw new IllegalArgumentException( String.format("Prefix (%s) is longer than length (%d)", prefix.toString(), length) );
            if( prefix < 1 ) throw new IllegalArgumentException( String.format("Prefix (%s):  prefixes must be positive numbers", prefix.toString()) );
         }
      }

      cardNums = new HashMap<>( prefixes.size() );
      random = new Random();

      for( final Long prefix : prefixes ) {
         final List<String> cardNumsForPrefix = new ArrayList<>( howManyOfEachPrefix );

         for( int i = 0; i < howManyOfEachPrefix; i++ ) {
            cardNumsForPrefix.add( generateCardNumber(prefix, lengths.get(random.nextInt(lengths.size()))) );
         }

         cardNums.put( prefix, cardNumsForPrefix );
      }

      return cardNums;
   }

   @Override
   public boolean passesLuhnCheck( final String num ) {
      if( num == null || num.isEmpty() ) throw new IllegalArgumentException( "Number is null or empty" );

      final int sum = calculateLuhnSum( num, true );
      final int checkDigit = calculateCheckDigit( sum );

      return (sum + checkDigit) % 10 == 0 && Integer.parseInt( num.substring(num.length() - 1) ) == checkDigit;
   }

   private static String generateCardNumber( final CardType cardType ) {
      final String cardNum = generateCardNumber( randomFromSet(cardType.getPrefixes()),
                                                 cardType.getLengths().get(new Random().nextInt(cardType.getLengths().size())) );
      return cardNum;
   }

   private static String generateCardNumber( final Long prefix, final int length ) {
      final StringBuffer num = new StringBuffer( prefix.toString() );

      final int howManyMore = length - num.toString().length() - 1;
      final Random random = new Random();

      for( int i = 0; i < howManyMore; i++ ) {
         num.append( Integer.valueOf(random.nextInt(9)) );
      }

      num.append( calculateCheckDigit(num.toString()) );

      return num.toString();
   }

   private static int calculateCheckDigit( final String str ) {
      final int sum = calculateLuhnSum( str, false );
      final int checkDigit = calculateCheckDigit( sum );

      return checkDigit;
   }

   private static int calculateCheckDigit( final int luhnSum ) {
      final int checkDigit = (luhnSum * 9) % 10;
      return checkDigit;
   }

   private static int calculateLuhnSum( final String str, final boolean hasCheckDigit ) {
      final int luhnNums[] = new int[str.length()];
      final int start = str.length() - (hasCheckDigit ? 2 : 1);
      int sum = 0;

      boolean doubleMe = true;

      for( int i = start; i >= 0; i-- ) {
         final int num = Integer.parseInt( str.substring(i, i + 1) );

         if( doubleMe ) {
            int x2 = num * 2;
            luhnNums[i] = x2 > 9 ? x2 - 9 : x2;
         } else {
            luhnNums[i] = num;
         }

         sum += luhnNums[i];
         doubleMe = !doubleMe;
      }

      return sum;
   }

   @SafeVarargs
   private static <T> Set<T> removeVarargDuplicates( final T ... stuff ) {
      final Set<T> set = new HashSet<>();

      if( stuff != null ) {
         for( final T cardType : stuff ) {
            if( cardType != null ) {
               set.add( cardType );
            }
         }
      }

      return set;
   }

   private static <T> T randomFromSet( final Set<T> set ) {
      T item = null;
      int random;

      if( set == null || set.size() < 1 ) throw new IllegalArgumentException( "Set is null or empty" );

      random = new Random().nextInt( set.size() );

      final Iterator<T> iterator = set.iterator();

      for( int i = 0; i <= random; i++ ) {
         item = iterator.next();
      }

      return item;
   }
}
