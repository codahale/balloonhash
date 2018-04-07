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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.IntStream;

/** An implementation of the {@link BalloonHash} algorithm. */
public class BalloonHash {

  private static final byte[] NULL = new byte[0]; // number of dependencies per block / graph depth
  private static final int DELTA = 3;

  private final String algorithm;
  private final int sCost, tCost, pCost;
  private final int digestLength;

  /**
   * Create a new {@link BalloonHash} instance with the given parameters.
   *
   * @param algorithm the name of the algorithm requested. See the MessageDigest section in the <a
   *     href=
   *     "https://docs.oracle.com/javase/9/docs/specs/security/standard-names.html#messagedigest-algorithms">
   *     Java Security Standard Algorithm Names Specification</a> for information about standard
   *     algorithm names.
   * @param sCost the space cost (in bytes)
   * @param tCost the time cost (in iterations)
   * @param pCost the parallelism cost (in threads)
   * @throws NoSuchAlgorithmException if no {@code Provider} supports a {@code MessageDigestSpi}
   *     implementation for the specified algorithm
   */
  public BalloonHash(String algorithm, int sCost, int tCost, int pCost)
      throws NoSuchAlgorithmException {
    this.algorithm = algorithm;
    this.sCost = sCost;
    this.tCost = tCost;
    this.pCost = pCost;
    this.digestLength = MessageDigest.getInstance(algorithm).getDigestLength();
  }

  /**
   * Returns the length of the resulting digest (in bytes).
   *
   * @return the length of the resulting digest (in bytes)
   */
  public int getDigestLength() {
    return digestLength;
  }

  /**
   * Returns the space cost (in bytes).
   *
   * @return the space cost (in bytes)
   */
  public int getSCost() {
    return sCost;
  }

  /**
   * Returns the time cost (in iterations).
   *
   * @return the time cost (in iterations)
   */
  public int getTCost() {
    return tCost;
  }

  /**
   * Returns the parallelism cost (in threads).
   *
   * @return the parallelism cost (in threads)
   */
  public int getPCost() {
    return pCost;
  }

  /**
   * Hashes the given password and salt.
   *
   * @param password a password of arbitrary length
   * @param salt a salt of at least 4 bytes
   * @return the hash balloon digest
   */
  public byte[] hash(byte[] password, byte[] salt) {
    if (salt.length < 4) {
      throw new IllegalArgumentException("salt must be at least 4 bytes long");
    }

    if (pCost == 1) {
      return singleHash(password, seed(salt, 1));
    }

    return IntStream.rangeClosed(1, pCost)
        .parallel()
        .mapToObj(i -> singleHash(password, seed(salt, i)))
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

  private byte[] singleHash(byte[] password, byte[] seed) {
    int cnt = 0; // the counter used in the security proof
    final byte[][] buf = new byte[sCost / digestLength][digestLength];

    // Step 1. Expand input into buffer.
    buf[0] = hash(cnt++, password, seed);
    for (int i = 1; i < buf.length; i++) {
      buf[i] = hash(cnt++, buf[i - 1], NULL);
    }

    // Step 2. Mix buffer contents.
    for (int t = 0; t < tCost; t++) {
      for (int m = 0; m < buf.length; m++) {
        // Step 2a. Hash last and current blocks.
        final byte[] prev = buf[mod(m - 1, buf.length)];
        buf[m] = hash(cnt++, prev, buf[m]);

        // Step 2b. Hash in pseudorandomly chosen blocks.
        for (int i = 0; i < DELTA; i++) {
          final byte[] idxBlock = new byte[12];
          idxBlock[0] = (byte) (t);
          idxBlock[1] = (byte) (t >>> 8);
          idxBlock[2] = (byte) (t >>> 16);
          idxBlock[3] = (byte) (t >>> 24);
          idxBlock[4] = (byte) (m);
          idxBlock[5] = (byte) (m >>> 8);
          idxBlock[6] = (byte) (m >>> 16);
          idxBlock[7] = (byte) (m >>> 24);
          idxBlock[8] = (byte) (i);
          idxBlock[9] = (byte) (i >>> 8);
          idxBlock[10] = (byte) (i >>> 16);
          idxBlock[11] = (byte) (i >>> 24);

          final byte[] h = hash(cnt++, seed, idxBlock);
          int other = (h[0] & 0xff);
          other |= (h[1] & 0xff) << 8;
          other |= (h[2] & 0xff) << 16;
          other |= (h[3] & 0xff) << 24;
          other = mod(other, buf.length);

          buf[m] = hash(cnt++, buf[m], buf[other]);
        }
      }
    }

    // Step 3. Extract output from buffer.
    return buf[buf.length - 1];
  }

  private byte[] seed(byte[] salt, int i) {
    final byte[] seed = Arrays.copyOfRange(salt, 0, salt.length + 12);

    // increment first four bytes with worker number
    int n = (seed[0] & 0xff);
    n |= (seed[1] & 0xff) << 8;
    n |= (seed[2] & 0xff) << 16;
    n |= (seed[3] & 0xff) << 24;
    n += i;
    seed[0] = (byte) (n);
    seed[1] = (byte) (n >>> 8);
    seed[2] = (byte) (n >>> 16);
    seed[3] = (byte) (n >>> 24);

    // add parameters
    int idx = salt.length;
    seed[idx++] = (byte) (sCost);
    seed[idx++] = (byte) (sCost >>> 8);
    seed[idx++] = (byte) (sCost >>> 16);
    seed[idx++] = (byte) (sCost >>> 24);
    seed[idx++] = (byte) (tCost);
    seed[idx++] = (byte) (tCost >>> 8);
    seed[idx++] = (byte) (tCost >>> 16);
    seed[idx++] = (byte) (tCost >>> 24);
    seed[idx++] = (byte) (pCost);
    seed[idx++] = (byte) (pCost >>> 8);
    seed[idx++] = (byte) (pCost >>> 16);
    seed[idx] = (byte) (pCost >>> 24);

    return seed;
  }

  private byte[] hash(int cnt, byte[] block, byte[] other) {
    try {
      final MessageDigest h = MessageDigest.getInstance(algorithm);
      h.update((byte) (cnt));
      h.update((byte) (cnt >>> 8));
      h.update((byte) (cnt >>> 16));
      h.update((byte) (cnt >>> 24));
      h.update(block);
      h.update(other);
      return h.digest();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  private static int mod(int dividend, int divisor) {
    return (int) ((dividend & 0xffffffffL) % (divisor & 0xffffffffL));
  }
}
