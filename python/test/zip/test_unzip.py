import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test1():
    zip_path = 'C:/test/zip1.zip'
    out_path = 'C:/test/unzip/1'
    util.unzip(zip_path, out_path)
    return 'OK'

def test_dir():
    zip_path = 'C:/test/zip_dir.zip'
    out_path = 'C:/test/unzip/dir'
    util.unzip(zip_path, out_path)
    return 'OK'

def test_pass():
    zip_path = 'C:/test/zip_pass.zip'
    out_path = 'C:/test/unzip/pass'
    util.unzip(zip_path, out_path, pwd='pass123')
    return 'OK'

def main():
    ret = ''
    ret += test1() + '\n'
    ret += test_dir() + '\n'
    ret += test_pass() + '\n'
    ret += '\n'

    print(ret)

main()
