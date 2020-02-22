#!/usr/bin/env bash

set -e

cmake .
make
./halite -s 42  --replay-directory replays/ -vvv --width 32 --height 32 "./MyBot"
./halite -s 1673031865  --replay-directory replays/ -vvv --width 32 --height 32 "./MyBot"
./halite -s 1773807367  --replay-directory replays/ -vvv --width 40 --height 40 "./MyBot"
./halite -s 1942373999  --replay-directory replays/ -vvv --width 48 --height 48 "./MyBot"
./halite -s 142342898  --replay-directory replays/ -vvv --width 56 --height 56 "./MyBot"

