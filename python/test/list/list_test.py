import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_next_list_val():
    ret = ''

    arr = ['a', 'b', 'c', 'd', 'e']

    ret += '\n'
    ret += "['a', 'b', 'c', 'd', 'e']\n"
    ret += '\n'
    ret += 'a\n'
    ret += util.next_list_val(arr, 'a') + '\n'
    ret += '\n'
    ret += 'b\n'
    ret += util.next_list_val(arr, 'b') + '\n'
    ret += '\n'
    ret += 'd\n'
    ret += util.next_list_val(arr, 'd') + '\n'
    ret += '\n'
    ret += 'e\n'
    ret += util.next_list_val(arr, 'e') + '\n'
    ret += '\n'
    ret += 'z\n'
    ret += util.next_list_val(arr, 'z') + '\n'

    ret += '\n'

    ret += '\n'
    ret += 'a, 2\n'
    ret += util.next_list_val(arr, 'a', 2) + '\n'
    ret += '\n'
    ret += 'e, 2\n'
    ret += util.next_list_val(arr, 'e', 2) + '\n'
    ret += '\n'
    ret += 'a, -1\n'
    ret += util.next_list_val(arr, 'a', -1) + '\n'
    ret += '\n'
    ret += 'e, -1\n'
    ret += util.next_list_val(arr, 'e', -1) + '\n'
    ret += '\n'
    ret += 'e, -2\n'
    ret += util.next_list_val(arr, 'e', -2) + '\n'

    ret += '\n'
    ret += 'a, 5\n'
    ret += util.next_list_val(arr, 'a', 5) + '\n'
    ret += '\n'
    ret += 'a, 6\n'
    ret += util.next_list_val(arr, 'a', 6) + '\n'

    ret += '\n'
    ret += 'a, -5\n'
    ret += util.next_list_val(arr, 'a', -5) + '\n'
    ret += '\n'
    ret += 'a, -6\n'
    ret += util.next_list_val(arr, 'a', -6) + '\n'
    return ret

def main():
    ret = test_next_list_val()
    print(ret)

main()
