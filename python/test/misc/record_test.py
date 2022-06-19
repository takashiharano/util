#!python

import os
import sys
import datetime

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_tab():
    a = util.Record()
    a.add('abc')
    a.add('xyz')
    s = '\n'
    s += a.to_str()
    return s

def test_commma():
    a = util.Record()
    a.add('abc')
    a.add('xyz')
    a.add('1\n23')
    s = '\n'
    s += a.to_str(sep=',')
    return s

def test_commma_dblquot():
    a = util.Record()
    a.add('abc')
    a.add('x"y"z')
    a.add('1\n23')
    s = '\n'
    s += 'csv=false          ' + a.to_str(sep=',', csvmode=False) + '\n'
    s += 'csv=true           ' + a.to_str(sep=',', csvmode=True) + '\n'
    s += 'csv=true:quot=true ' + a.to_str(sep=',', csvmode=True, quot=True) + '\n'
    return s

def test():
    ret = 'test_tab() = ' + test_tab()
    ret += '\n'
    ret += 'test_commma() = ' + test_commma()
    ret += '\n'
    ret += 'test_commma_dblquot() = ' + test_commma_dblquot()
    return ret

def main():
    try:
        ret = test()
    except Exception as e:
        ret = str(e)
    util.send_response('text', ret)

main()
