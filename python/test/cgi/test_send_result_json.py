#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_send_result_json():
  status = 'OK'
  body = {
    'aaa': 'bbb',
    'ccc': 'あいう'
  }
  util.send_result_json(status, body)

def main():
  test_send_result_json()

main()
