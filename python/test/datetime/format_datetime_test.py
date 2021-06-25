import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test(data_in, fmt, data_exp):
  data_out = util.format_datetime(data_in, fmt)
  if data_out == data_exp:
    ret = 'OK'
  else:
    ret = 'NG'
  print('[' + ret + '] IN=' + data_in + ' ' + 'EXP=' + data_exp + ' OUT=' + data_out)

def main():
  data_in = '20210102123456789123'

  fmt = '%Y-%m-%d %H:%M:%S.%f'
  data_exp = '2021-01-02 12:34:56.789123'
  test(data_in, fmt, data_exp)

  fmt = '%Y-%m-%dT%H:%M:%S'
  data_exp = '2021-01-02T12:34:56'
  test(data_in, fmt, data_exp)

  fmt = '%Y/%m/%d %H:%M'
  data_exp = '2021/01/02 12:34'
  test(data_in, fmt, data_exp)

main()
