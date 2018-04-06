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

import com.codahale.balloonhash.BalloonHashM;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

class BalloonHashMTest {

  @Test
  void hashingPasswords() throws NoSuchAlgorithmException {
    final byte[] password = "this is a good password".getBytes(StandardCharsets.UTF_8);
    final byte[] salt = "this is a good salt".getBytes(StandardCharsets.UTF_8);
    final BalloonHashM bh = new BalloonHashM("SHA-256", 1 << 6, 1 << 9, 10);
    final byte[] actual = bh.hash(password, salt);
    assertThat(actual)
        .containsExactly(
            -36, 64, 17, -2, 29, 109, -59, -98, 17, 38, -72, 13, -26, -53, -23, 123, -53, 7, -96,
            -100, 12, -27, 33, -80, -55, 30, 18, -117, -9, -71, 91, -101);
  }

  @Test
  void digestLength() throws NoSuchAlgorithmException {
    final BalloonHashM bh = new BalloonHashM("SHA-256", 1 << 6, 1 << 9, 10);
    assertThat(bh.getDigestLength()).isEqualTo(32);
  }

  @Test
  void memoryUsage() throws NoSuchAlgorithmException {
    final BalloonHashM bh = new BalloonHashM("SHA-256", 1 << 6, 1 << 9, 10);
    assertThat(bh.getMemoryUsage()).isEqualTo(20480);
  }
}
