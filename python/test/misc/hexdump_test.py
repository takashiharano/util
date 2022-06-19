#!python

import os
import sys
import datetime

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_hexdump():
    s = ''

    b = b'\x41\x42\x43'
    s += '\n'
    s += util.hexdump(b)
    s += '\n'

    s += file_test1('C:/test/a.txt')
    s += file_test1('C:/test/127.txt')
    s += file_test1('C:/test/128.txt')
    s += file_test1('C:/test/129.txt')

    #----------------------------
    s += file_test2('C:/test/127.txt')
    s += file_test2('C:/test/128.txt')
    s += file_test2('C:/test/129.txt')

    #----------------------------
    s += file_test2('C:/test/32.txt')


    #----------------------------
    s += 'no ascii-------------'
    s += file_test3('C:/test/128.txt')

    s += 'no addr-------------'
    s += file_test4('C:/test/128.txt')

    s += 'no header-------------'
    s += file_test5('C:/test/128.txt')

    #----------------------------
    s += 'file_dump-------------'
    s += file_dump_test1('C:/test/32.txt')
    s += file_dump_test1('C:/test/127.txt')

    return s

def file_test1(path):
    b = util.read_file(path, 'b')
    s = '\n\n'
    s += util.hexdump(b)
    s += '\n'
    return s

def file_test2(path):
    b = util.read_file(path, 'b')
    s = '\n\n'
    s += util.hexdump(b, 32, 2)
    s += '\n'
    return s

def file_test3(path):
    b = util.read_file(path, 'b')
    s = '\n\n'
    s += util.hexdump(b, 32, 2, ascii=False)
    s += '\n'
    return s

def file_test4(path):
    b = util.read_file(path, 'b')
    s = '\n\n'
    s += util.hexdump(b, 32, 2, address=False, ascii=False)
    s += '\n'
    return s

def file_test5(path):
    b = util.read_file(path, 'b')
    s = '\n\n'
    s += util.hexdump(b, 32, 2, header=False, address=False, ascii=False)
    s += '\n'
    return s

def file_dump_test1(path):
    s = '\n\n'
    s += util.file_dump(path)
    s += '\n'
    return s

def test():
    ret = 'test_hexdump() = ' + test_hexdump()
    return ret

def main():
    ret = test()
    util.send_response('text', ret)

main()
