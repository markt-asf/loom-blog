#!/bin/sh

for CONCURRENCY in 1 2 4 8 16 32
do
  for PORT in 8080 8081
  do
    for RUN in 1 2 3 4 5 6 7 8 9 10 11
    do
      if [ $PORT -eq 8080 ]; then
        SERVLET=async
      else
        SERVLET=blocking
      fi
      RESULT=`/opt/wrk/wrk -t$CONCURRENCY -c$CONCURRENCY -d10s http://localhost:$PORT/loom/$SERVLET | grep Requests | awk '{print $2}'`
      if [ $RUN -ne 1 ]; then
        echo $CONCURRENCY $PORT $RESULT
      fi
    done
  done
done
