import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
  b = util.hex2bytes('41 42 43')
  util.write_file('C:/tmp/a.txt', b)

def main():
  test()
  print('OK')

main()
