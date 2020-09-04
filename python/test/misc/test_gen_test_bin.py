#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
  data = util.gen_test_bin(1024)
  util.write_file('C:/test/test1.dat', data, 'b')

  data = util.gen_test_bin(1024, -1, -1)
  util.write_file('C:/test/test2.dat', data, 'b')

  data = util.gen_test_bin(1024, 0x21, 0x23)
  util.write_file('C:/test/test3.dat', data, 'b')

  return 'OK'

def main():
  try:
    ret = test()
  except Exception as e:
    ret = str(e)

  print(ret)

main()
