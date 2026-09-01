import os
import sys
import datetime

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test(data):
    data_in = data['in']
    fmt = data['fmt']
    tz = data['tz']
    data_exp = data['exp']

    data_out = util.get_datetime_str(data_in, fmt, tz)

    if data_out == data_exp:
        ret = 'OK'
    else:
        ret = 'NG'

    print('[' + ret + '] ' + 'IN=' + str(data_in) + ' ' + 'EXP=' + data_exp + ' ' + 'OUT=' + data_out)

def main():
    fmt = '%Y-%m-%d %H:%M:%S.%f'

    utc = datetime.timezone.utc
    jst = datetime.timezone(datetime.timedelta(hours=9))

    dt_utc = datetime.datetime(2026, 1, 2, 3, 34, 56, 789123, tzinfo=utc)

    tests = [
        {
            'in': dt_utc.timestamp(),
            'fmt': fmt,
            'tz': jst,
            'exp': '2026-01-02 12:34:56.789123'
        },
        {
            'in': dt_utc,
            'fmt': fmt,
            'tz': jst,
            'exp': '2026-01-02 12:34:56.789123'
        },
        {
            'in': datetime.datetime(
                2026, 1, 2, 12, 34, 56, 789123,
                tzinfo=jst
            ),
            'fmt': fmt,
            'tz': jst,
            'exp': '2026-01-02 12:34:56.789123'
        },
        {
            'in': datetime.datetime(
                2026, 1, 2, 12, 34, 56, 789123
            ),
            'fmt': fmt,
            'tz': jst,
            'exp': '2026-01-02 12:34:56.789123'
        }
    ]

    for data in tests:
        test(data)

main()
