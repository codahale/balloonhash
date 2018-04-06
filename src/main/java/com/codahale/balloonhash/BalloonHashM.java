/*
 * Copyright © 2018 Coda Hale (coda.hale@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codahale.balloonhash;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.IntStream;

/** An implementation of the parallel {@link BalloonHashM} algorithm. */
public class BalloonHashM extends BalloonHash {

  private final int pCost;

  /**
   * Create a new parallel {@link BalloonHashM} instance with the given parameters.
   *
   * @param algorithm the name of the algorithm requested. See the MessageDigest section in the <a
   *     href=
   *     "https://docs.oracle.com/javase/9/docs/specs/security/standard-names.html#messagedigest-algorithms">
   *     Java Security Standard Algorithm Names Specification</a> for information about standard
   *     algorithm names.
   * @param sCost the space cost (in bytes)
   * @param tCost the time cost (in iterations)
   * @param pCost the number of threads to use
   * @throws NoSuchAlgorithmException if no {@code Provider} supports a {@code MessageDigestSpi}
   *     implementation for the specified algorithm
   */
  public BalloonHashM(String algorithm, int sCost, int tCost, int pCost)
      throws NoSuchAlgorithmException {
    super(algorithm, sCost, tCost);
    this.pCost = pCost;
  }

  @Override
  public byte[] hash(byte[] password, byte[] salt) {
    return IntStream.rangeClosed(1, pCost)
        .parallel() // parallelize work as much as possible
        .mapToObj(
            i -> {
              // add the worker number to the end of the salt as a little-endian integer
              final byte[] pSalt = Arrays.copyOf(salt, salt.length);
              int n = pSalt[pSalt.length - 4] << 24;
              n |= (pSalt[pSalt.length - 3] & 0xff) << 16;
              n |= (pSalt[pSalt.length - 2] & 0xff) << 8;
              n |= (pSalt[pSalt.length - 1] & 0xff);
              n += i;
              pSalt[0] = (byte) (n);
              pSalt[1] = (byte) (n >>> 8);
              pSalt[2] = (byte) (n >>> 16);
              pSalt[3] = (byte) (n >>> 24);

              // then hash normally
              return super.hash(password, pSalt);
            })
        .reduce(
            new byte[getDigestLength()],
            (a, b) -> {
              // combine all hashes by XORing them together
              final byte[] c = new byte[a.length];
              for (int i = 0; i < a.length; i++) {
                c[i] = (byte) (a[i] ^ b[i]);
              }
              return c;
            });
  }

  @Override
  public int getSCost() {
    return super.getSCost() * pCost;
  }

  /**
   * Returns the parallelism cost (in threads).
   *
   * @return the parallelism cost (in threads)
   */
  public int getPCost() {
    return pCost;
  }

  @Override
  protected byte[] seed(byte[] salt) {
    final byte[] seed = super.seed(salt);
    int idx = salt.length - 4;
    seed[++idx] = (byte) (pCost);
    seed[++idx] = (byte) (pCost >>> 8);
    seed[++idx] = (byte) (pCost >>> 16);
    seed[++idx] = (byte) (pCost >>> 24);
    return seed;
  }
}
