import os
import sys
import re

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_extract_str():
    s = '\n'
    s += '\'abc123xyz\', \'ab(.\d+).*\' = ' + util.extract_str('abc123xyz', 'ab(.\d+).*')
    s += '\n'
    s += '\'abc123xyz\', \'ab(\d+).*\' = ' + util.extract_str('abc123xyz', 'ab(\d+).*')

    s += '\n'
    s += '\n'
    s += '\'abc123XyZ\', \'.*(xyz).*\' = ' + util.extract_str('abc123XyZ', '.*(xyz).*')
    s += '\n'
    s += '\'abc123XyZ\', \'.*(xyz).*\, flags=re.IGNORECASE\' = ' + util.extract_str('abc123XyZ', '.*(xyz).*', flags=re.IGNORECASE)

    s += '\n'
    s += '\n'
    s += '\'abc123x(y)z\', \'.*(x\\(y\\)z).**\' = ' + util.extract_str('abc123x(y)z', '.*(x\(y\)z).*')

    s += '\n'
    s += '\'aaa of bbb of ccc\', \'.+? of (.*)\' = ' + util.extract_str('aaa of bbb of ccc', '.+? of (.*)')
    s += '\n'
    s += '\'aaa bbb of ccc of ddd eee\', \'.+? of (.*)\' = ' + util.extract_str('aaa bbb of ccc of ddd eee', '.+? of (.*)')
    s += '\n'
    s += '\'aaa bbb of ccc of ddd eee\', \'of\' = ' + util.extract_str('aaa bbb of ccc of ddd eee', 'of')

    return s

def main():
    ret = ''
    ret += '\n'
    ret += 'test_extract_str() = ' + test_extract_str() + '\n'
    print(ret)

main()
