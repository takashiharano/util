import os
import sys
import datetime

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util


def test(data):
    arr = ['0300', '0900', '1200', '1800']

    moment = data['moment']
    offset = data.get('offset', 1)
    tz = data.get('tz')
    exp = data['exp']

    data_out = util.next_datetime(
        arr,
        offset=offset,
        moment=moment,
        tz=tz
    )

    if data_out == exp:
        ret = 'OK'
    else:
        ret = 'NG'

    print(
        '[' + ret + '] '
        + data['name']
        + ' IN=' + str(moment)
        + ' OFFSET=' + str(offset)
        + ' EXP=' + str(exp)
        + ' OUT=' + str(data_out)
    )


def main():
    utc = datetime.timezone.utc
    jst = datetime.timezone(datetime.timedelta(hours=9))

    tests = [
        # ------------------------------------------------------------
        # Basic
        # ------------------------------------------------------------
        {
            'name': 'before first',
            'moment': datetime.datetime(2026, 9, 1, 0, 0, 0),
            'exp': datetime.datetime(2026, 9, 1, 3, 0, 0)
        },
        {
            'name': 'exact first',
            'moment': datetime.datetime(2026, 9, 1, 3, 0, 0),
            'exp': datetime.datetime(2026, 9, 1, 3, 0, 0)
        },
        {
            'name': 'after first',
            'moment': datetime.datetime(2026, 9, 1, 3, 0, 0, 1),
            'exp': datetime.datetime(2026, 9, 1, 9, 0, 0)
        },
        {
            'name': 'exact second',
            'moment': datetime.datetime(2026, 9, 1, 9, 0, 0),
            'exp': datetime.datetime(2026, 9, 1, 9, 0, 0)
        },
        {
            'name': 'between second and third',
            'moment': datetime.datetime(2026, 9, 1, 10, 0, 0),
            'exp': datetime.datetime(2026, 9, 1, 12, 0, 0)
        },
        {
            'name': 'between third and fourth',
            'moment': datetime.datetime(2026, 9, 1, 15, 0, 0),
            'exp': datetime.datetime(2026, 9, 1, 18, 0, 0)
        },
        {
            'name': 'after last',
            'moment': datetime.datetime(2026, 9, 1, 19, 0, 0),
            'exp': datetime.datetime(2026, 9, 2, 3, 0, 0)
        },

        # ------------------------------------------------------------
        # String input
        # ------------------------------------------------------------
        {
            'name': 'string input',
            'moment': '2026-09-01 10:00:00.000000',
            'exp': datetime.datetime(2026, 9, 1, 12, 0, 0)
        },

        # ------------------------------------------------------------
        # Timestamp input
        # ------------------------------------------------------------
        {
            'name': 'timestamp float',
            'moment': datetime.datetime(
                2026, 9, 1, 10, 0, 0
            ).timestamp(),
            'exp': datetime.datetime(2026, 9, 1, 12, 0, 0)
        },
        {
            'name': 'timestamp int',
            'moment': int(datetime.datetime(
                2026, 9, 1, 10, 0, 0
            ).timestamp()),
            'exp': datetime.datetime(2026, 9, 1, 12, 0, 0)
        },

        # ------------------------------------------------------------
        # Negative offset
        # ------------------------------------------------------------
        {
            'name': 'offset -1 before first',
            'moment': datetime.datetime(2026, 9, 1, 0, 0, 0),
            'offset': -1,
            'exp': datetime.datetime(2026, 8, 31, 18, 0, 0)
        },
        {
            'name': 'offset -1 after first',
            'moment': datetime.datetime(2026, 9, 1, 4, 0, 0),
            'offset': -1,
            'exp': datetime.datetime(2026, 9, 1, 3, 0, 0)
        },
        {
            'name': 'offset -1 after last',
            'moment': datetime.datetime(2026, 9, 1, 19, 0, 0),
            'offset': -1,
            'exp': datetime.datetime(2026, 9, 1, 18, 0, 0)
        },
        {
            'name': 'offset -2 before first',
            'moment': datetime.datetime(2026, 9, 1, 0, 0, 0),
            'offset': -2,
            'exp': datetime.datetime(2026, 8, 31, 12, 0, 0)
        },
        {
            'name': 'offset -2 after first',
            'moment': datetime.datetime(2026, 9, 1, 4, 0, 0),
            'offset': -2,
            'exp': datetime.datetime(2026, 8, 31, 18, 0, 0)
        },

        # ------------------------------------------------------------
        # Positive offset
        # ------------------------------------------------------------
        {
            'name': 'offset +2 before first',
            'moment': datetime.datetime(2026, 9, 1, 0, 0, 0),
            'offset': 2,
            'exp': datetime.datetime(2026, 9, 1, 9, 0, 0)
        },
        {
            'name': 'offset +2 after first',
            'moment': datetime.datetime(2026, 9, 1, 4, 0, 0),
            'offset': 2,
            'exp': datetime.datetime(2026, 9, 1, 12, 0, 0)
        },
        {
            'name': 'offset +2 after last',
            'moment': datetime.datetime(2026, 9, 1, 19, 0, 0),
            'offset': 2,
            'exp': datetime.datetime(2026, 9, 2, 9, 0, 0)
        },

        # ------------------------------------------------------------
        # Offset across multiple schedule cycles
        # ------------------------------------------------------------
        {
            'name': 'offset +5',
            'moment': datetime.datetime(2026, 9, 1, 0, 0, 0),
            'offset': 5,
            'exp': datetime.datetime(2026, 9, 2, 3, 0, 0)
        },
        {
            'name': 'offset -5',
            'moment': datetime.datetime(2026, 9, 1, 0, 0, 0),
            'offset': -5,
            'exp': datetime.datetime(2026, 8, 30, 18, 0, 0)
        },

        # ------------------------------------------------------------
        # Timezone
        # ------------------------------------------------------------

        # Treat the naive datetime as a JST local time.
        {
            'name': 'naive datetime + JST',
            'moment': datetime.datetime(2026, 9, 1, 10, 0, 0),
            'tz': jst,
            'exp': datetime.datetime(
                2026, 9, 1, 12, 0, 0,
                tzinfo=jst
            )
        },

        # Use an aware datetime in UTC.
        {
            'name': 'UTC datetime',
            'moment': datetime.datetime(
                2026, 9, 1, 10, 0, 0,
                tzinfo=utc
            ),
            'tz': utc,
            'exp': datetime.datetime(
                2026, 9, 1, 12, 0, 0,
                tzinfo=utc
            )
        },

        # Convert an aware UTC datetime to JST.
        # 2026-09-01 00:30 UTC
        # = 2026-09-01 09:30 JST
        # The next candidate is 12:00 JST.
        {
            'name': 'UTC datetime -> JST',
            'moment': datetime.datetime(
                2026, 9, 1, 0, 30, 0,
                tzinfo=utc
            ),
            'tz': jst,
            'exp': datetime.datetime(
                2026, 9, 1, 12, 0, 0,
                tzinfo=jst
            )
        },

        # Treat a naive datetime string as UTC.
        {
            'name': 'string + UTC',
            'moment': '2026-09-01 10:00:00.000000',
            'tz': utc,
            'exp': datetime.datetime(
                2026, 9, 1, 12, 0, 0,
                tzinfo=utc
            )
        },

        # Evaluate a UTC timestamp in JST.
        # 2026-09-01 00:30 UTC
        # = 2026-09-01 09:30 JST
        {
            'name': 'timestamp + JST',
            'moment': datetime.datetime(
                2026, 9, 1, 0, 30, 0,
                tzinfo=utc
            ).timestamp(),
            'tz': jst,
            'exp': datetime.datetime(
                2026, 9, 1, 12, 0, 0,
                tzinfo=jst
            )
        },

        # Verify date rollover caused by timezone conversion.
        # 2026-09-01 18:30 UTC
        # = 2026-09-02 03:30 JST
        # The next candidate is 2026-09-02 09:00 JST.
        {
            'name': 'timezone date rollover',
            'moment': datetime.datetime(
                2026, 9, 1, 18, 30, 0,
                tzinfo=utc
            ),
            'tz': jst,
            'exp': datetime.datetime(
                2026, 9, 2, 9, 0, 0,
                tzinfo=jst
            )
        }
    ]

    for data in tests:
        test(data)


main()
