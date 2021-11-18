import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
  ret = util.path_exists('C:/tmp/a')
  print('OK: ' + str(ret))

main()
