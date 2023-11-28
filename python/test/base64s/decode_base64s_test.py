import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test(exp, k, b64):
    s = util.decode_base64s(b64, k)
    st = 'OK' if s == exp else 'NG'
    print('[' + st + '] exp=' + str(exp) + ' got=' + str(s))

def main():
    test(None, None, None)
    test('', None, '')
    test('abc', None, 'YWJj')
    test('', '', '')
    test('', 'x', '')
    test('abc', '', 'YWJj')
    test('abc', 'x', 'GRobAA==')
    test('abc', 'xyz', 'GRsZAA==')
    test('abc', 'xyz1', 'GRsZzgE=')
    test('a', 'A2345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234#', 'IM3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvc/g==')
    test('あいう', '', '44GC44GE44GG')
    test('あいう', 'x', 'm/n6m/n8m/n+AA==')
    test('あいう', 'xyz', 'm/j4m/j+m/j8AA==')
    test('あいう', 'xyz123456a', 'm/j40rO317SwngE=')

main()
