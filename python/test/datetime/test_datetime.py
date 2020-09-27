#!python

import os
import sys
import datetime

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_get_datetime():
  s = '\n'
  s += 'get_datetime() = ' + str(util.get_datetime()) + '\n'
  s += 'get_datetime(1546400096.987654) = ' + str(util.get_datetime(1546400096.987654)) + '\n'
  s += 'get_datetime(1546400096) = ' + str(util.get_datetime(1546400096)) + '\n'
  s += 'get_datetime(\'20190102123456987654\', \'%Y%m%d%H%M%S%f\') = ' + str(util.get_datetime('20190102123456987654', '%Y%m%d%H%M%S%f')) + '\n'
  s += 'get_datetime(\'2019-02-21 12:34:56.789\', \'%Y-%m-%d %H:%M:%S.%f\') = ' + str(util.get_datetime('2019-02-21 12:34:56.789', '%Y-%m-%d %H:%M:%S.%f')) + '\n'

  s += 'get_datetime(\'1234\', \'%H%M\') = ' + str(util.get_datetime('1234', '%H%M')) + '\n'

  obj = util.get_datetime('2019-01-02 12:34:56.987654')
  s += str(obj) + '\n'

  obj = util.get_datetime('20190102123456987654', '%Y%m%d%H%M%S%f')
  s += str(obj) + '\n'

  obj = util.get_datetime('20190102123456', '%Y%m%d%H%M%S')
  s += str(obj) + '\n'

  obj = util.get_datetime('201901021234', '%Y%m%d%H%M')
  s += str(obj) + '\n'
  return s

def test_get_datetime_str():
  s = '\n'
  s += 'get_datetime_str() = ' + util.get_datetime_str() + '\n'
  s += 'get_datetime_str(fmt=\'%Y-%m-%d_%H%M%S_%f\') = ' + util.get_datetime_str(fmt='%Y-%m-%d_%H%M%S_%f') + '\n'
  s += 'get_datetime_str(datetime.datetime(2018, 1, 2, 12, 34, 56, 987654)) = ' + util.get_datetime_str(datetime.datetime(2018, 1, 2, 12, 34, 56, 987654)) + '\n'
  s += '\n'
  s += 'get_datetime_str(1546400096.987654) = ' + util.get_datetime_str(1546400096.987654) + '\n'
  return s

def test_get_timestamp():
  s = ''

  timestamp = util.get_timestamp()
  s += str(timestamp) + '\n'

  dt = util.get_datetime('20190102123456987654', '%Y%m%d%H%M%S%f')
  timestamp = util.get_timestamp(dt)
  s += str(timestamp) + '\n'

  timestamp = util.get_timestamp('2019-01-02 12:34:56.789')
  s += str(timestamp) + '\n'

  timestamp = util.get_timestamp('197001020900', '%Y%m%d%H%M')
  s += str(timestamp) + '\n'

  return s

def test_get_datetime_of_day():
  s = '\n'
  s += 'get_datetime_of_day() = ' + str(util.get_datetime_of_day()) + '\n'
  s += 'get_datetime_of_day(\'12:34:56.987654\') = ' + str(util.get_datetime_of_day('12:34:56.987654')) + '\n'
  s += 'get_datetime_of_day(\'12:34\', \'%H:%M\') = ' + str(util.get_datetime_of_day('12:34', '%H:%M')) + '\n'

  s += '\n'
  s += 'get_datetime_of_day(offset=1) = ' + str(util.get_datetime_of_day(offset=1)) + '\n'
  s += 'get_datetime_of_day(\'12:34:56.987654\', offset=1) = ' + str(util.get_datetime_of_day('12:34:56.987654', offset=1)) + '\n'
  s += 'get_datetime_of_day(\'12:34\', \'%H:%M\', offset=1) = ' + str(util.get_datetime_of_day('12:34', '%H:%M', offset=1)) + '\n'

  s += '\n'
  s += 'get_datetime_of_day(offset=-1) = ' + str(util.get_datetime_of_day(offset=-1)) + '\n'
  s += 'get_datetime_of_day(\'12:34:56.987654\', offset=-1) = ' + str(util.get_datetime_of_day('12:34:56.987654', offset=-1)) + '\n'
  s += 'get_datetime_of_day(\'12:34\', \'%H:%M\', offset=-1) = ' + str(util.get_datetime_of_day('12:34', '%H:%M', offset=-1)) + '\n'
  return s

