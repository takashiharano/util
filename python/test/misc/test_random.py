#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_random():
  s = '\n'
  s += 'random_int(1) = ' + str(util.random_int(1)) + '\n'
  s += '\n'
  s += 'random_int(10) = ' + str(util.random_int(10)) + '\n'
  s += '\n'
  s += 'random_int(5, 20) = ' + str(util.random_int(5, 20)) + '\n'
  s += '\n'
  s += 'random_int(a, b, step) = ' + str(util.random_int(10, 20, 5)) + '\n'

  s += '\n'
  s += 'random_float(5, 10) = ' + str(util.random_float(5, 10)) + '\n'
  s += 'random_float(-1, 1) = ' + str(util.random_float(-1, 1)) + '\n'
  s += 'random_float(0, 1) = ' + str(util.random_float(0, 1)) + '\n'
  s += 'random_float(1.9, 2) = ' + str(util.random_float(1.9, 2)) + '\n'

  s += 'random_float(0, 1.1) = ' + str(util.random_float(0, 1.1)) + '\n'

  s += '\n'
  s += 'random_bool() = ' + str(util.random_bool()) + '\n'
  s += 'random_bool(0) = ' + str(util.random_bool(0)) + '\n'
  s += 'random_bool(1/10) = ' + str(util.random_bool(1/10)) + '\n'
  s += 'random_bool(1/90) = ' + str(util.random_bool(90/100)) + '\n'
  s += 'random_bool(100/100) = ' + str(util.random_bool(100/100)) + '\n'
  s += 'random_bool(2) = ' + str(util.random_bool(2)) + '\n'

  s += '\n'
  s += 'random_ascii() = ' + util.random_ascii() + '\n'

  s += '\n'
  s += 'random_str(4) = "' + util.random_str(4) + '"\n'
  s += '\n'
  s += 'random_str(1, 3) = "' + util.random_str(1, 3) + '"\n'

  s += '\n'
  s += 'random_str(4, tbl=\'ABC123\') = "' + util.random_str(4, tbl='ABC123') + '"\n'
  s += '\n'
  s += 'random_str(1, 3, tbl=\'ABC123\') = "' + util.random_str(1, 3, tbl='ABC123') + '"\n'

  s += '\n'
  s += 'random_str(4, tbl=\'ABC123あいうえお\') = "' + util.random_str(4, tbl='ABC123あいうえお') + '"\n'
  s += '\n'
  s += 'random_str(1, 3, tbl=\'ABC123あいうえお\') = "' + util.random_str(1, 3, tbl='ABC123あいうえお') + '"\n'

  return s

def main():
  s = test_random()
  util.send_response('text', s)

main()
