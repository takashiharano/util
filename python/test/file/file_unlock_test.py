import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
    util.file_unlock('C:/tmp/lock')
    print('OK')

main()
