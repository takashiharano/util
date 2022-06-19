import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    ret = _recursive_dir('./', 'dir2', 0)
    return str(ret)

def _recursive_dir(parent, dir_name, lv):
    ret = ''

    this_path = parent + dir_name
    if not this_path.endswith('/'):
        this_path += '/'

    target_path = this_path

    list = util.list_dir(target_path)
    for item in list:
        path = this_path + item
        is_dir = util.is_dir(path)
        file_info = util.str_padding(item + ' ' + str(is_dir) , ' ', lv) + '\n'
        ret += file_info
        if is_dir:
            ret += _recursive_dir(this_path, item, lv + 1)

    return ret

def main():
    try:
        ret = test()
    except Exception as e:
        ret = str(e)

    print(ret)

main()
