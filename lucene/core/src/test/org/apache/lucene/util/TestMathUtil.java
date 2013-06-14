package org.apache.lucene.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.math.BigInteger;
import java.util.Arrays;

import com.carrotsearch.randomizedtesting.generators.RandomPicks;

public class TestMathUtil extends LuceneTestCase {

  static long[] PRIMES = new long[] {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};

  static long randomLong() {
    if (random().nextBoolean()) {
      long l = 1;
      if (random().nextBoolean()) {
        l *= -1;
      }
      for (long i : PRIMES) {
        final int m = random().nextInt(3);
        for (int j = 0; j < m; ++j) {
          l *= i;
        }
      }
      return l;
    } else if (random().nextBoolean()) {
      return random().nextLong();
    } else {
      return RandomPicks.randomFrom(random(), Arrays.asList(Long.MIN_VALUE, Long.MAX_VALUE, 0L, -1L, 1L));
    }
  }

  // slow version used for testing
  static long gcd(long l1, long l2) {
    final BigInteger gcd = BigInteger.valueOf(l1).gcd(BigInteger.valueOf(l2));
    assert gcd.bitCount() <= 64;
    return gcd.longValue();
  }

  public void testGCD() {
    final int iters = atLeast(100);
    for (int i = 0; i < iters; ++i) {
      final long l1 = randomLong();
      final long l2 = randomLong();
      final long gcd = MathUtil.gcd(l1, l2);
      final long actualGcd = gcd(l1, l2);
      assertEquals(actualGcd, gcd);
      if (gcd != 0) {
        assertEquals(l1, (l1 / gcd) * gcd);
        assertEquals(l2, (l2 / gcd) * gcd);
      }
    }
  }
  
  // ported test from commons-math
  public void testGCD2() {
    long a = 30;
    long b = 50;
    long c = 77;
    
    assertEquals(0, MathUtil.gcd(0, 0));
    assertEquals(b, MathUtil.gcd(0, b));
    assertEquals(a, MathUtil.gcd(a, 0));
    assertEquals(b, MathUtil.gcd(0, -b));
    assertEquals(a, MathUtil.gcd(-a, 0));
    
    assertEquals(10, MathUtil.gcd(a, b));
    assertEquals(10, MathUtil.gcd(-a, b));
    assertEquals(10, MathUtil.gcd(a, -b));
    assertEquals(10, MathUtil.gcd(-a, -b));
    
    assertEquals(1, MathUtil.gcd(a, c));
    assertEquals(1, MathUtil.gcd(-a, c));
    assertEquals(1, MathUtil.gcd(a, -c));
    assertEquals(1, MathUtil.gcd(-a, -c));
    
    assertEquals(3L * (1L<<45), MathUtil.gcd(3L * (1L<<50), 9L * (1L<<45)));
    assertEquals(1L<<45, MathUtil.gcd(1L<<45, Long.MIN_VALUE));
    
    assertEquals(Long.MAX_VALUE, MathUtil.gcd(Long.MAX_VALUE, 0L));
    assertEquals(Long.MAX_VALUE, MathUtil.gcd(-Long.MAX_VALUE, 0L));
    assertEquals(1, MathUtil.gcd(60247241209L, 153092023L));
    
    assertEquals(Long.MIN_VALUE, MathUtil.gcd(Long.MIN_VALUE, 0));
    assertEquals(Long.MIN_VALUE, MathUtil.gcd(0, Long.MIN_VALUE));
    assertEquals(Long.MIN_VALUE, MathUtil.gcd(Long.MIN_VALUE, Long.MIN_VALUE));
  }

}
