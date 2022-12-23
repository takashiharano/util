import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test(size, addsize):
    TEST_DATA = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N']
    arr = []

    for i in range(addsize):
        data = TEST_DATA[i]
        print('add(' + str(i) + '): ' + data)
        arr = util.unshift_as_ringbuffer(arr, data, size)

    print('len: ' + str(len(arr)))
    print('-----------')
    print(str(arr))

def main():
    print('----------');
    test(3, 0)
    print('')

    print('----------');
    test(3, 1)
    print('')

    print('----------');
    test(3, 2)
    print('')

    print('----------');
    test(3, 3)
    print('')

    print('----------');
    test(3, 4)
    print('')

    print('----------');
    test(3, 5)
    print('')

    print('----------');
    test(3, 6)
    print('')

main()
