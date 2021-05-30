import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_listed():
  zip_path = 'C:/tmp/zip_listed.zip'
  target_list = ['C:/test/a.txt', 'C:/test/b.txt']
  util.zip(zip_path, target_list)
  return 'OK'

def main():
  test_listed()
  print('OK')

main()
