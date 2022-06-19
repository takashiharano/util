#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_read_text_file():
    text = util.read_text_file('a.txt')
    #text = util.read_text_file('a.txt', 'shift-jis')
    ret = ''
    for line in text:
      ret += line

    ret += '\n\nread()\n'
    text = util.read_file('a.txt')
    for line in text:
      ret += line

    ret += '\n\nread(t)\n'
    text = util.read_file('a.txt', 't')
    for line in text:
      ret += line

    ret += '\n\nread(b)\n'
    text = util.read_file('a.txt', 'b')
    ret += text.hex()

    return ret

def test():
    ret = 'test_read_text_file() = ' + test_read_text_file() + '\n'
    return ret

def main():
    try:
        ret = test()
    except Exception as e:
        ret = str(e)

    util.send_response('text', ret)

main()
