# balloonhash

A Java implementation of the [Balloon Hashing](https://crypto.stanford.edu/balloon/) algorithm.

Full paper is [here](https://eprint.iacr.org/2016/027.pdf).

## It Runs On My Laptop

```
Benchmark        (pCost)  (sCost)  (tCost)  Mode  Cnt        Score        Error  Units
Benchmarks.hash        1    16384        1  avgt   20     1733.604 ±     20.413  us/op
Benchmarks.hash        1    16384       10  avgt   20    15841.668 ±    142.792  us/op
Benchmarks.hash        1    16384      100  avgt   20   157949.271 ±   2048.015  us/op
Benchmarks.hash        1    32768        1  avgt   20     3408.747 ±     22.581  us/op
Benchmarks.hash        1    32768       10  avgt   20    31919.152 ±    661.482  us/op
Benchmarks.hash        1    32768      100  avgt   20   320829.314 ±   5454.548  us/op
Benchmarks.hash        1    65536        1  avgt   20     6913.930 ±    105.471  us/op
Benchmarks.hash        1    65536       10  avgt   20    65177.129 ±   1259.495  us/op
Benchmarks.hash        1    65536      100  avgt   20   628639.296 ±   5100.802  us/op
Benchmarks.hash        2    16384        1  avgt   20     2710.312 ±     85.583  us/op
Benchmarks.hash        2    16384       10  avgt   20    24513.566 ±    888.163  us/op
Benchmarks.hash        2    16384      100  avgt   20   253636.768 ±  29812.947  us/op
Benchmarks.hash        2    32768        1  avgt   20     5433.139 ±    185.455  us/op
Benchmarks.hash        2    32768       10  avgt   20    49771.167 ±   2643.394  us/op
Benchmarks.hash        2    32768      100  avgt   20   512605.472 ±  80745.693  us/op
Benchmarks.hash        2    65536        1  avgt   20    10636.285 ±    247.705  us/op
Benchmarks.hash        2    65536       10  avgt   20    99040.605 ±   5889.421  us/op
Benchmarks.hash        2    65536      100  avgt   20  1198894.735 ± 119447.247  us/op
Benchmarks.hash        4    16384        1  avgt   20     4531.136 ±     95.767  us/op
Benchmarks.hash        4    16384       10  avgt   20    40712.705 ±   1128.936  us/op
Benchmarks.hash        4    16384      100  avgt   20   383763.536 ±  21684.248  us/op
Benchmarks.hash        4    32768        1  avgt   20     8428.374 ±    282.328  us/op
Benchmarks.hash        4    32768       10  avgt   20    80133.163 ±   2988.062  us/op
Benchmarks.hash        4    32768      100  avgt   20   749751.316 ±  47871.438  us/op
Benchmarks.hash        4    65536        1  avgt   20    16895.051 ±    350.351  us/op
Benchmarks.hash        4    65536       10  avgt   20   157652.641 ±   8614.870  us/op
Benchmarks.hash        4    65536      100  avgt   20  1553014.394 ± 146591.740  us/op
Benchmarks.hash        8    16384        1  avgt   20     7692.387 ±    263.901  us/op
Benchmarks.hash        8    16384       10  avgt   20    69419.462 ±   1431.497  us/op
Benchmarks.hash        8    16384      100  avgt   20   685648.100 ±  25729.746  us/op
Benchmarks.hash        8    32768        1  avgt   20    15018.784 ±    161.490  us/op
Benchmarks.hash        8    32768       10  avgt   20   140388.972 ±   3276.612  us/op
Benchmarks.hash        8    32768      100  avgt   20  1402598.611 ± 115420.714  us/op
Benchmarks.hash        8    65536        1  avgt   20    29971.605 ±    466.772  us/op
Benchmarks.hash        8    65536       10  avgt   20   281555.148 ±  12953.525  us/op
Benchmarks.hash        8    65536      100  avgt   20  2908208.379 ± 210768.678  us/op
```