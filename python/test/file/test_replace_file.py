import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    ret = util.replace_file('a.txt', r"(?P<p1>.*\.v\s+=\s+').*(?P<p2>';)", r'\g<p1>20180101-1234\g<p2>')
    return str(ret)

def main():
    try:
        ret = test()
    except Exception as e:
        ret = str(e)

    print(ret)

main()
