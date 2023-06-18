import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

TEST_DATA_IN = [
    '"aaa"',
    '"aaa bbb"',
    '"aaa" bbb',
    'aaa "bbb"',
    '\\"aaa\\"'
]

TEST_DATA_IN2 = [
    '\'aaa\'',
    '\'aaa bbb\'',
    '\'aaa\' bbb',
    'aaa \'bbb\'',
    '\\\'aaa\\\''
]

TEST_DATA_IN3 = [
    '(aaa)',
    '(aaa bbb)',
    '(aaa) bbb',
    'aaa (bbb)'
]

def test1(data_in, q1='"', q2=None):
    print('----------')
    print('in =[' + data_in + ']')
    s = util.extract_quoted_string(data_in, q1, q2)
    print('out=[' + s + ']')
    print('')

def main():
    for i in range(len(TEST_DATA_IN)):
        data_in = TEST_DATA_IN[i]
        test1(data_in)

    for i in range(len(TEST_DATA_IN2)):
        data_in = TEST_DATA_IN2[i]
        test1(data_in, '\'')

    for i in range(len(TEST_DATA_IN3)):
        data_in = TEST_DATA_IN3[i]
        test1(data_in, '(', ')')

main()
