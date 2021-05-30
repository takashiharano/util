import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test1():
  zip_path = 'C:/tmp/zip1.zip'
  target_file_path = 'C:/test/a.txt'
  util.zip(zip_path, target_file_path)
  return 'OK'

def main():
  test1()
  print('OK')

main()
