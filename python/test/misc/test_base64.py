#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_base64_decode():
  s = '\n'
  s += util.base64_decode('YWJj')
  s += '\n'
  s += util.base64_decode('YWJj', 'utf-8')
  s += '\n'
  s += util.base64_decode('YWJj', 'shift-jis')
  s += '\n'
  s += util.base64_decode('YWJj44GC44GE44GG', 'utf-8')
  s += '\n'
  s += util.base64_decode('YWJjgqCCooKk', 'shift-jis')
  return s

def test_base64_encode():
  s = '\n'
  s += util.base64_encode('abc')
  s += '\n'
  s += util.base64_encode('abc', 'utf-8')
  s += '\n'
  s += util.base64_encode('abc', 'shift-jis')
  s += '\n'
  s += util.base64_encode('abcあいう', 'utf-8')
  s += '\n'
  s += util.base64_encode('abcあいう', 'shift-jis')
  return s

def test():
  ret = ''
  ret += 'test_base64_decode() = ' + test_base64_decode() + '\n'
  ret += '\n'
  ret += 'test_base64_encode() = ' + test_base64_encode() + '\n'
  return ret

def main():
  try:
    ret = test()
  except Exception as e:
    ret = str(e)
  util.send_response('text', ret)

main()
