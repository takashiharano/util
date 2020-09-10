#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
  print(util.sec2str(0))
  print(util.sec2str(0.000001))
  print(util.sec2str(0.00001))
  print(util.sec2str(0.1))
  print(util.sec2str(0.123456))
  print(util.sec2str(0.1234567))
  print(util.sec2str(1))
  print(util.sec2str(1.1))
  print(util.sec2str(60))
  print(util.sec2str(61))
  print(util.sec2str(61.123))
  print(util.sec2str(3600))
  print(util.sec2str(3601))
  print(util.sec2str(3660))
  print(util.sec2str(3661))
  print(util.sec2str(86400))
  print(util.sec2str(86400, h=True))
  print(util.sec2str(171959.123456))

  print('--- f=True ---')
  print(util.sec2str(0, f=True))
  print(util.sec2str(0.000001, f=True))
  print(util.sec2str(0.00001, f=True))
  print(util.sec2str(0.1, f=True))
  print(util.sec2str(0.123456, f=True))
  print(util.sec2str(0.1234567, f=True))
  print(util.sec2str(1, f=True))
  print(util.sec2str(1.1, f=True))



main()
