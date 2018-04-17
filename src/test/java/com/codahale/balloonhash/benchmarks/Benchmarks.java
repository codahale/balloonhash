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
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class Benchmarks {
  public static void main(String[] args) throws IOException, RunnerException {
    Main.main(args);
  }

  private final byte[] password = new byte[30];
  private final byte[] salt = new byte[32];

  @SuppressWarnings("NullAway.Init")
  private BalloonHash bh;

  @Param({"32", "64", "128"})
  public int n = 32;

  @Param({"1", "10", "100"})
  public int r = 1;

  @Param({"1", "2", "4"})
  public int p = 1;

  @Setup
  public void setup() throws NoSuchAlgorithmException {
    this.bh = new BalloonHash(MessageDigest.getInstance("SHA-512"), n, r, p);
  }

  @Benchmark
  public byte[] hash() {
    return bh.hash(password, salt);
  }
}
