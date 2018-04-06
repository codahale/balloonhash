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
            -113, 34, 108, 84, -10, -40, -79, -50, 77, 15, -34, -41, -41, -6, 113, 75, 105, 52, 114,
            17, -64, 3, 31, -65, 93, 35, -48, 32, 82, -62, 63, -65);
  }

  @Test
  void parameters() throws NoSuchAlgorithmException {
    final BalloonHashM bh = new BalloonHashM("SHA-256", 1 << 6, 1 << 9, 10);
    assertThat(bh.getDigestLength()).isEqualTo(32);
    assertThat(bh.getSCost()).isEqualTo(640);
    assertThat(bh.getTCost()).isEqualTo(512);
    assertThat(bh.getPCost()).isEqualTo(10);
  }
}
