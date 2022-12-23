import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test(size, addsize, getsize):
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
    print('get_reversed()')
    for i in range(getsize):
        print(str(i) + ': ' + str(rbuf.get_reversed(i)))

    print('-----------')
    print('get_all_reversed()')
    arr = rbuf.get_all_reversed()
    print(str(arr))
    for i in range(len(arr)):
        print(str(i) + ': ' + str(arr[i]))

def main():
    print('----------');
    test(3, 6, 10)
    print('')

    print('----------');
    test(3, 3, 5)
    print('')

    print('----------');
    test(3, 2, 5)
    print('')

    print('----------');
    test(3, 1, 2)
    print('')

    print('----------');
    test(3, 0, 2)
    print('')

main()

