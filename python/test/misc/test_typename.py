#!python

# "abc"     = str
# 123       = int
# 123.0     = float
# True      = bool
# {}        = dict
# []        = list
# ()        = tuple
# None      = NoneType
# b"\x00"   = bytes
# r"\d"     = str
# u"\u3042" = str
# test()    = function
# Exception = type
# util      = module
# datetime.datetime.today() = datetime

import os
import sys
import datetime

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_typename():
  s = '\n'
  s += '"abc"     = ' + util.typename('abc')
  s += '\n'
  s += '123       = ' + util.typename(123)
  s += '\n'
  s += '123.0     = ' + util.typename(123.0)
  s += '\n'
  s += 'True      = ' + util.typename(True)
  s += '\n'
  s += '{}        = ' + util.typename({})
  s += '\n'
  s += '[]        = ' + util.typename([])
  s += '\n'
  s += '()        = ' + util.typename(())
  s += '\n'
  s += 'None      = ' + util.typename(None)
  s += '\n'
  s += 'b"\\x00"   = ' + util.typename(b'\x00')
  s += '\n'
  s += 'r"\\d"     = ' + util.typename(r'\d')
  s += '\n'
  s += 'u"\\u3042" = ' + util.typename(u"\u3042")
  s += '\n'
  s += 'test()    = ' + util.typename(test)
  s += '\n'
  s += 'Exception = ' + util.typename(Exception)
  s += '\n'
  s += 'util      = ' + util.typename(util)
  s += '\n'
  s += 'datetime.datetime.today() = ' + util.typename(datetime.datetime.today())
  s += '\n'
  return s

def test():
  ret = 'test_typename() = ' + test_typename()
  return ret

def main():
  try:
    ret = test()
  except Exception as e:
    ret = str(e)
  util.send_response('text', ret)

main()
