#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
  obj = util.load_dict('./dict_to_load.json')
  ret = str(obj)
  return ret

def main():
  try:
    ret = test()
  except Exception as e:
    ret = str(e)

  print('Content-Type: text/plain')
  print()
  print(ret)

main()
