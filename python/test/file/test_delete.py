#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_delete():
  util.copy_file('a.txt', 'a.txt.bak')
  util.copy_dir('d1', 'd1_bak')
  util.delete('a.txt')
  util.delete('d1', force=True)
  return 'delete OK'

def main():
  s = test_delete()
  util.send_response('text', s)

main()
