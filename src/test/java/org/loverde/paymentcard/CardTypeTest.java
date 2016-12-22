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
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;


/**
 * These tests verify that the {@linkplain CardType} enum is built properly.  Validation criteria is based on
 * <a href="https://en.wikipedia.org/wiki/Payment_card_number">https://en.wikipedia.org/wiki/Payment_card_number</a>
 * as of December, 2016.
 */
public class CardTypeTest {

   /**
    * If this test fails, the results of the other tests cannot be trusted
    */
   @Test
   public void sanityCheck_inRange() {
      final Range range_1_2 = new Range( 1, 2 );
      final Range range_3 = new Range( 3, 3 );

      final Set<Long> prefix1 = new HashSet<>();
      prefix1.add( 1L );

      assertPrefixesWithinRange( prefix1, range_1_2 );

      final Set<Long> prefix2 = new HashSet<>();
      prefix2.add( 2L );

      assertPrefixesWithinRange( prefix2, range_1_2 );

      final Set<Long> prefix3 = new HashSet<>();
      prefix3.add( 3L );

      assertPrefixesWithinRange( prefix3, range_3 );

      final Set<Long> prefixAll = new HashSet<>();
      prefixAll.addAll( prefix1 );
      prefixAll.addAll( prefix2 );
      prefixAll.addAll( prefix3 );

      assertPrefixesWithinRange( prefix3, range_1_2, range_3 );
   }

   /**
    * If this test fails, the results of the other tests cannot be trusted
    */
   @Test( expected = AssertionError.class )
   public void sanityCheck_notInRange() {
      final Set<Long> prefix = new HashSet<>();
      final Range range = new Range( 2, 3 );

      prefix.add( 1L );
      prefix.add( 4L );

      assertPrefixesWithinRange( prefix, range );
   }

   @Test
   public void amex() {
      final CardType type = CardType.AMERICAN_EXPRESS;
      final List<Integer> lengths = type.getLengths();
      final Set<Long> prefixes = type.getPrefixes();

      assertEquals( 1, lengths.size() );
      assertTrue( lengths.contains(15) );

      assertEquals( 2, prefixes.size() );
      assertTrue( prefixes.contains(34L) );
      assertTrue( prefixes.contains(37L) );
   }

   @Test
   public void visa() {
      final CardType type = CardType.VISA;
      final List<Integer> lengths = type.getLengths();
      final Set<Long> prefixes = type.getPrefixes();

      assertEquals( 3, lengths.size() );
      assertTrue( lengths.contains(13) );
      assertTrue( lengths.contains(16) );
      assertTrue( lengths.contains(19) );

      assertEquals( 1, prefixes.size() );
      assertTrue( prefixes.contains(4L) );
   }

   @Test
   public void mastercard() {
      final CardType type = CardType.MASTERCARD;
      final List<Integer> lengths = type.getLengths();
      final Set<Long> prefixes = type.getPrefixes();

      assertEquals( 1, lengths.size() );
      assertTrue( lengths.contains(16) );

      final Range prefixRange_51_55 = new Range(51, 55),
                  prefixRange_2221_2720 = new Range( 2221, 2720 );

      assertEquals( prefixRange_51_55.size() + prefixRange_2221_2720.size(),
                    prefixes.size() );

      assertPrefixesWithinRange( prefixes, prefixRange_51_55, prefixRange_2221_2720 );
   }

   @Test
   public void discover() {
      final CardType type = CardType.DISCOVER;
      final List<Integer> lengths = type.getLengths();
      final Set<Long> prefixes = type.getPrefixes();

      assertEquals( 2, lengths.size() );
      assertTrue( lengths.contains(16) );
      assertTrue( lengths.contains(19) );

      final Range prefixRange_65_65 = new Range( 65, 65 ),
                  prefixRange_644_649 = new Range( 644, 649 ),
                  prefixRange_6011_6011 = new Range( 6011, 6011 ),
                  prefixRange_622126_622925 = new Range( 622126, 622925 );

      assertEquals( prefixRange_65_65.size() +
                    prefixRange_644_649.size() +
                    prefixRange_6011_6011.size() +
                    prefixRange_622126_622925.size()
                    , prefixes.size() );

      assertPrefixesWithinRange( prefixes, prefixRange_65_65, prefixRange_644_649, prefixRange_6011_6011, prefixRange_622126_622925 );
   }

   @Test( expected = UnsupportedOperationException.class )
   public void alterPrefixes_add() {
      CardType.AMERICAN_EXPRESS.getPrefixes().add( 14352L );
   }

   @Test( expected = UnsupportedOperationException.class )
   public void alterPrefixes_clear() {
      CardType.DISCOVER.getPrefixes().clear();
   }

   @Test( expected = UnsupportedOperationException.class )
   public void alterPrefixes_remove() {
      final Iterator<Long> iterator = CardType.MASTERCARD.getPrefixes().iterator();
      iterator.next();
      iterator.remove();
   }

   @Test( expected = UnsupportedOperationException.class )
   public void alterLengths_add() {
      CardType.AMERICAN_EXPRESS.getLengths().add( 12 );
   }

   @Test( expected = UnsupportedOperationException.class )
   public void alterLengths_clear() {
      CardType.DISCOVER.getLengths().clear();
   }

   @Test( expected = UnsupportedOperationException.class )
   public void alterLengths_remove() {
      final Iterator<Integer> iterator = CardType.MASTERCARD.getLengths().iterator();
      iterator.next();
      iterator.remove();
   }


   private static void assertPrefixesWithinRange( final Set<Long> prefixes, final Range ... ranges ) {
      if( prefixes == null || prefixes.size() < 1 ) throw new IllegalArgumentException( "No prefixes provided" );
      if( ranges == null || ranges.length < 1 ) throw new IllegalArgumentException( "No range provided" );

      for( final Long prefix : prefixes ) {
         boolean found = false;

         for( final Range range : ranges ) {
            for( long rangeElement = range.getStart(); rangeElement <= range.getEnd(); rangeElement++ ) {
               if( prefix.longValue() == rangeElement ) {
                  found = true;
                  break;
               }
            }

         }

         assertTrue( "Didn't find prefix " + prefix, found );
      }
   }
}
