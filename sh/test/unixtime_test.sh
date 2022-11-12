#!/bin/bash -eu

. ../util.sh

d="2022-11-12 12:34:56.789"
t_s=1668224096
t_ms=1668224096.789
fmt="%Y-%m-%d %H:%M:%S.%3N"

s=$(unixtime)
echo "${s}"

s=$(unixtime "${d}")
echo "${s}"

echo "----------"
s=$(unixmillis)
echo "${s}"

s=$(unixmillis "${d}")
echo "${s}"

echo "----------"
s=$(datestring)
echo "${s}"

s=$(datestring ${t_ms} "${fmt}")
echo "${s}"

s=$(datestring ${t_ms} "%Y-%m-%d %H:%M:%S")
echo "${s}"

s=$(datestring ${t_s} "${fmt}")
echo "${s}"

s=$(datestring ${t_s} "%Y-%m-%d %H:%M:%S")
echo "${s}"
