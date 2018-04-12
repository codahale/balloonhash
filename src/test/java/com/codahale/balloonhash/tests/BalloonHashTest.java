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
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

class BalloonHashTest {

  private final byte[] password = "this is a good password".getBytes(StandardCharsets.UTF_8);
  private final byte[] salt = "this is a good salt".getBytes(StandardCharsets.UTF_8);

  @Test
  void hashingPasswords() throws NoSuchAlgorithmException {
    final BalloonHash bh = new BalloonHash("SHA-256", 1 << 6, 1 << 9, 1);
    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            101, -8, -37, 117, 116, 83, 106, 116, -84, 55, -95, -58, 101, 22, 115, -110, 123, -54,
            30, -103, -4, 67, 110, -10, -47, -113, -62, -103, 116, 123, -128, 75);
  }

  @Test
  void oddSpaceCost() throws NoSuchAlgorithmException {
    final BalloonHash bh = new BalloonHash("SHA-256", 65, 1 << 9, 1);
    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            -26, 60, -115, -30, -21, -40, -68, -54, -102, -27, 36, 36, 3, 4, -51, -98, -55, 24, 38,
            41, -27, -91, 20, -100, -23, -19, -100, -73, -77, 2, -79, 28);
  }

  @Test
  void parallelism() throws NoSuchAlgorithmException {
    final BalloonHash bh = new BalloonHash("SHA-256", 1 << 6, 1 << 9, 10);
    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            -15, 57, 43, 109, -61, 52, -32, -90, -61, -42, 18, -15, -102, 108, -103, -93, -106, 46,
            -46, -1, 15, 72, 112, -14, 57, 40, 70, -49, 55, 126, 24, -116);
  }

  @Test
  void parameters() throws NoSuchAlgorithmException {
    final BalloonHash bh = new BalloonHash("SHA-256", 1024, 10, 1);
    assertThat(bh.digestLength()).isEqualTo(32);
    assertThat(bh.n()).isEqualTo(1024);
    assertThat(bh.r()).isEqualTo(10);
    assertThat(bh.p()).isEqualTo(1);
    assertThat(bh.memoryUsage()).isEqualTo(32768);
  }

  @Test
  void shortSalt() throws NoSuchAlgorithmException {
    final BalloonHash bh = new BalloonHash("SHA-256", 1024, 1, 1);
    assertThatThrownBy(() -> bh.hash(new byte[12], new byte[3]))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void badAlgorithm() {
    assertThatThrownBy(() -> new BalloonHash("YES", 1024, 1, 1))
        .isInstanceOf(NoSuchAlgorithmException.class);
  }

  @Test
  void badSpaceCost() {
    assertThatThrownBy(() -> new BalloonHash("SHA-512", -1, 1, 1))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void badTimeCost() {
    assertThatThrownBy(() -> new BalloonHash("SHA-512", 1024, 0, 1))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void badParallelismCost() {
    assertThatThrownBy(() -> new BalloonHash("SHA-512", 1024, 1, 0))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
