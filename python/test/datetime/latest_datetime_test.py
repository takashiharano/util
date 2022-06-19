import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test():
    ret = ''

    arr = ['20190626T235900', '20190627T000000', '20190627T060000', '20190627T120000', '20190627T123000', '20190627T150000', '20190628T060000']

    ret += str(arr) + '\n\n'

    c = '20190625T120000'
    t = util.get_latest_datetime(arr, c, fmt='%Y%m%dT%H%M%S')
    ret += '\n'
    ret += c + ' : ' + str(t) + '\n'

    c = '20190627T100000'
    t = util.get_latest_datetime(arr, c, fmt='%Y%m%dT%H%M%S')
    ret += '\n'
    ret += c + ' : ' + t + '\n'

    c = '20190627T121500'
    t = util.get_latest_datetime(arr, c, fmt='%Y%m%dT%H%M%S')
    ret += '\n'
    ret += c + ' : ' + t + '\n'

    c = '20190627T235959'
    t = util.get_latest_datetime(arr, c, fmt='%Y%m%dT%H%M%S')
    ret += '\n'
    ret += c + ' : ' + t + '\n'

    c = '20190629T120000'
    t = util.get_latest_datetime(arr, c, fmt='%Y%m%dT%H%M%S')
    ret += '\n'
    ret += c + ' : ' + t + '\n'

    c = None
    t = util.get_latest_datetime(arr, c, fmt='%Y%m%dT%H%M%S')
    ret += '\n'
    ret += str(c) + ' : ' + t + '\n'

    ret += '--------\n'

    c = '20190627T100000'
    t = util.get_latest_datetime(arr, c, fmt='%Y%m%dT%H%M%S', offset=1)
    ret += '\n'
    ret += c + ' , offset=1: ' + str(t) + '\n'

    c = '20190627T100000'
    t = util.get_latest_datetime(arr, c, fmt='%Y%m%dT%H%M%S', offset=4)
    ret += '\n'
    ret += c + ' , offset=4: ' + str(t) + '\n'

    c = '20190627T100000'
    t = util.get_latest_datetime(arr, c, fmt='%Y%m%dT%H%M%S', offset=-1)
    ret += '\n'
    ret += c + ' , offset=-1: ' + str(t) + '\n'

    c = '20190627T100000'
    t = util.get_latest_datetime(arr, c, fmt='%Y%m%dT%H%M%S', offset=-4)
    ret += '\n'
    ret += c + ' , offset=-4: ' + str(t) + '\n'

    c = '20190627T100000'
    t = util.get_latest_datetime(arr, c, fmt='%Y%m%dT%H%M%S', offset=-5)
    ret += '\n'
    ret += c + ' , offset=-5: ' + str(t) + '\n'

    return ret

def main():
    ret = test()
    print(ret)

main()
