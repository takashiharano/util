import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
    print(util.clock2sec('01:00'))
    print(util.clock2sec('01:00:30'))
    print(util.clock2sec('01:00:30.123456'))
    print(util.clock2sec('0100'))
    print(util.clock2sec('010030'))
    print(util.clock2sec('010030.123456'))

    print(util.clock2sec('-01:00'))
    print(util.clock2sec('-01:00:30'))
    print(util.clock2sec('-01:00:30.123456'))
    print(util.clock2sec('-0100'))
    print(util.clock2sec('-010030'))
    print(util.clock2sec('-010030.123456'))

    print(util.clock2sec('+1:00'))
    print(util.clock2sec('+1:00:30'))
    print(util.clock2sec('+1:00:30.123456'))
    print(util.clock2sec('+100'))
    print(util.clock2sec('+10030'))
    print(util.clock2sec('+10030.123456'))

    print(util.clock2sec('100:30'))
    print(util.clock2sec('100:30:45'))
    print(util.clock2sec('100:30:45.789'))

    print(util.clock2sec('-100:30'))
    print(util.clock2sec('-100:30:45'))
    print(util.clock2sec('-100:30:45.789'))

    print(util.clock2sec('+100:30'))
    print(util.clock2sec('+100:30:45'))
    print(util.clock2sec('+100:30:45.789'))

main()
