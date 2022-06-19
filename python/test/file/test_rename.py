import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    util.rename('a.txt', 'a_.txt')
    return ret

def main():
    try:
        ret = test()
    except Exception as e:
        ret = str(e)
    print('OK')

main()
