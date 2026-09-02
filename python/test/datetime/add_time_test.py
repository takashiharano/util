import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test(data):
    t1 = data['t1']
    t2 = data['t2']
    by_the_day = data.get('by_the_day', True)
    exp = data['exp']

    time = util.time_add(t1, t2)
    out = time.to_str('HMSsD', by_the_day=by_the_day)

    if out == exp:
        ret = 'OK'
    else:
        ret = 'NG'

    print(
        '[' + ret + '] '
        + 'T1=' + str(t1)
        + ' T2=' + str(t2)
        + ' BY_THE_DAY=' + str(by_the_day)
        + ' EXP=' + str(exp)
        + ' OUT=' + str(out)
    )

def main():
    tests = [
        # Positive values
        {
            't1': '12:00',
            't2': '01:30',
            'exp': '13:30:00.000000'
        },
        {
            't1': '12:00',
            't2': '13:00',
            'exp': '01:00:00.000000 (+1 Day)'
        },

        # Negative total
        {
            't1': '01:00',
            't2': '-02:00',
            'exp': '23:00:00.000000 (-1 Day)'
        },
        {
            't1': '00:30',
            't2': '-01:00',
            'exp': '23:30:00.000000 (-1 Day)'
        },

        # Negative integrated representation
        {
            't1': '01:00',
            't2': '-02:00',
            'by_the_day': False,
            'exp': '-01:00:00.000000'
        },
        {
            't1': '00:30',
            't2': '-01:00',
            'by_the_day': False,
            'exp': '-00:30:00.000000'
        },

        # Multiple days
        {
            't1': '01:00',
            't2': '48:00',
            'exp': '01:00:00.000000 (+2 Days)'
        },
        {
            't1': '01:00',
            't2': '-49:00',
            'exp': '00:00:00.000000 (-2 Days)'
        },

        # Exact day boundaries
        {
            't1': '00:00',
            't2': '24:00',
            'exp': '00:00:00.000000 (+1 Day)'
        },
        {
            't1': '00:00',
            't2': '-24:00',
            'exp': '00:00:00.000000 (-1 Day)'
        },

        # Zero
        {
            't1': '01:00',
            't2': '-01:00',
            'exp': '00:00:00.000000'
        }
    ]

    for data in tests:
        test(data)

main()
