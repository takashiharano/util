#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '..'))
import util

util.debug()

def test():
  util.log('test')
  s = ''

  

  return s

def loop_test():
  util.log('loop test')
  s = ''
  util.start_timetest()

  for i in range(10000000):
    pass

  util.log('time: ' + util.end_timetest())
  return s

def main():
  try:
    ret = test()
  except Exception as e:
    ret = str(e)

  if ret == '':
    ret = 'OK'

  util.send_response('text', ret)

main()
