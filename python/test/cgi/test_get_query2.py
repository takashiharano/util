#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    ret = ''

    ret += 'query=' + str(util.get_query()) + '\n'
    ret += '\n'

    ret += 'q=None\n'
    ret += 'query=' + str(util.get_query(q=None)) + '\n'
    ret += '\n'

    ret += 'a\n'
    ret += 'query=' + str(util.get_query('a')) + '\n'
    ret += '\n'

    ret += '?a=1 , a\n'
    ret += 'query=' + str(util.get_query('a', 'a=1')) + '\n'
    ret += '\n'

    ret += '?a=1&b=2 , b\n'
    ret += 'query=' + str(util.get_query('b', 'a=1&b=2')) + '\n'
    ret += '\n'

    ret += '?a=1&b=2&a=3 , a\n'
    ret += 'query=' + str(util.get_query('a', 'a=1&b=2&a=3')) + '\n'
    ret += '\n'

    ret += '?a=1&a=2+%26&b=3 , a\n'
    ret += 'query=' + str(util.get_query('a', 'a=1&a=2+%26&b=3')) + '\n'
    ret += '\n'

    ret += '?%E3%81%82=%E3%81%84 , %E3%81%82\n'
    ret += 'query=' + str(util.get_query('あ', '%E3%81%82=%E3%81%84')) + '\n'
    ret += '\n'

    return ret

def main():
    ret = test()
    print('Content-Type: text/plain')
    print()
    print(ret)

main()
