#!python

import os
import sys
import re

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_match():
  s = '\n'
  s += str(util.match('abc', 'a'))
  s += '\n'
  s += str(util.match('abc123', 'bc'))
  s += '\n'
  s += str(util.match('abc123', '123'))
  s += '\n'
  s += str(util.match('abc 123', '\s'))
  s += '\n'
  s += str(util.match('abc 123xyz', '\d'))
  s += '\n'
  s += str(util.match('abc123', '^abc'))
  s += '\n'
  s += str(util.match('abc123', '^123'))
  s += '\n'
  s += str(util.match('abc', 'z'))
  return s

def test_replace():
  s = '\n'
  s += util.replace('abc', 'a', 'z')
  s += '\n'
  s += util.replace('abc123', 'bc', 'zy')
  s += '\n'
  s += util.replace('abc123', '123', '987')
  s += '\n'
  s += util.replace('abc123', '\d', 'zyx')
  s += '\n'
  s += util.replace('abc 123', '\s', '****')
  s += '\n'
  s += util.replace('abc 123', r'^ab', 'AB')
  s += '\n'
  s += util.replace('abc 123 abcabc', r'ab', 'AB')
  s += '\n'
  s += '\n'
  s += util.replace('abc 123 abcabc\nabcabcxyz', r'^ab', 'AB')
  s += '\n'
  s += '\n'
  s += util.replace('abc 123 abcabc\nabcabcxyz', r'^ab', 'AB', flags=re.MULTILINE)
  s += '\n'
  s += '\n'
  s += util.replace('123abcdefg123 abx abc456xyz789abcdefgxyz', r'ab(?P<name1>.)', r'AB\g<name1>')
  return s

def test_str_delete_patterns():
  s = '\n'
  s += util.delete_patterns('abc123aabb', ['^a', '\d'])
  return s

def test_get_tag_text():
  s = '\n'
  s += util.get_tag_text('<span>abc</span>', 'span')
  s += '\n'
  s += util.get_tag_text('<span >abc</span>', 'span')
  s += '\n'
  s += util.get_tag_text('<span id="a">abc</span>', 'span')
  s += '\n'
  s += util.get_tag_text('<spanz>abc</span>', 'span')
  s += '\n'
  s += util.get_tag_text('<span>abc</a>', 'span')
  s += '\n'
  s += util.get_tag_text('<span>abc</span>', 'a')
  s += '\n'
  s += util.get_tag_text('<span>abc<span>123</span>xyz</span>', 'span')
  s += '\n'
  s += util.get_tag_text('<span>abc</span><span>123</span>', 'span')
  return s

def test_str_find():
  s = '\n'
  m = util.str_find('abc', 'a')
  s += str(m) + '\n'
  s += 'm.group(0)=' + str(m.group(0)) + '\n'
  s += '\n'

  m = util.str_find('abc123', r'\d+')
  s += str(m) + '\n'
  s += 'm.group(0)=' + str(m.group(0)) + '\n'
  s += '\n'

  m = util.str_find('abc123 xyz', r'(\d+)\s(xy)z')
  s += str(m) + '\n'
  s += 'm.group(0)=' + str(m.group(0)) + '\n'
  s += 'm.group(1)=' + str(m.group(1)) + '\n'
  s += 'm.group(2)=' + str(m.group(2)) + '\n'
  s += '\n'

  return s

def test_half_full_width():
  ret = ''
  ret += '\n'
  ret += util.to_half_width('　！”＃＄％＆’（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［＼￥］＾＿‘ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～〜') + '\n'
  ret += util.to_half_width('　！”＃＄％＆’（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［＼￥］＾＿‘ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～〜', alphabet=False) + '\n'
  ret += util.to_half_width('　！”＃＄％＆’（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［＼￥］＾＿‘ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～〜', number=False) + '\n'
  ret += util.to_half_width('　！”＃＄％＆’（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［＼￥］＾＿‘ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～〜', symbol=False) + '\n'

  ret += '\n'
  ret += util.to_full_width(' !"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~') + '\n'
  ret += util.to_full_width(' !"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~', alphabet=False) + '\n'
  ret += util.to_full_width(' !"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~', number=False) + '\n'
  ret += util.to_full_width(' !"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~', symbol=False) + '\n'

  return ret

def test_convert_newline():
  ret = '\n'
  ret += 'lf=\n'
  ret += util.convert_newline('A\r\nB\nC\rD', '\n')
  ret += '\n\ncrlf=\n'
  ret += util.convert_newline('A\r\nB\nC\rD', '\r\n')
  ret += '\n\ncr=\n'
  ret += util.convert_newline('A\r\nB\nC\rD', '\r')
  ret += '\n\nbr=\n'
  ret += util.convert_newline('A\r\nB\nC\rD', '<br>')
  return ret

def main():
  ret = ''
  ret += '\n'
  ret += 'test_contains() = ' + test_match() + '\n'
  ret += '\n'
  ret += 'test_replace() = ' + test_replace() + '\n'
  ret += '\n'
  ret += 'test_str_delete_patterns() = ' + test_str_delete_patterns() + '\n'
  ret += '\n'
  ret += 'test_get_tag_text() = ' + test_get_tag_text() + '\n'
  ret += '\n'
  ret += 'test_str_find() = ' + test_str_find() + '\n'
  ret += '\n'
  ret += test_half_full_width()

  ret += '\n'
  ret +='test_convert_newline() = ' + test_convert_newline() + '\n'

  print('Content-Type: text/plain')
  print()
  print(ret)

main()
