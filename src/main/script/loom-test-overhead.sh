#!/bin/sh

#for CONCURRENCY in 1 2 4 8 16 32
for CONCURRENCY in 1 2
do
#  for EXP in 8 10 12 14 16 18 20 22 24
  for EXP in 8
  do
    # for PORT in 8080 8081
    for PORT in 8082
    do
      # for RUN in 1 2 3 4 5 6 7 8 9 10 11
      for RUN in 1 2
      do
        RESULT=`/opt/wrk/wrk -t$CONCURRENCY -c$CONCURRENCY -d10s http://localhost:$PORT/loom/overhead?exp=$EXP | grep Requests | awk '{print $2}'`
        if [ $RUN -ne 1 ]; then
          echo $CONCURRENCY $EXP $PORT $RESULT
        fi
      done
    done
  done
done
