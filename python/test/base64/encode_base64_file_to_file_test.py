import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
    util.encode_base64_file_to_file('C:/test/image.jpg', 'C:/tmp/b64_newline.txt')
    util.encode_base64_file_to_file('C:/test/image.jpg', 'C:/tmp/b64_1.txt', 0)
    print('OK')

main()
