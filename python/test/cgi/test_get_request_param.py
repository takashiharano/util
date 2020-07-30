#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
  ret = ''
  ret += 'data1=' + str(util.get_request_param('data1')) + '\n'
  ret += 'data2=' + str(util.get_request_param('data2')) + '\n'
  return ret

def main():
  ret = test()
  print('Content-Type: text/plain')
  print()
  print(ret)

main()
