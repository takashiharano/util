import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_copy_dir():
  util.copy_dir('d1', 'd3', True)
  return 'copy OK:'

def main():
  s = test_copy_dir()
  print(s)

main()
