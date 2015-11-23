funes
=====

A prototype for a cache server specialized in time series.

Funes is supposing that you have a backend where you can run queries against it and the result is time series data.
Once a bucket of time is calculated by the backend, it is not supposed to change.  

Use sbt run -Dconfig.file=conf/yourconf.conf


## Running tests

Single run:

    sbt test

Continuous:

    ~ test  // you should run it inside sbt console.