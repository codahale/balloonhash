# balloonhash

A Java implementation of the [Balloon Hashing](https://crypto.stanford.edu/balloon/) algorithm.

Full paper is [here](https://eprint.iacr.org/2016/027.pdf).

## It Runs On My Laptop

```
Benchmark        (n)  (p)  (r)  Mode  Cnt       Score      Error  Units
Benchmarks.hash   32    1    1  avgt  200     213.235 ±    1.532  us/op
Benchmarks.hash   32    1   10  avgt  200    1889.376 ±    7.469  us/op
Benchmarks.hash   32    1  100  avgt  200   18746.237 ±   69.552  us/op
Benchmarks.hash   32    2    1  avgt  200     255.329 ±    2.923  us/op
Benchmarks.hash   32    2   10  avgt  200    2041.328 ±   58.318  us/op
Benchmarks.hash   32    2  100  avgt  200   18987.226 ±  235.335  us/op
Benchmarks.hash   32    4    1  avgt  200     456.587 ±    4.775  us/op
Benchmarks.hash   32    4   10  avgt  200    4052.261 ±  155.758  us/op
Benchmarks.hash   32    4  100  avgt  200   37627.115 ±  270.478  us/op
Benchmarks.hash   64    1    1  avgt  200     395.948 ±    3.127  us/op
Benchmarks.hash   64    1   10  avgt  200    3622.080 ±   18.525  us/op
Benchmarks.hash   64    1  100  avgt  200   35925.682 ±  168.344  us/op
Benchmarks.hash   64    2    1  avgt  200     462.278 ±    6.382  us/op
Benchmarks.hash   64    2   10  avgt  200    3956.019 ±   92.832  us/op
Benchmarks.hash   64    2  100  avgt  200   39380.029 ± 1098.735  us/op
Benchmarks.hash   64    4    1  avgt  200     870.926 ±    9.764  us/op
Benchmarks.hash   64    4   10  avgt  200    7662.153 ±   69.601  us/op
Benchmarks.hash   64    4  100  avgt  200   75204.242 ± 1552.080  us/op
Benchmarks.hash  128    1    1  avgt  200     791.992 ±    9.299  us/op
Benchmarks.hash  128    1   10  avgt  200    7223.105 ±   36.170  us/op
Benchmarks.hash  128    1  100  avgt  200   71815.203 ±  515.569  us/op
Benchmarks.hash  128    2    1  avgt  200     876.679 ±   12.514  us/op
Benchmarks.hash  128    2   10  avgt  200    7925.335 ±  226.375  us/op
Benchmarks.hash  128    2  100  avgt  200   78892.745 ± 2634.904  us/op
Benchmarks.hash  128    4    1  avgt  200    1690.955 ±   19.249  us/op
Benchmarks.hash  128    4   10  avgt  200   15446.866 ±  155.790  us/op
Benchmarks.hash  128    4  100  avgt  200  150359.627 ± 2680.702  us/op
```