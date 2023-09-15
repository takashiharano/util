import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test(s, k, exp):
    b = util.encode_base64s(s, k)
    st = 'OK' if b == exp else 'NG'
    print('[' + st + '] exp=' + exp + ' got=' + b)

def main():
    test('', '', '')
    test('', 'x', '')
    test('abc', '', 'YWJj')
    test('abc', 'x', 'ABkaGw==')
    test('abc', 'xyz', 'ABkbGQ==')
    test('abc', 'xyz1', 'ARkbGc4=')
    test('a', 'A2345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234#', '/iDNzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczL3A==')
    test('あいう', '', '44GC44GE44GG')
    test('あいう', 'x', 'AJv5+pv5/Jv5/g==')
    test('あいう', 'xyz', 'AJv4+Jv4/pv4/A==')
    test('あいう', 'xyz123456a', 'AZv4+NKzt9e0sJ4=')

main()
