# Base64s
# Base64s (with "s" standing for "secure") is a derivative encoding scheme of Base64.
#
# The MIT License
#
# Copyright (c) 2023 Takashi Harano
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
#------------------------------------------------------------------------------
# Usage:
# . ".\base64s.ps1"
#
# [Encode]
#  String:
#   $s = Get-Base64sEncodedString "<STRING>" "<KEY>"
#
#  Byte[]:
#   [byte[]]$b = Get-Content "C:\test\file.bin" -Encoding Byte
#   $s = Get-Base64sEncodedString $b "<KEY>"
#
# [Decode]
#  String:
#   $s = Get-Base64sDecodedString "<BASE64_STRING>" "<KEY>"
#
#  Byte[]:
#   $b = Get-Base64sDecodedBytes "<BASE64_STRING>" "<KEY>"
#   Set-Content "C:\tmp\file.bin" -Value $b -Encoding Byte
#------------------------------------------------------------------------------

#------------------------------------------------------------------------------
# Byte array or plain text to Base64 encoded string with XORing source and key
#------------------------------------------------------------------------------
function Get-Base64sEncodedString {
    Param (
        $Src,
        $Key
    )

    if ($Src.GetType().Name -eq "String") {
        $b = [System.Text.Encoding]::UTF8.GetBytes($Src)
    } else {
        $b = $Src
    }

    $kb = [System.Text.Encoding]::UTF8.GetBytes($Key)

    $buf = Get-EncdedBytes $b $kb
    $encoded = [System.Convert]::ToBase64String($buf)

    return $encoded
}

#------------------------------------------------------------------------------
# Base64 encoded string to Byte array with XORing source and key
#------------------------------------------------------------------------------
function Get-Base64sDecodedBytes {
    Param (
        $Src,
        $Key
    )

    $buf = [System.Convert]::FromBase64String($Src)
    $kb = [System.Text.Encoding]::UTF8.GetBytes($Key)
    $arr = Get-DecdedBytes $buf $kb
    return $arr
}

#------------------------------------------------------------------------------
# Base64 encoded string to Plain text with XORing source and key
#------------------------------------------------------------------------------
function Get-Base64sDecodedString {
    Param (
        $Src,
        $Key
    )

    $buf = Get-Base64sDecodedBytes $Src $Key
    $str = [System.Text.Encoding]::UTF8.GetString($buf)
    return $str
}

#------------------------------------------------------------------------------
function Get-EncdedBytes {
    Param (
        [byte[]]$Src,
        [byte[]]$Key
    )

    if (($Src.Length -eq 0) -or ($Key.Length -eq 0)) {
        return $Src
    }

    $p = $Key.Length - $Src.Length
    if ($p -lt 0) {
        $p = 0
    }

    $buf = New-Object byte[] ($Src.Length + $p + 1)

    $buf[0] = $p
    for ($i=0; $i -lt $Src.Length; $i++) {
        $buf[$i + 1] = $Src[$i] -bxor $Key[$i % $Key.Length]
    }

    $j = $i
    for ($i=0; $i -lt $p; $i++) {
        $buf[$j + 1] = (255 -bxor $Key[$j % $Key.Length])
        $j++
    }

    return $buf
}

#------------------------------------------------------------------------------
function Get-DecdedBytes {
    Param (
        [byte[]]$Src,
        [byte[]]$Key
    )

    if (($Src.Length -eq 0) -or ($Key.Length -eq 0)) {
        return $Src
    }

    $p = $Src[0]
    $len = $Src.Length - $p
    $buf = New-Object byte[] ($len - 1)

    $j = 0
    for ($i=1; $i -lt $len; $i++) {
        $buf[$j] = $Src[$i] -bxor $Key[$j % $Key.Length]
        $j++;
    }

    return $buf
}
