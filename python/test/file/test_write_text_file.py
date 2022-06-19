import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_write_text_file():
    base_path = 'C:/test/'
    util.write_text_file(base_path + 'a.txt', 'aaa\nbbb\nccc')
    util.write_text_file(base_path + 'a-kana.txt', 'abcあいう')
    util.write_text_file(base_path + 'a-kana-utf8.txt', 'abcあいう', encoding='utf-8')
    util.write_text_file(base_path + 'a-kana-sjis.txt', 'abcあいう', encoding='shift-jis')
    return ''

def test():
    ret = 'test_write_text_file() = ' + test_write_text_file() + '\n'
    return ret

def main():
    try:
        ret = test()
    except Exception as e:
        ret = str(e)

    print(ret)

main()
