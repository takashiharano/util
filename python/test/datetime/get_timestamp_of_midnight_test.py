import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
  test_data = [
    [1642604400.000, 1642604400.000],
    [1642604400.001, 1642604400.000],
    [1642681157.943, 1642604400.000],
    [1642690799.999, 1642604400.000],
    [1642690800.000, 1642690800.000],
    ['2022-01-21 12:34:56.789000', 1642690800.000]
  ]

  for i in range(len(test_data)):
    data = test_data[i]
    data_in = data[0]
    data_expected = data[1]

    out = util.get_timestamp_of_midnight(data_in)
    if out == data_expected:
      ret = 'OK'
    else:
      ret = 'NG'
    print('[' + ret + '] IN=' + str(data_in) + ' ' + 'EXP=' + str(data_expected) + ' OUT=' + str(out))

main()
