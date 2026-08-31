import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_copy_file():
    ret = util.copy('a.txt', 'a1.txt')
    return 'copy OK: ' + str(ret)

def main():
    s = test_copy_file()
    print(s)

main()
