#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_time_add():
  s = ''
  s += '\n--------------------\n'
  s += _test_calc_time('add', '00:05:30.700', '00:01:20.123000', False, '00:06:50.823')

  s += '\n--------------------\n'
  s += _test_calc_time('add', '10:00', '16:00', True, '02:00:00.000000 (+1 Day)')
  s += _test_calc_time('add', '10:00', '16:00', False, '26:00:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('add', '10:00', '14:00', True, '00:00:00.000000 (+1 Day)')
  s += _test_calc_time('add', '10:00', '14:00', False, '24:00:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('add', '10:00', '14:00:00.000001', True, '00:00:00.000000 (+1 Day)')
  s += _test_calc_time('add', '10:00', '14:00:00.000001', False, '24:00:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('add', '10:00', '37:59', True, '23:59:00.000000 (+1 Day)')
  s += _test_calc_time('add', '10:00', '37:59', False, '47:59:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('add', '10:00', '38:00', True, '00:00:00.000000 (+2 Days)')
  s += _test_calc_time('add', '10:00', '38:00', False, '48:00:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('add', '10:00', '38:01', True, '00:01:00.000000 (+2 Days)')
  s += _test_calc_time('add', '10:00', '38:01', False, '48:01:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('add', '10:00', '00:00:01', True, '10:00:01.000000')
  s += _test_calc_time('add', '10:00', '00:00:01', False, '10:00:01.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('add', '10:00', '00:00:00.1', True, '10:00:00.100000')
  s += _test_calc_time('add', '10:00', '00:00:00.1', False, '10:00:00.100000')

  s += '\n--------------------\n'
  s += _test_calc_time('add', '10:00', '00:00:00.000001', True, '10:00:00.000001')
  s += _test_calc_time('add', '10:00', '00:00:00.000001', False, '10:00:00.000001')

  s += '\n--------------------\n'
  s += _test_calc_time('add', '000530.700', '000120.123000', False, '00:06:50.823000')
  s += _test_calc_time('add', '1000', '0900', False, '19:00:00.000000')

  return s

def test_time_sub():
  s = ''
  s += _test_calc_time('sub', '00:05:30.700', '00:05:20.400000', False, '00:00:10.300')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '10:00', '16:00', True, '18:00:00.000000 (-1 Day)')
  s += _test_calc_time('sub', '10:00', '16:00', False, '-06:00:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '00:00', '23:59', True, '00:01:00.000000 (-1 Day)')
  s += _test_calc_time('sub', '00:00', '23:59', False, '-23:59:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '00:00', '24:00', True, '00:00:00.000000 (-1 Day)')
  s += _test_calc_time('sub', '00:00', '24:00', False, '-24:00:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '00:00', '24:01', True, '23:59:00.000000 (-2 Days)')
  s += _test_calc_time('sub', '00:00', '24:01', False, '-24:01:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '00:00', '24:02', True, '23:58:00.000000 (-2 Days)')
  s += _test_calc_time('sub', '00:00', '24:02', False, '-24:02:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '00:01', '24:00', True, '00:01:00.000000 (-1 Day)')
  s += _test_calc_time('sub', '00:01', '24:00', False, '-23:59:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '00:01', '24:01', True, '00:00:00.000000 (-1 Day)')
  s += _test_calc_time('sub', '00:01', '24:01', False, '-24:00:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '00:01', '24:02', True, '23:59:00.000000 (-2 Days)')
  s += _test_calc_time('sub', '00:01', '24:02', False, '-24:01:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '10:00', '10:00', True, '00:00:00.000000')
  s += _test_calc_time('sub', '10:00', '10:00', False, '00:00:00.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '10:00', '10:00:01', True, '23:59:59.000000 (-1 Day)')
  s += _test_calc_time('sub', '10:00', '10:00:01', False, '-00:00:01.000000')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '10:00', '10:00:00.10000', True, '23:59:59.000000 (-1 Day)')
  s += _test_calc_time('sub', '10:00', '10:00:00.10000', False, '-00:00:00.100000')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '10:00', '10:00:00.000001', True, '23:59:59.999999 (-1 Day)')
  s += _test_calc_time('sub', '10:00', '10:00:00.000001', False, '-00:00:00.000001')

  s += '\n--------------------\n'
  s += _test_calc_time('sub', '000530.700000', '000520.400000', False, '00:00:10.300000')
  s += _test_calc_time('sub', '1500', '1200', False, '03:00:00.000000')

  return s

def _test_calc_time(type, t1, t2, by_the_day, expected):
  op = '+' if type == 'add' else '-'

  s = ''
  s += '----------\n'
  s += t1 + ' ' + op + ' ' + t2 + '\n'
  s += 't1 = ' + t1 + '\n'
  s += 't2 = ' + t2 + '\n'
  s += 'by_the_day = ' + str(by_the_day) + '\n'

  if type == 'add':
    ret = util.time_add(t1, t2)
  else:
    ret = util.time_sub(t1, t2)

  s += str(ret) + '\n'
  s += 'exp        = ' + expected + '\n'
  s += 'got        = ' + ret.to_str(by_the_day=by_the_day) + '\n'
  s += 'got(HM   ) = ' + ret.to_str(fmt='HM', by_the_day=by_the_day) + '\n'
  s += 'got(HMS  ) = ' + ret.to_str(fmt='HMS', by_the_day=by_the_day) + '\n'
  s += 'got(HMSs ) = ' + ret.to_str(fmt='HMSs', by_the_day=by_the_day) + '\n'
  s += 'got(HMSsD) = ' + ret.to_str(fmt='HMSsD', by_the_day=by_the_day) + '\n'
  s += 'got        = ' + str(ret.secs) + '\n'
  return s

def main():
  ret = ''
  ret += test_time_add()
  ret += '\n============================================================\n'
  ret += test_time_sub()

  print('Content-Type: text/plain')
  print()
  print(ret)

main()
