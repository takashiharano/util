import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test(data):
    data_in = data['in']
    ndigits = data.get('ndigits')
    data_exp = data['exp']

    data_out = util.clock2float(data_in, ndigits)

    if data_out == data_exp:
        ret = 'OK'
    else:
        ret = 'NG'

    print(
        '[' + ret + '] '
        + 'IN=' + str(data_in)
        + ' NDIGITS=' + str(ndigits)
        + ' EXP=' + str(data_exp)
        + ' OUT=' + str(data_out)
    )

def main():
    tests = [
        # Positive values
        {
            'in': '07:00',
            'exp': 7.0
        },
        {
            'in': '07:30',
            'exp': 7.5
        },
        {
            'in': '07:45',
            'exp': 7.75
        },
        {
            'in': '00:30',
            'exp': 0.5
        },

        # Negative values
        {
            'in': '-07:00',
            'exp': -7.0
        },
        {
            'in': '-07:30',
            'exp': -7.5
        },
        {
            'in': '-07:45',
            'exp': -7.75
        },

        # Verify that the sign of "-00" is preserved.
        {
            'in': '-00:30',
            'exp': -0.5
        },
        {
            'in': '-00:45',
            'exp': -0.75
        },

        # Zero
        {
            'in': '00:00',
            'exp': 0.0
        },
        {
            'in': '-00:00',
            'exp': 0.0
        },

        # Rounding
        {
            'in': '07:20',
            'ndigits': 2,
            'exp': 7.33
        },
        {
            'in': '-00:20',
            'ndigits': 2,
            'exp': -0.33
        },
        {
            'in': '-07:20',
            'ndigits': 2,
            'exp': -7.33
        }
    ]

    for data in tests:
        test(data)

main()
