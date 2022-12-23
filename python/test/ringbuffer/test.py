import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def add_test(size, addsize, getsize):
    TEST_DATA = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N']
    rbuf = util.RingBuffer(size)

    for i in range(addsize):
        data = TEST_DATA[i]
        print('add(' + str(i) + '): ' + data)
        rbuf.add(data)

    print('len: ' + str(len(rbuf)))
    print('size: ' + str(rbuf.size))
    print('count: ' + str(rbuf.count))
    print('-----------')
    print('get()')
    for i in range(getsize):
        print(str(i) + ': ' + str(rbuf.get(i)))

    print('-----------')
    print('get_all()')
    arr = rbuf.get_all()
    print(str(arr))
    for i in range(len(arr)):
        print(str(i) + ': ' + str(arr[i]))

def main():
    print('----------');
    add_test(3, 0, 3)
    print('')

    print('----------');
    add_test(3, 1, 3)
    print('')

    print('----------');
    add_test(3, 2, 5)
    print('')

    print('----------');
    add_test(3, 3, 5)
    print('')

    print('----------');
    add_test(3, 4, 5)
    print('')

    print('----------');
    add_test(3, 5, 5)
    print('')

    print('----------');
    add_test(3, 6, 10)
    print('')

main()

