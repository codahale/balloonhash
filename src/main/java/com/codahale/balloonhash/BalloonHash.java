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

/** An implementation of the {@link BalloonHash} algorithm. */
public class BalloonHash {

  private static final byte[] NULL = new byte[0];

  private final String algorithm;
  private final int sCost, tCost;
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
   * @throws NoSuchAlgorithmException if no {@code Provider} supports a {@code MessageDigestSpi}
   *     implementation for the specified algorithm
   */
  public BalloonHash(String algorithm, int sCost, int tCost) throws NoSuchAlgorithmException {
    this.algorithm = algorithm;
    this.sCost = sCost;
    this.tCost = tCost;
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
   * Hashes the given password and salt.
   *
   * @param password a password of arbitrary length
   * @param salt a salt of arbitrary length
   * @return the hash balloon digest
   */
  public byte[] hash(byte[] password, byte[] salt) {
    final int delta = 3;
    int cnt = 0;
    final int blockCount = sCost / digestLength;
    final byte[][] blocks = new byte[blockCount][digestLength];

    // Step 1. Expand input into buffer.
    blocks[0] = hash(cnt++, password, seed(salt));
    for (int i = 1; i < blockCount; i++) {
      blocks[i] = hash(cnt++, blocks[i - 1], NULL);
    }

    // Step 2. Mix buffer contents.
    for (int t = 0; t < tCost; t++) {
      for (int m = 0; m < blockCount; m++) {
        // Step 2a. Hash last and current blocks.
        final byte[] prev = blocks[mod(m - 1, blockCount)];
        blocks[m] = hash(cnt++, prev, blocks[m]);

        // Step 2b. Hash in pseudorandomly chosen blocks.
        for (int i = 0; i < delta; i++) {
          final byte[] in = new byte[12];
          in[0] = (byte) (t);
          in[1] = (byte) (t >>> 8);
          in[2] = (byte) (t >>> 16);
          in[3] = (byte) (t >>> 24);

          in[4] = (byte) (m);
          in[5] = (byte) (m >>> 8);
          in[6] = (byte) (m >>> 16);
          in[7] = (byte) (m >>> 24);

          in[8] = (byte) (i);
          in[9] = (byte) (i >>> 8);
          in[10] = (byte) (i >>> 16);
          in[11] = (byte) (i >>> 24);

          final byte[] h = hash(cnt++, salt, in);
          int idx = (h[0] & 0xff);
          idx |= (h[1] & 0xff) << 8;
          idx |= (h[2] & 0xff) << 16;
          idx |= (h[3] & 0xff) << 24;
          idx = mod(idx, blockCount);
          blocks[m] = hash(cnt++, blocks[m], blocks[idx]);
        }
      }
    }

    // Step 3. Extract output from buffer.
    return blocks[blockCount - 1];
  }

  protected byte[] seed(byte[] salt) {
    final byte[] seed = Arrays.copyOfRange(salt, 0, salt.length + 12);
    int idx = salt.length;
    seed[++idx] = (byte) (sCost);
    seed[++idx] = (byte) (sCost >>> 8);
    seed[++idx] = (byte) (sCost >>> 16);
    seed[++idx] = (byte) (sCost >>> 24);
    seed[++idx] = (byte) (tCost);
    seed[++idx] = (byte) (tCost >>> 8);
    seed[++idx] = (byte) (tCost >>> 16);
    seed[++idx] = (byte) (tCost >>> 24);
    seed[++idx] = 1; // one thread
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
