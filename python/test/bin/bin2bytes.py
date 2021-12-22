import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
  b = util.bin2bytes('01000001 01000010 01000011')
  util.write_file('C:/tmp/a.txt', b)

def main():
  test()
  print('OK')

main()
