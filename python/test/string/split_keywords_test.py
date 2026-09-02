import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test(data):
    data_in = data['in']
    limit = data.get('limit', 0)
    exp = data['exp']

    out = util.split_keywords(data_in, limit)

    if out == exp:
        ret = 'OK'
    else:
        ret = 'NG'

    print(
        '[' + ret + '] '
        + 'IN=' + repr(data_in)
        + ' LIMIT=' + str(limit)
        + ' EXP=' + repr(exp)
        + ' OUT=' + repr(out)
    )

def main():
    tests = [
        # ------------------------------------------------------------
        # Double quotes
        # ------------------------------------------------------------
        {
            'in': 'aaa',
            'exp': ['aaa']
        },
        {
            'in': 'aaa bbb',
            'exp': ['aaa', 'bbb']
        },
        {
            'in': 'aaa bbb ccc',
            'exp': ['aaa', 'bbb', 'ccc']
        },
        {
            'in': '\"aaa\"',
            'exp': ['aaa']
        },
        {
            'in': '"aaa bbb"',
            'exp': ['aaa bbb']
        },
        {
            'in': 'aaa "bbb ccc"',
            'exp': ['aaa', 'bbb ccc']
        },
        {
            'in': 'aaa "bbb ccc" ddd',
            'exp': ['aaa', 'bbb ccc', 'ddd']
        },
        {
            'in': '"aaa\"bbb\""',
            'exp': ['aaa"bbb"']
        },
        {
            'in': '"aaa\"bbb\"" ccc',
            'exp': ['aaa"bbb"', 'ccc']
        },
        {
            'in': 'aaa:"bbb ccc"',
            'exp': ['aaa:"bbb ccc"']
        },
        {
            'in': 'aaa bbb:"ccc ddd"',
            'exp': ['aaa', 'bbb:"ccc ddd"']
        },
        {
            'in': 'aaa bbb:"ccc ddd" eee',
            'exp': ['aaa', 'bbb:"ccc ddd"', 'eee']
        },

        # ------------------------------------------------------------
        # Single quotes
        # ------------------------------------------------------------
        {
            'in': '\'aaa\'',
            'exp': ['aaa']
        },
        {
            'in': '\'aaa bbb\'',
            'exp': ['aaa bbb']
        },
        {
            'in': 'aaa \'bbb ccc\'',
            'exp': ['aaa', 'bbb ccc']
        },
        {
            'in': 'aaa \'bbb ccc\' ddd',
            'exp': ['aaa', 'bbb ccc', 'ddd']
        },
        {
            'in': '\'aaa\'bbb\'\'',
            'exp': ['aaa\'bbb\'']
        },
        {
            'in': '\'aaa\'bbb\'\' ccc',
            'exp': ['aaa\'bbb\'', 'ccc']
        },
        {
            'in': 'aaa:\'bbb ccc\'',
            'exp': ['aaa:\'bbb ccc\'']
        },
        {
            'in': 'aaa bbb:\'ccc ddd\'',
            'exp': ['aaa', 'bbb:\'ccc ddd\'']
        },
        {
            'in': 'aaa bbb:\'ccc ddd\' eee',
            'exp': ['aaa', 'bbb:\'ccc ddd\'', 'eee']
        },

        # ------------------------------------------------------------
        # Parentheses
        # ------------------------------------------------------------
        {
            'in': '(aaa)',
            'exp': ['(aaa)']
        },
        {
            'in': '(aaa bbb)',
            'exp': ['(aaa bbb)']
        },
        {
            'in': 'aaa (bbb ccc)',
            'exp': ['aaa', '(bbb ccc)']
        },
        {
            'in': '(aaa bbb) ccc',
            'exp': ['(aaa bbb)', 'ccc']
        },
        {
            'in': 'aaa (bb(b) ccc)',
            'exp': ['aaa', '(bb(b) ccc)']
        },

        # ------------------------------------------------------------
        # Limit
        # ------------------------------------------------------------

        # No limit.
        {
            'in': 'a b c d',
            'limit': 0,
            'exp': ['a', 'b', 'c', 'd']
        },

        # Limit 1 keeps the whole string.
        {
            'in': 'a b c d',
            'limit': 1,
            'exp': ['a b c d']
        },

        # Limit 2 keeps the remaining text as the second value.
        {
            'in': 'a b c d',
            'limit': 2,
            'exp': ['a', 'b c d']
        },

        # Limit 3 keeps the remaining text as the third value.
        {
            'in': 'a b c d',
            'limit': 3,
            'exp': ['a', 'b', 'c d']
        },

        # A limit equal to the number of values has no visible effect.
        {
            'in': 'a b c d',
            'limit': 4,
            'exp': ['a', 'b', 'c', 'd']
        },

        # A larger limit has no effect.
        {
            'in': 'a b c d',
            'limit': 10,
            'exp': ['a', 'b', 'c', 'd']
        },

        # Verify that parsing stops after the limit is reached.
        {
            'in': 'aaa bbb ccc ddd',
            'limit': 2,
            'exp': ['aaa', 'bbb ccc ddd']
        },

        # Keep quoted text in the remaining value.
        {
            'in': 'aaa "bbb ccc" ddd eee',
            'limit': 2,
            'exp': ['aaa', '"bbb ccc" ddd eee']
        },

        # Extract a quoted value before the limit is reached.
        {
            'in': '"aaa bbb" ccc ddd',
            'limit': 2,
            'exp': ['aaa bbb', 'ccc ddd']
        },

        # Parenthesized text counts as one value.
        {
            'in': 'aaa (bbb ccc) ddd eee',
            'limit': 3,
            'exp': ['aaa', '(bbb ccc)', 'ddd eee']
        },

        # Leading and trailing spaces are ignored.
        {
            'in': '  a b c d  ',
            'limit': 2,
            'exp': ['a', 'b c d']
        },

        # Empty input keeps the existing behavior.
        {
            'in': '',
            'exp': ['']
        },

        # Empty input with limit 1 keeps the existing behavior.
        {
            'in': '',
            'limit': 1,
            'exp': ['']
        }
    ]

    for data in tests:
        test(data)

main()