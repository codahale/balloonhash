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
package com.codahale.balloonhash.benchmarks;

import com.codahale.balloonhash.BalloonHash;
import com.codahale.balloonhash.BalloonHashM;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;

@State(Scope.Benchmark)
public class Benchmarks {
  public static void main(String[] args) throws IOException, RunnerException {
    Main.main(args);
  }

  private static BalloonHashM newBHM() {
    try {
      return new BalloonHashM("SHA-512", 1 << 14, 5, 4);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  private static BalloonHash newBH() {
    try {
      return new BalloonHash("SHA-512", 1 << 14, 5);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  private final byte[] password = new byte[30];
  private final byte[] salt = new byte[32];
  private final BalloonHash bh = newBH();
  private final BalloonHashM bhm = newBHM();

  @Benchmark
  public byte[] hash() {
    return bh.hash(password, salt);
  }

  @Benchmark
  public byte[] hashM() {
    return bhm.hash(password, salt);
  }
}
