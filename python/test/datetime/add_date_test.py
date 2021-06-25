import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test(data_in, offset, fmt, data_exp):
  data_out = util.add_date(data_in, offset, fmt)
  if data_out == data_exp:
    ret = 'OK'
  else:
    ret = 'NG'
  print('[' + ret + '] IN=' + data_in + ' ' + 'EXP=' + data_exp + ' OUT=' + data_out)

def main():
  fmt = '%Y-%m-%d'

  data_in = '2021-01-01'
  offset = 1
  data_exp = '2021-01-02'
  test(data_in, offset, fmt, data_exp)

  data_in = '2021-01-01'
  offset = 2
  data_exp = '2021-01-03'
  test(data_in, offset, fmt, data_exp)

  data_in = '2021-01-01'
  offset = 365
  data_exp = '2022-01-01'
  test(data_in, offset, fmt, data_exp)

  data_in = '2021-12-31'
  offset = 1
  data_exp = '2022-01-01'
  test(data_in, offset, fmt, data_exp)

  data_in = '2021-01-01'
  offset = -1
  data_exp = '2020-12-31'
  test(data_in, offset, fmt, data_exp)

  data_in = '2021-01-01'
  offset = -2
  data_exp = '2020-12-30'
  test(data_in, offset, fmt, data_exp)

  data_in = '9999-12-31'
  offset = -1
  data_exp = '9999-12-30'
  test(data_in, offset, fmt, data_exp)

  #----------------------------------------
  data_in = '1970-01-01'
  offset = 1
  data_exp = '1970-01-02'
  test(data_in, offset, fmt, data_exp)

  data_in = '1970-01-01'
  offset = -1
  data_exp = '1969-12-31'
  test(data_in, offset, fmt, data_exp)

  data_in = '1900-01-01'
  offset = 1
  data_exp = '1900-01-02'
  test(data_in, offset, fmt, data_exp)

  data_in = '1900-01-01'
  offset = -1
  data_exp = '1899-12-31'
  test(data_in, offset, fmt, data_exp)

  #----------------------------------------
  ## OverflowError: date value out of range
  #data_in = '9999-12-31'
  #offset = 1
  #data_exp = '9999-12-30'
  #test(data_in, offset, fmt, data_exp)

main()
