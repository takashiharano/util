#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_random():
  s = '\n'
  s += 'randint(1) = ' + str(util.randint(1)) + '\n'
  s += '\n'
  s += 'randint(10) = ' + str(util.randint(10)) + '\n'
  s += '\n'
  s += 'randint(5, 20) = ' + str(util.randint(5, 20)) + '\n'
  s += '\n'
  s += 'randint(a, b, step) = ' + str(util.randint(10, 20, 5)) + '\n'

  s += '\n'
  s += 'randfloat(5, 10) = ' + str(util.randfloat(5, 10)) + '\n'
  s += 'randfloat(-1, 1) = ' + str(util.randfloat(-1, 1)) + '\n'
  s += 'randfloat(0, 1) = ' + str(util.randfloat(0, 1)) + '\n'
  s += 'randfloat(1.9, 2) = ' + str(util.randfloat(1.9, 2)) + '\n'

  s += 'randfloat(0, 1.1) = ' + str(util.randfloat(0, 1.1)) + '\n'

  s += '\n'
  s += 'randbool() = ' + str(util.randbool()) + '\n'
  s += 'randbool(0) = ' + str(util.randbool(0)) + '\n'
  s += 'randbool(1/10) = ' + str(util.randbool(1/10)) + '\n'
  s += 'randbool(1/90) = ' + str(util.randbool(90/100)) + '\n'
  s += 'randbool(100/100) = ' + str(util.randbool(100/100)) + '\n'
  s += 'randbool(2) = ' + str(util.randbool(2)) + '\n'

  s += '\n'
  s += 'randascii() = ' + util.randascii() + '\n'

  s += '\n'
  s += 'randstr(4) = "' + util.randstr(4) + '"\n'
  s += '\n'
  s += 'randstr(1, 3) = "' + util.randstr(1, 3) + '"\n'

  s += '\n'
  s += 'randstr(4, tbl=\'ABC123\') = "' + util.randstr(4, tbl='ABC123') + '"\n'
  s += '\n'
  s += 'randstr(1, 3, tbl=\'ABC123\') = "' + util.randstr(1, 3, tbl='ABC123') + '"\n'

  s += '\n'
  s += 'randstr(4, tbl=\'ABC123あいうえお\') = "' + util.randstr(4, tbl='ABC123あいうえお') + '"\n'
  s += '\n'
  s += 'randstr(1, 3, tbl=\'ABC123あいうえお\') = "' + util.randstr(1, 3, tbl='ABC123あいうえお') + '"\n'

  return s

def main():
  s = test_random()
  util.send_response('text', s)

main()
