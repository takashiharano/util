#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test1():
  zip_path = 'C:/test/zip1.zip'
  target_file_path = 'C:/test/a.txt'
  util.zip(zip_path, target_file_path)
  return 'OK'

def test_dir():
  zip_path = 'C:/test/zip_dir.zip'
  target_file_path = 'C:/test/dir1'
  util.zip(zip_path, target_file_path)
  return 'OK'

def main():
  ret = ''
  ret += test1() + '\n'
  ret += test_dir() + '\n'
  ret += '\n'

  print('Content-Type: text/plain')
  print()
  print(ret)

main()
