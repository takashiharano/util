#!python
#!/usr/bin/python3

import os
import sys

ROOT_DIR = '../'
sys.path.append(os.path.join(os.path.dirname(__file__), ROOT_DIR + 'libs'))
import util

import sysmon

sysmon.main()
