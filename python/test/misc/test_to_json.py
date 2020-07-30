#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_to_json():
  data = {
    'key1': 'val1',
    'key2': 'val2',
    'key3': [1, 2, 3],
    'key4': {
       'key4-1': 1,
       'key4-2': 2,
       'key4-3': 3
     }
  }

  s = util.to_json(data) + '\n'
  s += util.to_json(data, indent=2) + '\n'

  return s

def test():
  ret = 'test_to_json() = ' + test_to_json() + '\n'
  return ret

def main():
  try:
    ret = test()
  except Exception as e:
    ret = str(e)

  #util.send_response('text', ret, encoding='utf-8')
  #util.send_response('text', ret, encoding='shift_jis')
  util.send_response('text', ret)

main()
