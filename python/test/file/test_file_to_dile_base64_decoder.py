import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
  util.file_to_file_base64_decoder('C:/test/b64.txt', 'C:/test/b64.jpg')
  print('OK')

main()
