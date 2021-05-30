import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_dir():
  out_path = 'C:/tmp/zip_dir.zip'
  target_file_path = 'C:/test/dir1'
  util.zip(out_path, target_file_path)

def test_dir_excl_root_path():
  out_path = 'C:/tmp/zip_dir_excl_root_path.zip'
  target_file_path = 'C:/test/dir1'
  util.zip(out_path, target_file_path, excl_root_path=True)

def main():
  print('ZIP dir')
  test_dir()

  print('ZIP dir (excl. root dir)')
  test_dir_excl_root_path()

  print('OK')

main()
