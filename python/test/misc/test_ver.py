#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
  s = ''
  s += 'ver = ' + str(util.get_python_version()) + '\n'
  s += 'sys.platform = ' + sys.platform + '\n'
  s += '__file__ = ' + __file__ + '\n'
  s += '__name__ = ' + __name__ + '\n'
  util.send_response('text', s)

main()
