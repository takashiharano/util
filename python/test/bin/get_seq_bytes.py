#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
  out_dir = 'C:/tmp/'
  path = out_dir + 'test_bytes1.dat'
  data = util.get_seq_bytes(1024)
  util.write_file(path, data)
  print(path + ': OK')

  path = out_dir + 'test_bytes2.dat'
  data = util.get_seq_bytes(1024, -1, -1)
  util.write_file(path, data)
  print(path + ': OK')

  path = out_dir + 'test_bytes3.dat'
  data = util.get_seq_bytes(1024, 0x21, 0x23)
  util.write_file(path, data)
  print(path + ': OK')

  return 'OK'

def main():
  try:
    ret = test()
  except Exception as e:
    ret = str(e)

  print(ret)

main()
