import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    util.sleep(3)

def main():
    test()
    print('OK')

main()
