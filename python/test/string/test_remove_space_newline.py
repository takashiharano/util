import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
  s = ' abc\n123 456 '
  r = util.remove_space_newline(s)
  print('"' + r + '"')

main()
