import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
  util.base64_file_to_file_decoder('C:/test/b64.txt', 'C:/test/b64.jpg')
  print('OK')

main()
