#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

text1 = '01: AAAAAAAAAA\n'
text1 += '02: BBB<start>BBB123BBBB\n'
text1 += '03: CCCCCCCCCC\n'
text1 += '04: DD45 6DDDDD<end>DDD\n'
text1 += '05: EEEEEEEEEE\n'

text2 = '01:AAAAAAAAAA\n'
text2 += '02:BBB<start>BBB123BBBB\n'
text2 += '03:CCCCCCCCCC\n'
text2 += '04:DD45 6DDDDD<end>DDD\n'
text2 += '05:EEEEEEEEEE\n'
text2 += '06:AAAAAAAAAA\n'
text2 += '07:BBB<start>BBB123BBBB\n'
text2 += '08:CCCCCCCCCC\n'
text2 += '09:DD45 6DDDDD<end>DDD\n'
text2 += '10:EEEEEEEEEE\n'

text3 = '01:AAAAAAAAAA\n'
text3 += '02:BBB<start>BBB123BBBB\n'
text3 += '03:CCCCCCCCCC\n'
text3 += '04:AAAAAAAAAA\n'
text3 += '05:BBB<start>BBB123BBBB\n'
text3 += '06:CCCCCCCCCC\n'
text3 += '07:DD45 6DDDDD<end>DDD\n'
text3 += '08:EEEEEEEEEE\n'
text3 += '09:DD45 6DDDDD<end>DDD\n'
text3 += '10:EEEEEEEEEE\n'

text4 = '01: AAAAAAAAAA\n'
text4 += '02: B<start>BB\n'
text4 += '03: CCCCCCCCCC\n'
text4 += '04: B<start>BB\n'
text4 += '05: CCCCCCCCCC\n'
text4 += '06: B<start>BB\n'
text4 += '07: CCCCCCCCCC\n'
text4 += '08: DD<end>DDD\n'
text4 += '09: EEEEEEEEEE\n'
text4 += '10: DD<end>DDD\n'
text4 += '11: EEEEEEEEEE\n'
text4 += '12: DD<end>DDD\n'
text4 += '13: FFFFFFFFFF\n'

def test1():
  ret = ''
  ret += '------------\n'
  ret += 'test1\n'
  ret += '------------\n'
  ret += 'start, end\n'
  ret += util.extract_text(text1, 'start', 'end')
  ret += '\n'
  ret += '------------\n'
  ret += 'start, end, True, False\n'
  ret += util.extract_text(text1, 'start', 'end', True, False)
  ret += '\n'
  ret += '------------\n'
  ret += 'start, end, False, True\n'
  ret += util.extract_text(text1, 'start', 'end', False, True)
  ret += '\n'
  ret += '------------\n'
  ret += 'start, end, False, False\n'
  ret += util.extract_text(text1, 'start', 'end', False, False)
  ret += '\n'
  return ret

def test2():
  ret = '---------------------------------------'
  ret += '------------\n'
  ret += 'test2\n'
  ret += '------------\n'
  ret += 'nostart, end\n'
  ret += util.extract_text(text1, 'nostart', 'end')
  ret += '\n'
  ret += '------------\n'
  ret += 'start, noend\n'
  ret += util.extract_text(text1, 'start', 'noend')
  ret += '\n'
  ret += '------------\n'
  ret += 'nostart, noend\n'
  ret += util.extract_text(text1, 'nostart', 'noend')
  ret += '\n'
  return ret

def test3():
  ret = ''
  ret += '------------\n'
  ret += 'test3\n'
  ret += '------------\n'
  ret += '\\d, \\d\\s\\d\n'
  ret += util.extract_text(text1, r'\d', r'\d\s\d')
  ret += '\n'
  return ret

def test4():
  ret = ''
  ret += '------------\n'
  ret += 'test4\n'
  ret += '------------\n'
  ret += 'start, 123\n'
  ret += util.extract_text(text1, r'start', '123')
  ret += '\n'
  ret += '------------\n'
  ret += '123, start\n'
  ret += util.extract_text(text1, r'123', 'start')
  ret += '\n'
  return ret

def test5():
  ret = ''
  ret += '------------\n'
  ret += 'start, end, greedy=True\n'
  ret += util.extract_text(text2, r'start', 'end', greedy=True)
  ret += '\n'
  ret += '------------\n'
  ret += 'start, end, greedy=False\n'
  ret += util.extract_text(text2, r'start', 'end', greedy=False)
  ret += '\n'
  return ret

def test6():
  ret = ''
  ret += '------------\n'
  ret += 'test6\n'
  ret += '------------\n'
  ret += 'start, end, greedy=True\n'
  ret += util.extract_text(text3, r'start', 'end', greedy=True)
  ret += '\n'
  ret += '------------\n'
  ret += 'start, end, greedy=False\n'
  ret += util.extract_text(text3, r'start', 'end', greedy=False)
  ret += '\n'
  return ret

def test7():
  ret = ''
  ret += '------------\n'
  ret += 'test7\n'
  ret += '------------\n'
  ret += 'start, end, greedy=True, count=1\n'
  ret += util.extract_text(text4, r'start', 'end', greedy=True, count=1)
  ret += '\n'
  ret += '------------\n'
  ret += 'start, end, greedy=True, count=2\n'
  ret += util.extract_text(text4, r'start', 'end', greedy=True, count=2)
  ret += '\n'
  return ret

def test8():
  ret = ''
  ret += '------------\n'
  ret += 'test8\n'
  ret += '------------\n'
  ret += 'start=None\n'
  ret += util.extract_text(text1, start=None, end='end')
  ret += '\n'
  ret += '------------\n'
  ret += 'end=None\n'
  ret += util.extract_text(text1, start='start', end=None)
  ret += '\n'
  ret += '------------\n'
  ret += 'start=None, end=None\n'
  ret += util.extract_text(text1, start=None, end=None)
  ret += '\n'
  return ret

def main():
  ret = ''
  ret += test1()
  ret += '\n'
  ret += test2()
  ret += '\n'
  ret += test3()
  ret += '\n'
  ret += test4()
  ret += '\n'
  ret += test5()
  ret += '\n'
  ret += test6()
  ret += '\n'
  ret += test7()
  ret += '\n'
  ret += test8()

  print('Content-Type: text/plain')
  print()
  print(ret)

main()
