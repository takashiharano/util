#!python

import os
import sys
import re

sys.path.append(os.path.join(os.path.dirname(__file__), '..'))
import util

def test_typename():
  s = '\n'
  s += util.typename('abc')
  s += '\n'
  s += util.typename(123)
  s += '\n'
  s += util.typename(True)
  s += '\n'
  s += util.typename(None)
  s += '\n'
  return s

def test_contains():
  s = '\n'
  s += str(util.contains('abc', 'a'))
  s += '\n'
  s += str(util.contains('abc123', 'bc'))
  s += '\n'
  s += str(util.contains('abc123', '123'))
  s += '\n'
  s += str(util.contains('abc 123', '\s'))
  s += '\n'
  s += str(util.contains('abc 123xyz', '\d'))
  s += '\n'
  s += str(util.contains('abc123', '^abc'))
  s += '\n'
  s += str(util.contains('abc123', '^123'))
  s += '\n'
  s += str(util.contains('abc', 'z'))
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

def test_base64_decode():
  s = '\n'
  s += util.base64_decode('YWJj')
  s += '\n'
  s += util.base64_decode('YWJj', 'utf-8')
  s += '\n'
  s += util.base64_decode('YWJj', 'shift-jis')
  s += '\n'
  s += util.base64_decode('YWJj44GC44GE44GG', 'utf-8')
  s += '\n'
  s += util.base64_decode('YWJjgqCCooKk', 'shift-jis')
  return s

def test_base64_encode():
  s = '\n'
  s += util.base64_encode('abc')
  s += '\n'
  s += util.base64_encode('abc', 'utf-8')
  s += '\n'
  s += util.base64_encode('abc', 'shift-jis')
  s += '\n'
  s += util.base64_encode('abcあいう', 'utf-8')
  s += '\n'
  s += util.base64_encode('abcあいう', 'shift-jis')
  return s

def test_to_json():
  data = {
    'key1': 'val1',
    'key2': 'val2',
  }
  return util.to_json(data)

def test_from_json():
  return None

def test_get_relative_path():
  s = '\n'
  s += util.get_relative_path('./dir1/a.txt', './dir2')
  return s

def test_write_text_file():
  util.write_text_file('a.txt', 'aaa\nbbb\nccc')
  util.write_text_file('a-kana.txt', 'abcあいう')
  util.write_text_file('a-kana-utf8.txt', 'abcあいう', encoding='utf-8')
  util.write_text_file('a-kana-sjis.txt', 'abcあいう', encoding='shift-jis')
  return ''

def test_read_text_file():
  text = util.read_text_file('a.txt')
  #text = util.read_text_file('a.txt', 'shift-jis')
  ret = ''
  for line in text:
    ret += line

  ret += '\n\nread()\n'
  text = util.read_file('a.txt')
  for line in text:
    ret += line

  ret += '\n\nread(t)\n'
  text = util.read_file('a.txt', 't')
  for line in text:
    ret += line

  ret += '\n\nread(b)\n'
  text = util.read_file('a.txt', 'b')
  ret += text.hex()

  return ret

def test_delete_file():
  util.delete_file('z.txt')

def test_delete_dir():
  util.delete_dir('./z')

def test_rename_file():
  util.rename_file('a.txt', 'b.txt')

def test_send_result_json():
  status = 'OK'
  message = 'abc'
  body = '{"aaa": "bbb"}'
  util.send_result_json(status, message, body)
  #util.send_result_json(status)

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

def test():
  ret = '__file__ = ' + __file__ + '\n'
  ret += '\n'
  ret += 'test_typename() = ' + test_typename() + '\n'
  ret += '\n'
  ret += 'test_contains() = ' + test_contains() + '\n'
  ret += '\n'
  ret += 'test_replace() = ' + test_replace() + '\n'
  ret += '\n'
  ret += 'test_str_find() = ' + test_str_find() + '\n'
  ret += '\n'
  ret += 'test_base64_decode() = ' + test_base64_decode() + '\n'
  ret += '\n'
  ret += 'test_base64_encode() = ' + test_base64_encode() + '\n'
  ret += '\n'
  ret += 'test_to_json() = ' + test_to_json() + '\n'
  ret += '\n'
  ret += 'test_get_relative_path() = ' + test_get_relative_path() + '\n'
  ret += '\n'
  ret += 'test_write_text_file() = ' + test_write_text_file() + '\n'
  ret += '\n'
  ret += 'test_read_text_file() = ' + test_read_text_file() + '\n'
  ret += '\n'
  ret +='test_convert_newline() = ' + test_convert_newline() + '\n'
  ret += '\n'

  #test_delete_file()
  #test_delete_dir()
  #test_rename_file()

  return ret

def main():
  try:
    ret = test()
  except Exception as e:
    ret = str(e)

  #util.send_response('text', ret, encoding='utf-8')
  #util.send_response('text', ret, encoding='shift_jis')
  util.send_response('text', ret)

main()
