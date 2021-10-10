import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
  util.decode_base64_file_to_file('C:/test/b64image.txt', 'C:/tmp/image.jpg')
  print('OK')

main()
