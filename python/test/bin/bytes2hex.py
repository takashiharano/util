import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    b = util.read_file('C:/test/a.txt', 'b')
    #b = b'\x00\x01\x02\x03'
    s = util.bytes2hex(b)
    print('hex="' + s + '"')

def main():
    test()
    print('OK')

main()
