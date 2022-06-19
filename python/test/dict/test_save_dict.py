import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    ret = 'OK'
    obj = {
        'str': 'val1',
        'num': 123,
        'flag': True
    }
    util.save_dict('dict_saved.json', obj)
    util.save_dict('dict_saved_None.json', None)
    util.save_dict('./data/dict_saved.json', obj)
    return ret

def main():
    try:
        ret = test()
    except Exception as e:
        ret = str(e)
    print(ret)

main()
