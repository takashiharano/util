#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

text = '01:AAAAAAAAAA\n'
text += '02:BBB<start>BBB123BBBB\n'
text += '03:CCCCCCCCCC\n'
text += '04:BBB<start>BBB123BBBB\n'
text += '05:DD45 6DDDDD<end>DDD\n'
text += '06:EEEEEEEEEE\n'

def test1():
  ret = ''
  ret += '------------\n'
  ret += 'start\n'
  ret += util.extract_line(text, 'start')
  ret += '\n'
  ret += '\n'
  ret += '------------\n'
  ret += 'start, count=2\n'
  ret += util.extract_line(text, 'start', count=2)
  ret += '\n'
  return ret

def main():
  ret = ''
  ret += test1()
  ret += '\n'

  print('Content-Type: text/plain')
  print()
  print(ret)

main()
