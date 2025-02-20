#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_get_dir_info():
    s = '\n'

    path = util.get_relative_path(__file__, 'a.txt')
    s += 'get_dir_info(\'' + path + '\') = ' + util.to_json(util.get_dir_info(path)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir1')
    s += 'get_dir_info(\'' + path + '\') = ' + util.to_json(util.get_dir_info(path)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir2')
    s += 'get_dir_info(\'' + path + '\') = ' + util.to_json(util.get_dir_info(path)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir2')
    s += 'get_dir_info(\'' + path + '\', recursive=0) = ' + util.to_json(util.get_dir_info(path, recursive=0)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir2')
    s += 'get_dir_info(\'' + path + '\', recursive=2) = ' + util.to_json(util.get_dir_info(path, recursive=2)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir2')
    s += 'get_dir_info(\'' + path + '\', True, pattern=r\'.+\\.log\', recursive=0) = ' + util.to_json(util.get_dir_info(path, pattern=r'.+\\.log', recursive=0)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir2')
    s += 'get_dir_info(\'' + path + '\', pattern=r\'.*log.*\', recursive=0) = ' + util.to_json(util.get_dir_info(path, pattern=r'.*log.*', recursive=0)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir2')
    s += 'get_dir_info(\'' + path + '\', recursive=0, depth=1) = ' + util.to_json(util.get_dir_info(path, recursive=0, depth=1)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir2')
    s += 'get_dir_info(\'' + path + '\', recursive=0, depth=2) = ' + util.to_json(util.get_dir_info(path, recursive=0, depth=2)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir2/')
    s += 'get_dir_info(\'' + path + '\', recursive=0, depth=2) = ' + util.to_json(util.get_dir_info(path, recursive=0, depth=2)) + '\n'

    s += '\n'
    path = util.get_relative_path(__file__, 'dir2/aaa.txt')
    s += 'get_dir_info(\'' + path + '\') = ' + util.to_json(util.get_dir_info(path)) + '\n'

    #s += '\n'
    #path ='C:/Program Files/'
    #s += 'get_dir_info(\'' + path + '\', recursive=0, depth=1) = ' + util.to_json(util.get_dir_info(path, recursive=0, depth=1)) + '\n'

    s += '\n'
    path ='C:/notexist/'
    try:
        s += 'get_dir_info(\'' + path + '\', recursive=0, depth=1) = ' + util.to_json(util.get_dir_info(path, recursive=0, depth=1)) + '\n'
    except FileNotFoundError as e:
        s += str(e)

    return s

def test_file():
    s = '\n'
    s += 'test_get_dir_info() = ' + test_get_dir_info() + '\n'
    return s

def main():
    path = util.get_arg(1)
    if path == '':
        s = test_file()
    else:
        s = util.to_json(util.get_dir_info(path)) + '\n'
    print(s)

main()
