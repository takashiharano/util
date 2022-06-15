import os
import sys
import datetime

sys.path.append(os.path.join(os.path.dirname(__file__), '../..'))
import util

# Test
def test(target_pattern, chars='abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ !"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~', max_digit=8):
    last_index = util.strp_total(chars, max_digit)

    print('chars : ' + chars)
    print('target: ' + target_pattern)
    print()
    start = datetime.datetime.today().timestamp()
    print('start: ' + datetime.datetime.fromtimestamp(start).strftime('%Y-%m-%d %H:%M:%S.%f') + '\n')
    elapsed = 0
    a = None
    found = False
    for i in range(1, last_index - 1):
        r = util._strp(chars, i, a)

        s = r['s']
        if s == target_pattern:
            found = True

        if i % 10000 == 0 or found:
            elapsed = datetime.datetime.today().timestamp() - start
            print(str(i) + ': ' + s + ' T+' + str(elapsed) + 's')

        if found:
            break

        a = r['a']

    end = datetime.datetime.today().timestamp()
    elapsed = end - start
    print()
    if found:
        print('Found! ' + str(i) + ': ' + s)
    else:
        print('Not Found')
    print('end  : ' + datetime.datetime.fromtimestamp(end).strftime('%Y-%m-%d %H:%M:%S.%f') + '\n')
    speed = int(i / elapsed)
    print(str(elapsed) + 's ' + str(speed) + '/s')

# start: 2019-03-24 00:05:18.377915
#
# 1000: all 0.21622419357299805s
# 2000: bxx 0.7802000045776367s
# 3000: dkj 1.726391077041626s
# 4000: ewv 3.0859081745147705s
# 5000: gjh 4.909600019454956s
# 6000: hvt 7.0636069774627686s
# 7000: jif 9.597015142440796s
# 8000: kur 12.56584620475769s
# 9000: mhd 15.90491509437561s
# 10000: ntp 19.717950105667114s
# 11000: pgb 23.827152013778687s
# 12000: qsn 28.37999415397644s
# 13000: sez 33.317586183547974s
# 14000: trl 38.70374608039856s
# 15000: vdx 44.462669134140015s
# 16000: wqj 50.61592221260071s
# 17000: ycv 57.196293115615845s
# 18000: zph 64.17893815040588s
#
# end  : 2019-03-24 00:06:24.589708
#
# Found! 18279: aaaa
# 66.2117931842804s 276/s

# >python bf.py
# chars : abcdefghijklmnopqrstuvwxyz
# target: ffff
#
# start: 2021-05-15 22:01:16.059521
#
# 10000: ntp T+15.69790005683899s
# 20000: acof T+59.70252704620361s
# 30000: ariv T+136.97435903549194s
# 40000: bgdl T+264.2412819862366s
# 50000: buyb T+424.5510821342468s
# 60000: cjsr T+624.8411791324615s
# 70000: cynh T+866.8162751197815s
# 80000: dnhx T+1153.7093379497528s
# 90000: eccn T+1462.2957999706268s
# 100000: eqxd T+1835.9667949676514s
# 109674: ffff T+2206.5861020088196s (36m 46s 586)
#
# Found! 109674: ffff
# end  : 2021-05-15 22:38:02.645623
#
# 2206.5861020088196s 49/s

#------------------------------------
# Python 3.10.5
# start: 2022-06-15 22:25:19.853071
# 
# 1000: all T+0.12021803855895996s
# 2000: bxx T+0.4987490177154541s
# 3000: dkj T+1.2030229568481445s
# 4000: ewv T+2.213871955871582s
# 5000: gjh T+3.447016954421997s
# 6000: hvt T+4.923882961273193s
# 7000: jif T+6.695007085800171s
# 8000: kur T+8.735297918319702s
# 9000: mhd T+11.058709144592285s
# 10000: ntp T+13.658288955688477s

#------------------------------------
# start: 2022-06-15 22:38:30.745769
#
# 10000: ntp T+0.018663883209228516s
# 20000: acof T+0.03361988067626953s
# 30000: ariv T+0.05160093307495117s
# 40000: bgdl T+0.06955289840698242s
# 50000: buyb T+0.08562088012695312s
# 60000: cjsr T+0.10362505912780762s
# 70000: cynh T+0.11955094337463379s
# 80000: dnhx T+0.1365358829498291s
# 90000: eccn T+0.15429306030273438s
# 100000: eqxd T+0.17121601104736328s
# 109674: ffff T+0.18717288970947266s
#
# Found! 109674: ffff
# end  : 2022-06-15 22:38:30.932942
#
# 0.18717288970947266s 585950/s
def main():
    test('ffff', chars='abcdefghijklmnopqrstuvwxyz', max_digit=8)

main()
