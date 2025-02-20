import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_get_file_info():
    s = '\n'

    path = util.get_relative_path(__file__, 'a.txt')
    s += 'get_file_info(\'' + path + '\') = ' + util.to_json(util.get_file_info(path)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir1')
    s += 'get_file_info(\'' + path + '\') = ' + util.to_json(util.get_file_info(path)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir2')
    s += 'get_file_info(\'' + path + '\') = ' + util.to_json(util.get_file_info(path)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir2/')
    s += 'get_file_info(\'' + path + '\') = ' + util.to_json(util.get_file_info(path)) + '\n'

    #path = util.get_relative_path(__file__, 'notexist')
    #s += 'get_file_info(\'' + path + '\') = ' + util.to_json(util.get_file_info(path)) + '\n'

    return s

def test_file():
    s = '\n'
    s += 'test_get_file_info() = ' + test_get_file_info() + '\n'
    return s

def main():
    path = util.get_arg(1)
    if path == '':
        s = test_file()
    else:
        s = util.to_json(util.get_file_info(path)) + '\n'
    print(s)

main()
