# balloonhash

A Java implementation of the [Balloon Hashing](https://crypto.stanford.edu/balloon/) algorithm.

Full paper is [here](https://eprint.iacr.org/2016/027.pdf).

## It Runs On My Laptop

```
Benchmark        (pCost)  (sCost)  (tCost)  Mode  Cnt        Score       Error  Units
Benchmarks.hash        1    16384        1  avgt   20     1950.188 ±   176.727  us/op
Benchmarks.hash        1    16384       10  avgt   20    16316.650 ±   587.262  us/op
Benchmarks.hash        1    16384      100  avgt   20   187157.098 ± 20327.191  us/op
Benchmarks.hash        1    32768        1  avgt   20     4232.839 ±   608.748  us/op
Benchmarks.hash        1    32768       10  avgt   20    34700.016 ±  1949.423  us/op
Benchmarks.hash        1    32768      100  avgt   20   323211.751 ± 11923.827  us/op
Benchmarks.hash        1    65536        1  avgt   20     7025.093 ±   124.585  us/op
Benchmarks.hash        1    65536       10  avgt   20    67869.719 ±  3910.278  us/op
Benchmarks.hash        1    65536      100  avgt   20   634982.519 ±  6080.598  us/op
Benchmarks.hash        2    16384        1  avgt   20     2278.077 ±   329.609  us/op
Benchmarks.hash        2    16384       10  avgt   20    20054.696 ±  3978.163  us/op
Benchmarks.hash        2    16384      100  avgt   20   182462.360 ± 19276.830  us/op
Benchmarks.hash        2    32768        1  avgt   20     3694.852 ±    59.264  us/op
Benchmarks.hash        2    32768       10  avgt   20    34996.614 ±  1428.547  us/op
Benchmarks.hash        2    32768      100  avgt   20   346435.443 ±  7948.153  us/op
Benchmarks.hash        2    65536        1  avgt   20     7338.098 ±   147.979  us/op
Benchmarks.hash        2    65536       10  avgt   20    69695.124 ±  2219.025  us/op
Benchmarks.hash        2    65536      100  avgt   20   706004.461 ± 20058.820  us/op
Benchmarks.hash        4    16384        1  avgt   20     3687.478 ±    65.059  us/op
Benchmarks.hash        4    16384       10  avgt   20    35517.362 ±  2907.763  us/op
Benchmarks.hash        4    16384      100  avgt   20   361960.295 ± 42505.221  us/op
Benchmarks.hash        4    32768        1  avgt   20     7668.689 ±   684.894  us/op
Benchmarks.hash        4    32768       10  avgt   20    65602.941 ±   630.975  us/op
Benchmarks.hash        4    32768      100  avgt   20   657004.965 ±  6239.871  us/op
Benchmarks.hash        4    65536        1  avgt   20    14655.729 ±   454.198  us/op
Benchmarks.hash        4    65536       10  avgt   20   133752.186 ±  4013.978  us/op
Benchmarks.hash        4    65536      100  avgt   20  1321554.166 ± 18856.317  us/op
Benchmarks.hash        8    16384        1  avgt   20     7219.033 ±   114.280  us/op
Benchmarks.hash        8    16384       10  avgt   20    65421.112 ±  1200.678  us/op
Benchmarks.hash        8    16384      100  avgt   20   656377.529 ± 16653.710  us/op
Benchmarks.hash        8    32768        1  avgt   20    14469.046 ±   217.888  us/op
Benchmarks.hash        8    32768       10  avgt   20   129175.352 ±  1028.344  us/op
Benchmarks.hash        8    32768      100  avgt   20  1303250.136 ± 23058.349  us/op
Benchmarks.hash        8    65536        1  avgt   20    29046.276 ±   835.051  us/op
Benchmarks.hash        8    65536       10  avgt   20   260338.616 ±  3504.806  us/op
Benchmarks.hash        8    65536      100  avgt   20  2598822.868 ± 20996.839  us/op
```