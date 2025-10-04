import os
import sys

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

def test_http_request(url):
    return util.http(url)

def test_http_request1(url):
    method = 'GET'
    params = {'key1': 'aaa', 'key2': 'bbb', 'key3': ['a1', 'a2', 'a3']}
    #params = None
    headers = {
        'User-Agent': 'Mozilla/5.0 (x128) TestAgent/1.0'
    }
    return util.http(url, method, params, None, 'user1', '1111', headers)

def test_http_request2(url):
    method = 'GET'
    params = {'key1': 'aaa', 'key2': 'bbb', 'key3': ['a1', 'a2', 'a3']}
    #params = None
    headers = {
        'User-Agent': 'Mozilla/5.0 (x128) TestAgent/1.0'
    }
    res =  util.http_request(url, method, params, None, 'user1', '1111', headers)
    text = str(res.getcode()) + '\n'
    text += res.read().decode('utf-8')
    return text

def test_get_proxies():
    return util.get_proxies()

def test():
    #url = 'http://localhost/takashiharano.com/public/404/'
    #url = 'http://localhost/takashiharano.com/public/test/sleep.cgi?t=5'
    url = 'http://localhost/takashiharano.com/public/test/'
    ret = url + '\n'

    ret += 'test_http_request() = \n' + util.convert_newline(test_http_request(url), '\n') + '\n'
    ret += 'test_http_request1() = \n' + util.convert_newline(test_http_request1(url), '\n') + '\n'
    ret += 'test_http_request2() = \n' + util.convert_newline(test_http_request2(url), '\n') + '\n'

    ret += 'test_get_proxies() = ' + str(test_get_proxies()) + '\n'
    ret += '\n'

    return ret

def main():
    ret = test()
    print(ret)

main()
