import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_hash(algorithm, expected):
    s = ''
    s += algorithm + ':\n'
    s += expected + '\n'
    s += util.hash('', algorithm) + '\n'
    s += '\n'
    return s

def main():
    s = ''
    s += test_hash('MD5', 'd41d8cd98f00b204e9800998ecf8427e')
    s += test_hash('SHA-1', 'da39a3ee5e6b4b0d3255bfef95601890afd80709')
    s += test_hash('SHA-224', 'd14a028c2a3a2bc9476102bb288234c415a2b01f828ea62ac5b3e42f')
    s += test_hash('SHA-256', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855')
    s += test_hash('SHA-384', '38b060a751ac96384cd9327eb1b1e36a21fdb71114be07434c0cc7bf63f6e1da274edebfe76f65fbd51ad2f14898b95b')
    s += test_hash('SHA-512', 'cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e')

    s += test_hash('SHA3-224', '6b4e03423667dbb73b6e15454f0eb1abd4597f9a1b078e3f5b5a6bc7')
    s += test_hash('SHA3-256', 'a7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a')
    s += test_hash('SHA3-384', '0c63a75b845e4f7d01107d852e4c2485c51a50aaaa94fc61995e71bbee983a2ac3713831264adb47fb6bd1e058d5f004')
    s += test_hash('SHA3-512', 'a69f73cca23a9ac5c8b567dc185a756e97c982164fe25859e0d1dcc1475c80a615b2123af1f5f94c11e3e9402c3ac558f500199d95b6d3e301758586281dcd26')

    s += test_hash('INVALID_ALGORITHM', '')

    print(s)

main()
