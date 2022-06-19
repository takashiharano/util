import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
    ret = util.file_lock('C:/tmp/lock')
    print('OK: ' + str(ret))

main()
