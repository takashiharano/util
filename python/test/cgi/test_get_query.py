#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
  ret = 'query=' + str(util.get_query()) + '\n'
  ret += 'a=' + str(util.get_query('a')) + '\n'
  ret += 'b=' + str(util.get_query('b')) + '\n'
  return ret

def main():
  ret = test()
  print('Content-Type: text/plain')
  print()
  print(ret)

main()
