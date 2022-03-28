# Requirements
The requirements are announced in this [file](README%5B47%5D.md)

# About technologies
The project was made using scala with FS2 streaming library  and
the test suit is built on top of Scalatests.

# About the project structure.
It does have a package named `com.github.devcdcc` and it includes a set of traits that define the application behavior.

in the `src/test/scala` are included the set of tests made for this project, the most important test made using TDD is
DataReducerSpec, it does encapsulate the logic of basic metrics, each record processed by the stream will be aggregated
and compared with the previous metric of its device.

# Solution
Basically I've decided to Memoization to avoid an exponential complexity algorithm, that's possible only because 
we don't care about the previous records, we just need to take care about the metrics, 
otherwise we should have to use another quadratic structure/algorithm.

# Run

To run, please be sure to use `sbt run` inside the project.
It will read a set of file from the `input` folder and the program just show us the output in the spected format.

### Output Example

```text
Num of processed files: 2
Num of processed measurements: 7
Num of failed measurements: 2

Sensors with highest avg humidity:

sensor-id,min,avg,max
s1,36.0,10,98
s2,82.0,78,88
s3,NaN,NaN,NaN
```