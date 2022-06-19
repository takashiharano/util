import os
import sys
import datetime

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
    print('--- get_timestamp(today)')
    for i in range(100):
        now = util.get_timestamp(datetime.datetime.today())
        print(str(now))

    print('--- get_timestamp()')
    for i in range(100):
        now = util.get_timestamp()
        print(str(now))

    print('--- now()')
    for i in range(100):
        now = util.now()
        print(str(now))

main()
