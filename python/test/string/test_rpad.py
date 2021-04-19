#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util


def test1():
  ret = ''
  ret += util.rpad('', '0', 5) + '\n'
  ret += util.rpad('ABC', '0', 5) + '\n'
  ret += util.rpad('ABCDE', '0', 5) + '\n'
  ret += util.rpad('ABCDEF', '0', 5) + '\n'
  ret += util.rpad(1, '0', 5) + '\n'
  return ret

def main():
  ret = ''
  ret += test1()
  ret += '\n'

  print('Content-Type: text/plain')
  print()
  print(ret)

main()
