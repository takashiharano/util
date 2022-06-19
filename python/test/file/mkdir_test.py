import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
    util.mkdir('C:/tmp/a')
    print('OK')

main()
