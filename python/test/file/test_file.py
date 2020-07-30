#!python

import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_get_parent_path():
  s = '\n'
  s += './dir1/a.txt      = ' + util.get_parent_path('./dir1/a.txt') + '\n'
  s += './dir1/dir2/a.txt = ' + util.get_parent_path('./dir1/dir2/a.txt') + '\n'
  s += './dir1/dir2       = ' + util.get_parent_path('./dir1/dir2') + '\n'
  s += './dir1/dir2/      = ' + util.get_parent_path('./dir1/dir2/') + '\n'
  s += 'a.txt             = ' + util.get_parent_path('a.txt') + '\n'
  s += 'C:/a.txt          = ' + util.get_parent_path('C:/a.txt') + '\n'
  s += 'C:\\a.txt          = ' + util.get_parent_path('C:\\a.txt') + '\n'
  s += 'C:/               = ' + util.get_parent_path('C:/') + '\n'
  s += 'C:\\               = ' + util.get_parent_path('C:\\') + '\n'
  return s

def test_get_filename():
  s = '\n'
  s += 'abc             = ' + util.get_filename('abc') + '\n'
  s += './abc.txt       = ' + util.get_filename('./abc.txt') + '\n'
  s += 'abc.ext         = ' + util.get_filename('abc.ext') + '\n'
  s += 'abc.def.ext     = ' + util.get_filename('abc.def.ext') + '\n'
  s += 'aaa/abc         = ' + util.get_filename('aaa/abc') + '\n'
  s += 'aaa/abc/        = ' + util.get_filename('aaa/abc/') + '\n'
  s += 'aaa/abc/zzz.txt = ' + util.get_filename('aaa/abc/zzz.txt') + '\n'
  s += 'aaa/abc/zzz     = ' + util.get_filename('aaa/abc/zzz') + '\n'
  s += 'aaa/abc/zzz/    = ' + util.get_filename('aaa/abc/zzz/') + '\n'
  s += 'aaa/abc/zzz\\    = ' + util.get_filename('aaa/abc/zzz\\') + '\n'
  s += 'aaa/abc.ext     = ' + util.get_filename('aaa/abc.ext') + '\n'
  s += 'aaa\\abc         = ' + util.get_filename('aaa\\abc') + '\n'
  s += 'aaa\\abc.ext     = ' + util.get_filename('aaa\\abc.ext') + '\n'
  return s

def test_get_file_name():
  s = '\n'
  s += 'abc         = ' + util.get_file_name('abc') + '\n'
  s += 'abc.ext     = ' + util.get_file_name('abc.ext') + '\n'
  s += 'abc.def.ext = ' + util.get_file_name('abc.def.ext') + '\n'
  s += 'aaa/abc     = ' + util.get_file_name('aaa/abc') + '\n'
  s += 'aaa/abc/    = ' + util.get_file_name('aaa/abc/') + '\n'
  s += 'aaa/abc.ext = ' + util.get_file_name('aaa/abc.ext') + '\n'
  s += 'aaa\\abc     = ' + util.get_file_name('aaa\\abc') + '\n'
  s += 'aaa\\abc.ext = ' + util.get_file_name('aaa\\abc.ext') + '\n'
  s += 'aaa\\abc.def\\xyz.ext = ' + util.get_file_name('aaa\\abc.def\\xyz.ext') + '\n'
  return s

def test_get_file_ext():
  s = '\n'
  s += 'abc         = ' + util.get_file_ext('abc') + '\n'
  s += 'abc.ext     = ' + util.get_file_ext('abc.ext') + '\n'
  s += 'abc.def.ext = ' + util.get_file_ext('abc.def.ext') + '\n'
  s += 'aaa/abc     = ' + util.get_file_ext('aaa/abc') + '\n'
  s += 'aaa/abc/    = ' + util.get_file_ext('aaa/abc/') + '\n'
  s += 'aaa/abc.ext = ' + util.get_file_ext('aaa/abc.ext') + '\n'
  s += 'aaa\\abc     = ' + util.get_file_ext('aaa\\abc') + '\n'
  s += 'aaa\\abc.ext = ' + util.get_file_ext('aaa\\abc.ext') + '\n'
  s += 'aaa\\abc.def\\xyz.ext = ' + util.get_file_ext('aaa\\abc.def\\xyz.ext') + '\n'
  return s

