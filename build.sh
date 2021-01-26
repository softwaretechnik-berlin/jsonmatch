#!/usr/bin/env bash

set -e
set -u
set -o pipefail

mvn clean test
mvn docufier:docufy

cat README.md | sed 's%src/test/java/jsonmatch/MatchingTest.java%https://github.com/softwaretechnik-berlin/jsonmatch/blob/main/src/test/java/jsonmatch/MatchingTest.java%'>pages/index.md


