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
    final BalloonHash bh = new BalloonHash("SHA-256", 1 << 11, 1 << 9, 1);
    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            2, 84, 26, -93, -92, -37, -105, -115, 7, 36, 120, 43, -33, -82, -98, -42, 44, 121, -97,
            25, -70, -70, -70, -15, -25, 61, -78, 28, -46, -54, -72, 67);
  }

  @Test
  void oddSpaceCost() throws NoSuchAlgorithmException {
    final BalloonHash bh = new BalloonHash("SHA-256", 65, 1 << 9, 1);
    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            27, 68, 89, -102, -82, -19, 29, -72, -123, 1, 92, -21, 7, 92, 29, 69, -27, -78, -71,
            -56, 26, 116, 5, -120, 2, -81, 49, -38, 58, 7, 117, 6);
  }

  @Test
  void parallelism() throws NoSuchAlgorithmException {
    final BalloonHash bh = new BalloonHash("SHA-256", 1 << 11, 1 << 9, 10);
    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            43, 18, 16, -7, 76, -44, 22, 113, 124, 0, -37, -57, -88, 4, -114, -30, -118, 48, -52,
            67, 8, -127, -29, -120, -16, -3, -77, -87, 111, -23, 39, -40);
  }

  @Test
  void parameters() throws NoSuchAlgorithmException {
    final BalloonHash bh = new BalloonHash("SHA-256", 1024, 10, 1);
    assertThat(bh.getDigestLength()).isEqualTo(32);
    assertThat(bh.getSCost()).isEqualTo(1024);
    assertThat(bh.getTCost()).isEqualTo(10);
    assertThat(bh.getPCost()).isEqualTo(1);
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
    assertThatThrownBy(() -> new BalloonHash("SHA-512", 1, 1, 1))
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
