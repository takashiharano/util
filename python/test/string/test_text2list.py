#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

text1 = 'AAA\n'
text1 += 'BBB\n'
text1 += 'CCC'

text2 = 'AAA\n'
text2 += 'BBB\n'
text2 += 'CCC\n'

text3 = 'AAA\n'
text3 += 'BBB\n'
text3 += 'CCC\n'
text3 += '\n'

text4 = ''

text5 = '\n'

text6 = '\nAAA'

def test1():
  ret = ''
  ret += '------------\n'
  ret += str(util.text2list(text1))
  ret += '\n'
  ret += '------------\n'
  ret += str(util.text2list(text2))
  ret += '\n'
  ret += '------------\n'
  ret += str(util.text2list(text3))
  ret += '\n'
  ret += '------------\n'
  ret += str(util.text2list(text4))
  ret += '\n'
  ret += '------------\n'
  ret += str(util.text2list(text5))
  ret += '\n'
  ret += '------------\n'
  ret += str(util.text2list(text6))
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
