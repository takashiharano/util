import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
  test_data = [
    ['20200920', '20200920000000000000'],
    ['2020-09-20', '20200920000000000000'],
    ['2020/09/20', '20200920000000000000'],

    ['20200920 1234', '20200920123400000000'],
    ['2020-09-20 12:34', '20200920123400000000'],
    ['2020/09/20 12:34', '20200920123400000000'],

    ['20200920T1234', '20200920123400000000'],
    ['20200920T123456.789', '20200920123456789000'],
    ['20200920T123456.789123', '20200920123456789123'],
    ['2020-09-20 12:34:56.789', '20200920123456789000'],
    ['2020-09-20 12:34:56.789123', '20200920123456789123'],
    ['2020/09/03 12:34:56.789', '20200903123456789000'],
    ['2020/09/03 12:34:56.789123', '20200903123456789123'],
    ['2020/9/3 12:34:56.789', '20200903123456789000'],
    ['2020/9/3 12:34:56.789123', '20200903123456789123'],

    ['20200903123456789123', '20200903123456789123']
  ]

  for i in range(len(test_data)):
    data = test_data[i]
    data_in = data[0]
    data_expected = data[1]

    out = util.serialize_datetime(data_in)
    if out == data_expected:
      ret = 'OK'
    else:
      ret = 'NG'
    print('[' + ret + '] IN=' + data_in + ' ' + 'EXP=' + data_expected + ' OUT=' + out)

main()