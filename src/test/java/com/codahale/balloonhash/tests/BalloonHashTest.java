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
package com.codahale.balloonhash.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codahale.balloonhash.BalloonHash;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

class BalloonHashTest {

  private final MessageDigest SHA_256;
  private final byte[] password = "this is a good password".getBytes(StandardCharsets.UTF_8);
  private final byte[] salt = "this is a good salt".getBytes(StandardCharsets.UTF_8);

  BalloonHashTest() throws NoSuchAlgorithmException {
    this.SHA_256 = MessageDigest.getInstance("SHA-256");
  }

  @Test
  void hashingPasswords() {
    final BalloonHash bh = new BalloonHash(SHA_256, 1 << 6, 1 << 9, 1);
    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            90, -33, -15, -5, 72, 110, -60, 30, 66, -122, -60, -64, 68, -19, 48, 74, 51, -112, -120,
            -42, 55, -51, 89, 120, 24, -41, 93, -24, 0, -1, -53, -23);
  }

  @Test
  void oddSpaceCost() {
    final BalloonHash bh = new BalloonHash(SHA_256, 65, 1 << 9, 1);
    assertThat(bh.n()).isEqualTo(66);

    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            118, 73, 93, -21, -65, -87, 112, -9, 12, 111, -106, -23, 22, -30, -52, -2, 27, -82, -91,
            63, -98, -115, 9, 0, -39, 49, -51, -49, 26, -110, -9, 35);
  }

  @Test
  void parallelism() {
    final BalloonHash bh = new BalloonHash(SHA_256, 1 << 6, 1 << 9, 10);
    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            -33, -107, 53, 8, -5, 42, -121, -56, 40, -63, 103, -77, -1, 5, 82, -100, -47, -122, 56,
            -85, 72, -35, 59, 112, 12, -26, -12, -121, -112, 118, -67, 106);
  }

  @Test
  void parameters() {
    final BalloonHash bh = new BalloonHash(SHA_256, 1024, 10, 1);
    assertThat(bh.digestLength()).isEqualTo(32);
    assertThat(bh.n()).isEqualTo(1024);
    assertThat(bh.r()).isEqualTo(10);
    assertThat(bh.p()).isEqualTo(1);
    assertThat(bh.memoryUsage()).isEqualTo(32768);
  }

  @Test
  void shortSalt() {
    final BalloonHash bh = new BalloonHash(SHA_256, 1024, 1, 1);
    assertThatThrownBy(() -> bh.hash(new byte[12], new byte[3]))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void badSpaceCost() {
    assertThatThrownBy(() -> new BalloonHash(SHA_256, -1, 1, 1))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void badTimeCost() {
    assertThatThrownBy(() -> new BalloonHash(SHA_256, 1024, 0, 1))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void badParallelismCost() {
    assertThatThrownBy(() -> new BalloonHash(SHA_256, 1024, 1, 0))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