def test_get_timestamp_of_day():
  s = '\n'
  s += 'get_timestamp_of_day() = ' + str(util.get_timestamp_of_day()) + '\n'
  s += 'get_timestamp_of_day(\'12:34:56.987654\') = ' + str(util.get_timestamp_of_day('12:34:56.987654')) + '\n'
  s += 'get_timestamp_of_day(\'12:34\', \'%H:%M\') = ' + str(util.get_timestamp_of_day('12:34', '%H:%M')) + '\n'

  s += '\n'
  s += 'get_timestamp_of_day(offset=1) = ' + str(util.get_timestamp_of_day(offset=1)) + '\n'
  s += 'get_timestamp_of_day(\'12:34:56.987654\', offset=1) = ' + str(util.get_timestamp_of_day('12:34:56.987654', offset=1)) + '\n'
  s += 'get_timestamp_of_day(\'12:34\', \'%H:%M\', offset=1) = ' + str(util.get_timestamp_of_day('12:34', '%H:%M', offset=1)) + '\n'

  s += '\n'
  s += 'get_timestamp_of_day(offset=-1) = ' + str(util.get_timestamp_of_day(offset=-1)) + '\n'
  s += 'get_timestamp_of_day(\'12:34:56.987654\', offset=-1) = ' + str(util.get_timestamp_of_day('12:34:56.987654', offset=-1)) + '\n'
  s += 'get_timestamp_of_day(\'12:34\', \'%H:%M\', offset=-1) = ' + str(util.get_timestamp_of_day('12:34', '%H:%M', offset=-1)) + '\n'
  return s

def test_is_leap_year():
  s = ''
  for i in range(30):
    year = 1995 + i
    s += 'is_leap_year(' + str(year) + ') = ' + str(util.is_leap_year(year)) + '\n'
  return s

def test_get_last_day_of_month():
  s = ''
  s += 'get_last_day_of_month(2000, 1) = ' + str(util.get_last_day_of_month(2000, 1)) + '\n'
  s += 'get_last_day_of_month(2000, 2) = ' + str(util.get_last_day_of_month(2000, 2)) + '\n'
  s += 'get_last_day_of_month(2000, 3) = ' + str(util.get_last_day_of_month(2000, 3)) + '\n'
  s += 'get_last_day_of_month(2018, 11) = ' + str(util.get_last_day_of_month(2018, 11)) + '\n'
  s += 'get_last_day_of_month(2018, 12) = ' + str(util.get_last_day_of_month(2018, 12)) + '\n'
  s += 'get_last_day_of_month(2019, 1) = ' + str(util.get_last_day_of_month(2019, 1)) + '\n'
  s += 'get_last_day_of_month(2019, 2) = ' + str(util.get_last_day_of_month(2019, 2)) + '\n'
  s += 'get_last_day_of_month(2019, 3) = ' + str(util.get_last_day_of_month(2019, 3)) + '\n'
  return s

def test():
  s = ''

  s += 'test_get_datetime() = ' + test_get_datetime() + '\n'
  s += '\n'

  s += 'test_get_datetime_str() = ' + test_get_datetime_str() + '\n'
  s += '\n'

  s += 'test_get_timestamp() = \n' + test_get_timestamp() + '\n'
  s += '\n'

  s += 'test_get_datetime_of_day() = ' + test_get_datetime_of_day() + '\n'
  s += '\n'

  s += 'test_get_timestamp_of_day() = \n' + test_get_timestamp_of_day() + '\n'
  s += '\n'

  s += 'test_get_last_day_of_month() = \n' + test_get_last_day_of_month() + '\n'
  s += '\n'

  s += 'test_is_leap_year() = \n' + test_is_leap_year() + '\n'
  s += '\n'

  s += 'datetime:\n'
  dt = util.get_datetime()
  s += 'timestamp() = ' + str(dt.timestamp()) + '\n'
  s += 'year        = ' + str(dt.year) + '\n'
  s += 'month       = ' + str(dt.month) + '\n'
  s += 'day         = ' + str(dt.day) + '\n'
  s += 'hour        = ' + str(dt.hour) + '\n'
  s += 'minute      = ' + str(dt.minute) + '\n'
  s += 'second      = ' + str(dt.second) + '\n'
  s += 'microsecond = ' + str(dt.microsecond) + '\n'
  s += 'tzinfo      = ' + str(dt.tzinfo) + '\n'
  s += '\n'

  return s

def main():
  ret = test()

  print('Content-Type: text/plain')
  print()
  print(ret)

main()