def test_join_path():
  s = '\n'
  s += "'./dir1', 'a.txt'   = " + util.join_path('./dir1', 'a.txt') + '\n'
  s += "'./dir1/', 'a.txt'  = " + util.join_path('./dir1/', 'a.txt') + '\n'
  s += "'./dir1', '/a.txt'  = " + util.join_path('./dir1', '/a.txt') + '\n'
  s += "'./dir1/', '/a.txt' = " + util.join_path('./dir1/', '/a.txt') + '\n'
  return s

def test_path_exists():
  s = '\n'
  s += 'a.txt = ' + str(util.path_exists('a.txt')) + '\n'
  s += 'b.txt = ' + str(util.path_exists('b.txt')) + '\n'
  s += 'dir0  = ' + str(util.path_exists('dir0')) + '\n'
  s += 'dir1  = ' + str(util.path_exists('dir1')) + '\n'
  return s

def test_is_file():
  s = '\n'
  s += 'a.txt = ' + str(util.is_file('a.txt')) + '\n'
  s += 'b.txt = ' + str(util.is_file('b.txt')) + '\n'
  return s

def test_is_dir():
  s = '\n'
  s += './    = ' + str(util.is_dir('./')) + '\n'
  s += 'a.txt = ' + str(util.is_dir('a.txt')) + '\n'
  return s

def test_list_dir():
  ret = '\n'
  ret += './=' + str(util.list_dir('./dir2')) + '\n'
  ret += './=' + str(util.list_dir('./dir2', pattern=r'\d.+')) + '\n'
  ret += './=' + str(util.list_dir('./dir2', pattern=r'lang_.*')) + '\n'
  return ret

def test_list_files():
  ret = '\n'
  ret += './=' + str(util.list_files('./dir2')) + '\n'
  ret += './=' + str(util.list_files('./dir2', pattern=r'\d.+')) + '\n'
  ret += './=' + str(util.list_files('./dir2', pattern=r'lang_.*')) + '\n'
  return ret

def test_list_dirs():
  ret = '\n'
  ret += './=' + str(util.list_dirs('./dir2')) + '\n'
  ret += './=' + str(util.list_dirs('./dir2', pattern=r'\d.+')) + '\n'
  ret += './=' + str(util.list_dirs('./dir2', pattern=r'b')) + '\n'
  return ret

def test_delete_file():
  util.delete_file('z.txt')

def test_delete_dir():
  util.delete_dir('./z')

def test_rename_file():
  util.rename_file('a.txt', 'b.txt')


def test_file():
  s = '\n'
  s += 'test_get_parent_path() = ' + test_get_parent_path() + '\n'
  s += '\n'
  s += 'test_get_filename() = ' + test_get_filename() + '\n'
  s += '\n'
  s += 'test_get_file_name() = ' + test_get_file_name() + '\n'
  s += '\n'
  s += 'test_get_file_ext() = ' + test_get_file_ext() + '\n'
  s += '\n'
  s += 'test_join_path() = ' + test_join_path() + '\n'
  s += '\n'
  s += 'test_path_exists() = ' + test_path_exists() + '\n'
  s += '\n'
  s += 'test_is_file() = ' + test_is_file() + '\n'
  s += '\n'
  s += 'test_is_dir() = ' + test_is_dir() + '\n'
  s += '\n'
  path = util.get_relative_path(__file__, 'dir2')
  s += 'get_size(\'' + path + '\')       = ' + str(util.get_size(path)) + '\n'

  path = util.get_relative_path(__file__, 'dir2/')
  s += 'get_size(\'' + path + '\')      = ' + str(util.get_size(path)) + '\n'

  path = util.get_relative_path(__file__, 'dir2')
  s += 'get_size(\'' + path + '\', True) = ' + str(util.get_size(path, True)) + '\n'

  path = util.get_relative_path(__file__, 'a.txt')
  s += 'get_size(\'' + path + '\')      = ' + str(util.get_size(path)) + '\n'

  path = util.get_relative_path(__file__, 'dir1')
  s += 'get_size(\'' + path + '\')       = ' + str(util.get_size(path)) + '\n'

  s += '\n'
  s += 'test_list_dir() = ' + test_list_dir() + '\n'
  s += '\n'
  s += 'test_list_files() = ' + test_list_files() + '\n'
  s += '\n'
  s += 'test_list_dirs() = ' + test_list_dirs() + '\n'

  #test_delete_file()
  #test_delete_dir()
  #test_rename_file()

  return s

def main():
  s = test_file()
  util.send_response('text', s)

main()
