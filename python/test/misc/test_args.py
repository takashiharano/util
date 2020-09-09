#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
  ret = ''
  ret += 'get_args() = ' + str(util.get_args()) + '\n'
  ret += '\n'
  ret += 'get_args_len() = ' + str(util.get_args_len()) + '\n'
  ret += '\n'
  ret += 'get_arg(0) = ' + util.get_arg(0) + '\n'
  ret += 'get_arg(1) = ' + util.get_arg(1) + '\n'
  ret += 'get_arg(2) = ' + util.get_arg(2) + '\n'
  ret += 'get_arg(3, \'-\') = ' + util.get_arg(3, '-') + '\n'
  return ret

def main():
  try:
    ret = test()
  except Exception as e:
    ret = str(e)
  print(ret)

main()
