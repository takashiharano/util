import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def main():
    util.start_timetest()
    util.print_elapsed_time()
    util.printlog('abc');
    util.sleep(0.1)
    util.printlog();
    util.printlog('xyz');
    util.sleep(0.2)
    util.printlog('1234567890');
    util.sleep(3)
    util.printlog();
    util.print_elapsed_time()
    util.sleep(1)
    util.print_elapsed_time('Elapsed: ')
    util.sleep(0.5)
    util.print_elapsed_time(suffix=' has passed')
    util.sleep(0.5)
    util.print_elapsed_time('It took ', ' to process')
    util.printlog('Done');

main()
