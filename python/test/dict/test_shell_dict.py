import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    ret = ''

    obj = {
        "a": {
            "b": "c"
        }
    }
    ret += _test(obj)

    obj = {
        "a": {
            "b": "c"
        },
        "b": {
            "e": "f"
        }
    }
    ret += _test(obj)

    obj = "abc"
    ret += _test(obj)

    obj = [1, 2, 3]
    ret += _test(obj)

    obj = {
        "a": "b"
    }
    ret += _test(obj)

    obj = {
        "a": {
            "b": "c",
            "d": "e"
        }
    }
    ret += _test(obj)

    obj = {
        "a": {
           "b": {
               "c": "d"
           }
        }
    }
    ret += _test(obj)

    return ret

def _test(o1):
    o2 = util.shell_dict(o1)
    ret = str(o1)
    ret += '\n'
    ret += str(o2)
    ret += '\n'
    ret += '\n'
    return ret

def main():
    try:
        ret = test()
    except Exception as e:
        ret = str(e)
    print(ret)

main()
