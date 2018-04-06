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

  @Test
  void hashingPasswords() throws NoSuchAlgorithmException {
    final byte[] password = "this is a good password".getBytes(StandardCharsets.UTF_8);
    final byte[] salt = "this is a good salt".getBytes(StandardCharsets.UTF_8);
    final BalloonHash bh = new BalloonHash("SHA-256", 1 << 11, 1 << 9);
    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            -97, -85, -88, 40, -84, -39, -104, -75, -33, -13, 24, 113, -33, 18, 100, 75, 118, 79,
            28, 61, -112, -14, -17, -41, 81, 15, 89, 6, 70, 45, -3, -61);
  }

  @Test
  void digestLength() throws NoSuchAlgorithmException {
    final BalloonHash bh = new BalloonHash("SHA-256", 1024, 1);
    assertThat(bh.getDigestLength()).isEqualTo(32);
  }

  @Test
  void memoryUsage() throws NoSuchAlgorithmException {
    final BalloonHash bh = new BalloonHash("SHA-256", 1024, 1);
    assertThat(bh.getMemoryUsage()).isEqualTo(32768);
  }
}
