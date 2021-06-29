import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
  data = util.read_csv('C:/test/csv/test.csv')

  new_data = []
  row_idx = 0
  col_idx = 0
  for row in data:
    row_idx = row_idx + 1
    new_row = []
    col_idx = 0
    for col in row:
      col_idx = col_idx + 1
      s = str(row_idx) + ':' + str(col_idx) + '=' + col
      print(s)
      new_row.append(col + '_NEW')
    new_data.append(new_row)

  util.write_csv('C:/tmp/newcsv.csv', new_data)

main()
