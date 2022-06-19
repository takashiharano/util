import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_hash():
    ret = ''
    ret += '\n'
    ret += 'MD5\n'
    ret += util.hash('MD5', 'abc') + '\n'

    ret += '\n'
    ret += 'SHA-128\n'
    ret += util.hash('SHA-1', 'abc') + '\n'

    ret += '\n'
    ret += 'SHA-224\n'
    ret += util.hash('SHA-224', 'abc') + '\n'

    ret += '\n'
    ret += 'SHA-256\n'
    ret += util.hash('SHA-256', 'abc') + '\n'

    ret += '\n'
    ret += 'SHA-384\n'
    ret += util.hash('SHA-384', 'abc') + '\n'

    ret += '\n'
    ret += 'SHA-512\n'
    ret += util.hash('SHA-512', 'abc') + '\n'

    # v3.6+
    ret += '\n'
    ret += 'SHA3-224\n'
    ret += util.hash('SHA3-224', 'abc') + '\n'

    ret += '\n'
    ret += 'SHA3-256\n'
    ret += util.hash('SHA3-256', 'abc') + '\n'

    ret += '\n'
    ret += 'SHA3-384\n'
    ret += util.hash('SHA3-384', 'abc') + '\n'

    ret += '\n'
    ret += 'SHA3-512\n'
    ret += util.hash('SHA3-512', 'abc') + '\n'

    return ret

def test():
    ret = ''

    ret += 'test_hash() = ' + test_hash() + '\n'
    ret += '\n'

    return ret

def main():
    try:
        ret = test()
    except Exception as e:
        ret = str(e)

    print(ret)

main()
