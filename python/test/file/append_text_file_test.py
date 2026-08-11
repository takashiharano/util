import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    base_path = 'C:/tmp/'
    file_path = base_path + 'a.txt'
    util.append_text_file(file_path, 'aaa')
    util.append_text_file(file_path, 'bbb')

def main():
    test()
    print('OK')

main()
