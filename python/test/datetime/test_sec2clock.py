#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
  print(util.sec2clock(0))
  print(util.sec2clock(0.000001))
  print(util.sec2clock(0.1))
  print(util.sec2clock(1))
  print(util.sec2clock(60))
  print(util.sec2clock(3600))
  print(util.sec2clock(86399))
  print(util.sec2clock(86400))
  print(util.sec2clock(360000))
  print(util.sec2clock(361845.789))

  print('---')
  print(util.sec2clock(-0))
  print(util.sec2clock(-0.000001))
  print(util.sec2clock(-0.1))
  print(util.sec2clock(-1))
  print(util.sec2clock(-60))
  print(util.sec2clock(-3600))
  print(util.sec2clock(-360000))

main()
