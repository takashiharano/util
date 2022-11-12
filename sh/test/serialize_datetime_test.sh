#!/bin/bash -eu

../serialize_datetime.sh 20220102123456789
../serialize_datetime.sh 20220102T123456789
../serialize_datetime.sh 2022-01-02T12:34:56.789
../serialize_datetime.sh 2022/01/02T12:34:56.789
../serialize_datetime.sh 2022-01-02T12:34:56
../serialize_datetime.sh 2022-01-02T12:34
../serialize_datetime.sh 2022-01-02
../serialize_datetime.sh 2022/01/02
../serialize_datetime.sh 2022-01
../serialize_datetime.sh 2022
