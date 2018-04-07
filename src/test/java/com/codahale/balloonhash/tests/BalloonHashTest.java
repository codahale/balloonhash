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
            -115, -67, -79, 56, -23, 87, 53, -117, 45, 109, -77, 82, -73, 12, 48, -75, -54, 48, 95,
            -121, 46, -61, 121, -23, -4, -44, -93, 72, 4, 6, -99, -53);
  }

  @Test
  void parallelism() throws NoSuchAlgorithmException {
    final BalloonHash bh = new BalloonHash("SHA-256", 1 << 11, 1 << 9, 10);
    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            107, 37, 126, 29, 78, 77, 95, 36, -74, 94, -10, 117, -89, 84, -39, 35, 101, 13, -108, 7,
            -38, 97, -20, 26, -12, -16, -85, 15, -37, -1, 51, -32);
  }

  @Test
  void parameters() throws NoSuchAlgorithmException {
    final BalloonHash bh = new BalloonHash("SHA-256", 1024, 10, 1);
    assertThat(bh.getDigestLength()).isEqualTo(32);
    assertThat(bh.getSCost()).isEqualTo(1024);
    assertThat(bh.getTCost()).isEqualTo(10);
    assertThat(bh.getPCost()).isEqualTo(1);
  }
}
