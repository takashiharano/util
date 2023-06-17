import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

TEST_DATA_IN = [
    'aaa',
    'aaa bbb',
    'aaa bbb ccc',
    '\"aaa\"',
    '"aaa bbb"',
    'aaa "bbb ccc"',
    'aaa "bbb ccc" ddd',
    '"aaa\"bbb\""',
    '"aaa\"bbb\"" ccc',
    'aaa:"bbb ccc"',
    'aaa bbb:"ccc ddd"',
    'aaa bbb:"ccc ddd" eee'
]

TEST_DATA_IN2 = [
    '\'aaa\'',
    '\'aaa bbb\'',
    'aaa \'bbb ccc\'',
    'aaa \'bbb ccc\' ddd',
    '\'aaa\'bbb\'\'',
    '\'aaa\'bbb\'\' ccc',
    'aaa:\'bbb ccc\'',
    'aaa bbb:\'ccc ddd\'',
    'aaa bbb:\'ccc ddd\' eee'
]

TEST_DATA_IN3 = [
    '(aaa)',
    '(aaa bbb)',
    'aaa (bbb ccc)',
    '(aaa bbb) ccc',
    'aaa (bb(b) ccc)'
]

def test1(data_in):
    print('----------')
    print(data_in)
    word_list = util.split_keywords(data_in)
    for i in range(len(word_list)):
        word = word_list[i]
        print('[' + str(i) + '] ' + word)
    print('')

def main():
    for i in range(len(TEST_DATA_IN)):
        data_in = TEST_DATA_IN[i]
        test1(data_in)

    for i in range(len(TEST_DATA_IN2)):
        data_in = TEST_DATA_IN2[i]
        test1(data_in)

    for i in range(len(TEST_DATA_IN3)):
        data_in = TEST_DATA_IN3[i]
        test1(data_in)

main()
