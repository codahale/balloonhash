# balloonhash

A Java implementation of the [Balloon Hashing](https://crypto.stanford.edu/balloon/) algorithm.

Full paper is [here](https://eprint.iacr.org/2016/027.pdf).

## It Runs On My Laptop

```
Benchmark        (pCost)  (sCost)  (tCost)  Mode  Cnt        Score        Error  Units
Benchmarks.hash        1    16384        1  avgt   20     1302.747 ±     27.825  us/op
Benchmarks.hash        1    16384       10  avgt   20    11803.477 ±    220.127  us/op
Benchmarks.hash        1    16384      100  avgt   20   123765.412 ±   5229.388  us/op
Benchmarks.hash        1    32768        1  avgt   20     2866.452 ±    100.132  us/op
Benchmarks.hash        1    32768       10  avgt   20    24441.100 ±    585.228  us/op
Benchmarks.hash        1    32768      100  avgt   20   236999.584 ±   3347.721  us/op
Benchmarks.hash        1    65536        1  avgt   20     5298.413 ±    153.520  us/op
Benchmarks.hash        1    65536       10  avgt   20    48900.735 ±   2199.724  us/op
Benchmarks.hash        1    65536      100  avgt   20   466784.845 ±   3515.073  us/op
Benchmarks.hash        2    16384        1  avgt   20     1452.474 ±     16.245  us/op
Benchmarks.hash        2    16384       10  avgt   20    12889.956 ±    159.371  us/op
Benchmarks.hash        2    16384      100  avgt   20   130104.540 ±   4373.776  us/op
Benchmarks.hash        2    32768        1  avgt   20     2819.216 ±     41.330  us/op
Benchmarks.hash        2    32768       10  avgt   20    25854.120 ±    501.021  us/op
Benchmarks.hash        2    32768      100  avgt   20   254844.835 ±   3260.042  us/op
Benchmarks.hash        2    65536        1  avgt   20     5977.668 ±    118.370  us/op
Benchmarks.hash        2    65536       10  avgt   20    52896.898 ±    728.306  us/op
Benchmarks.hash        2    65536      100  avgt   20   541780.547 ±  13540.110  us/op
Benchmarks.hash        4    16384        1  avgt   20     2945.484 ±     93.198  us/op
Benchmarks.hash        4    16384       10  avgt   20    24527.547 ±    342.342  us/op
Benchmarks.hash        4    16384      100  avgt   20   242056.082 ±   6248.123  us/op
Benchmarks.hash        4    32768        1  avgt   20     5804.524 ±     93.181  us/op
Benchmarks.hash        4    32768       10  avgt   20    48819.061 ±    632.693  us/op
Benchmarks.hash        4    32768      100  avgt   20   475733.915 ±   7293.469  us/op
Benchmarks.hash        4    65536        1  avgt   20    11128.475 ±    144.092  us/op
Benchmarks.hash        4    65536       10  avgt   20    96668.524 ±   2011.351  us/op
Benchmarks.hash        4    65536      100  avgt   20   966115.990 ±  34608.087  us/op
Benchmarks.hash        8    16384        1  avgt   20     5549.935 ±    194.879  us/op
Benchmarks.hash        8    16384       10  avgt   20    48792.783 ±    591.732  us/op
Benchmarks.hash        8    16384      100  avgt   20   490619.757 ±  13011.951  us/op
Benchmarks.hash        8    32768        1  avgt   20    11023.311 ±    140.705  us/op
Benchmarks.hash        8    32768       10  avgt   20    98414.393 ±   1960.864  us/op
Benchmarks.hash        8    32768      100  avgt   20   984885.873 ±  35824.511  us/op
Benchmarks.hash        8    65536        1  avgt   20    22755.232 ±    733.042  us/op
Benchmarks.hash        8    65536       10  avgt   20   197410.479 ±   4773.180  us/op
Benchmarks.hash        8    65536      100  avgt   20  1992558.968 ± 105694.721  us/op
```