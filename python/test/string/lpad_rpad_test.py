import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test(data):
    func = data['func']
    data_in = data['in']
    ch = data['ch']
    ln = data['len']
    adj = data.get('adj', False)
    exp = data['exp']

    if func == 'lpad':
        out = util.lpad(data_in, ch, ln, adj)
    else:
        out = util.rpad(data_in, ch, ln, adj)

    if out == exp:
        ret = 'OK'
    else:
        ret = 'NG'

    print(
        '[' + ret + '] '
        + 'FUNC=' + func
        + ' IN=' + repr(data_in)
        + ' CH=' + repr(ch)
        + ' LEN=' + str(ln)
        + ' ADJ=' + str(adj)
        + ' EXP=' + repr(exp)
        + ' OUT=' + repr(out)
    )

def main():
    tests = [
        # ------------------------------------------------------------
        # lpad
        # ------------------------------------------------------------
        {
            'func': 'lpad',
            'in': '',
            'ch': '0',
            'len': 5,
            'exp': '00000'
        },
        {
            'func': 'lpad',
            'in': 'ABC',
            'ch': '0',
            'len': 5,
            'exp': '00ABC'
        },
        {
            'func': 'lpad',
            'in': 'ABCDE',
            'ch': '0',
            'len': 5,
            'exp': 'ABCDE'
        },
        {
            'func': 'lpad',
            'in': 'ABCDEF',
            'ch': '0',
            'len': 5,
            'exp': 'ABCDEF'
        },
        {
            'func': 'lpad',
            'in': 1,
            'ch': '0',
            'len': 5,
            'exp': '00001'
        },

        # adj=True truncates a value longer than the specified length.
        {
            'func': 'lpad',
            'in': 'ABCDEF',
            'ch': '0',
            'len': 5,
            'adj': True,
            'exp': 'ABCDE'
        },

        # adj=True has no effect when the length already matches.
        {
            'func': 'lpad',
            'in': 'ABCDE',
            'ch': '0',
            'len': 5,
            'adj': True,
            'exp': 'ABCDE'
        },

        # adj=True still pads a value shorter than the specified length.
        {
            'func': 'lpad',
            'in': 'ABC',
            'ch': '0',
            'len': 5,
            'adj': True,
            'exp': '00ABC'
        },

        # ------------------------------------------------------------
        # rpad
        # ------------------------------------------------------------
        {
            'func': 'rpad',
            'in': '',
            'ch': '0',
            'len': 5,
            'exp': '00000'
        },
        {
            'func': 'rpad',
            'in': 'ABC',
            'ch': '0',
            'len': 5,
            'exp': 'ABC00'
        },
        {
            'func': 'rpad',
            'in': 'ABCDE',
            'ch': '0',
            'len': 5,
            'exp': 'ABCDE'
        },
        {
            'func': 'rpad',
            'in': 'ABCDEF',
            'ch': '0',
            'len': 5,
            'exp': 'ABCDEF'
        },
        {
            'func': 'rpad',
            'in': 1,
            'ch': '0',
            'len': 5,
            'exp': '10000'
        },

        # adj=True truncates a value longer than the specified length.
        {
            'func': 'rpad',
            'in': 'ABCDEF',
            'ch': '0',
            'len': 5,
            'adj': True,
            'exp': 'ABCDE'
        },

        # adj=True has no effect when the length already matches.
        {
            'func': 'rpad',
            'in': 'ABCDE',
            'ch': '0',
            'len': 5,
            'adj': True,
            'exp': 'ABCDE'
        },

        # adj=True still pads a value shorter than the specified length.
        {
            'func': 'rpad',
            'in': 'ABC',
            'ch': '0',
            'len': 5,
            'adj': True,
            'exp': 'ABC00'
        }
    ]

    for data in tests:
        test(data)

main()
