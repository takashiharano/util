#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_next_datetime():
  ret = ''

  arr = ['0300', '0900', '1200', '1800']

  ret += '\n'
  ret += "['0300', '0900', '1200', '1800']\n"
  ret += '\n'
  ret += 'now\n'
  ret += str(util.next_datetime(arr)) + '\n'

  ret += '\n'
  dtstr = '2019-02-28 00:00:00.0000'
  ret += dtstr + '\n'
  ret += '(exp=03:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, moment=dt)) + '\n'

  ret += '\n'
  dtstr = '2019-02-28 03:00:00.0000'
  ret += dtstr + '\n'
  ret += '(exp=02-28 03:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, moment=dt)) + '\n'

  ret += '\n'
  dtstr = '2019-02-28 03:00:00.0001'
  ret += dtstr + '\n'
  ret += '(exp=02-28 09:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, moment=dt)) + '\n'

  ret += '\n'
  dtstr = '2019-02-28 09:00:00.0000'
  ret += dtstr + '\n'
  ret += '(exp=02-28 09:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, moment=dt)) + '\n'

  ret += '\n'
  dtstr = '2019-02-28 10:00:00.0000'
  ret += dtstr + '\n'
  ret += '(exp=02-28 12:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, moment=dt)) + '\n'

  ret += '\n'
  dtstr = '2019-02-28 15:00:00.0000'
  ret += dtstr + '\n'
  ret += '(exp=02-28 18:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, moment=dt)) + '\n'

  ret += '\n'
  dtstr = '2019-02-28 19:00:00.0000'
  ret += dtstr + '\n'
  ret += '(exp=03-01 03:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, moment=dt)) + '\n'

  # 2019-02-28 00:00:00.0000
  ret += '\n'
  dt = 1551279600.000
  ret += str(dt) + '\n'
  ret += '(exp=02-28 03:00)\n'
  ret += str(util.next_datetime(arr, moment=dt)) + '\n'

  # 2019-02-28 00:00:00.0000
  ret += '\n'
  dt = 1551279600
  ret += str(dt) + '\n'
  ret += '(exp=02-28 03:00)\n'
  ret += str(util.next_datetime(arr, moment=dt)) + '\n'

  # 2019-02-28 10:00:00.0000
  ret += '\n'
  dt = 1551315600.000
  ret += str(dt) + '\n'
  ret += '(exp=02-28 12:00)\n'
  ret += str(util.next_datetime(arr, moment=dt)) + '\n'

  # 2019-02-28 10:00:00.0000
  ret += '\n'
  dt = 1551315600
  ret += str(dt) + '\n'
  ret += '(exp=02-28 12:00)\n'
  ret += str(util.next_datetime(arr, moment=dt)) + '\n'

  ret += '----'

  ret += '\n'
  dtstr = '2019-02-28 00:00:00.0000'
  ret += dtstr + '\n'
  ret += '(-1 exp=02-27 18:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, -1, moment=dt)) + '\n'

  ret += '\n'
  dtstr = '2019-02-28 04:00:00.0000'
  ret += dtstr + '\n'
  ret += '(-1 exp=02-28 03:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, -1,  moment=dt)) + '\n'


  ret += '----'

  ret += '\n'
  dtstr = '2019-02-28 00:00:00.0000'
  ret += dtstr + '\n'
  ret += '(-2 exp=02-27 12:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, -2, moment=dt)) + '\n'

  ret += '\n'
  dtstr = '2019-02-28 04:00:00.0000'
  ret += dtstr + '\n'
  ret += '(-2 exp=02-27 18:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, -2,  moment=dt)) + '\n'


  ret += '----'

  ret += '\n'
  dtstr = '2019-02-28 00:00:00.0000'
  ret += dtstr + '\n'
  ret += '(2 exp=02-28 09:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, 2, moment=dt)) + '\n'

  ret += '\n'
  dtstr = '2019-02-28 04:00:00.0000'
  ret += dtstr + '\n'
  ret += '(2 exp=02-28 12:00)\n'
  dt = util.get_datetime(dtstr)
  ret += str(util.next_datetime(arr, 2,  moment=dt)) + '\n'

  return ret

def main():
  ret = test_next_datetime()

  print('Content-Type: text/plain')
  print()
  print(ret)

main()
