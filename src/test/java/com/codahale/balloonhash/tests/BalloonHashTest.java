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
    final BalloonHash bh = new BalloonHash("SHA-256", 1 << 6, 1 << 9);
    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            -57, -79, -41, -4, 0, 83, 85, 53, 119, -93, 62, 104, -46, 127, -87, 69, 15, -112, -10,
            -47, -101, -70, 124, 116, 96, 91, 87, -124, 21, 0, -89, -75);
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
