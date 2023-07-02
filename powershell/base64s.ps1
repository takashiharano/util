# Base64S
# Base64S (with "S" standing for "Secure") is a derivative encoding scheme of Base64.
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
#   $s = Get-Base64SEncodedString "<STRING>" <0-255>
#
#  Byte[]:
#   [byte[]]$b = Get-Content "C:\test\file.bin" -Encoding Byte
#   $s = Get-Base64SEncodedString $b <0-255>
#
# [Decode]
#  String:
#   $s = Get-Base64SDecodedString "<BASE64_STRING>" <0-255>
#
#  Byte[]:
#   $b = Get-Base64SDecodedBytes "<BASE64_STRING>" <0-255>
#   Set-Content "C:\tmp\file.bin" -Value $b -Encoding Byte
#------------------------------------------------------------------------------

#------------------------------------------------------------------------------
# Byte array or plain text to Base64 encoded string with XORing source and key
#------------------------------------------------------------------------------
function Get-Base64SEncodedString {
    Param (
        $Src,
        [int]$Key
    )

    if ($Src.GetType().Name -eq "String") {
        $b = [System.Text.Encoding]::UTF8.GetBytes($Src)
    } else {
        $b = $Src
    }

    $buf = Get-XoredBytes $b $Key
    $encoded = [System.Convert]::ToBase64String($buf)

    return $encoded
}

#------------------------------------------------------------------------------
# Base64 encoded string to Byte array with XORing source and key
#------------------------------------------------------------------------------
function Get-Base64SDecodedBytes {
    Param (
        $Src,
        [int]$Key
    )

    $buf = [System.Convert]::FromBase64String($Src)
    $arr = Get-XoredBytes $buf $Key
    return $arr
}

#------------------------------------------------------------------------------
# Base64 encoded string to Plain text with XORing source and key
#------------------------------------------------------------------------------
function Get-Base64SDecodedString {
    Param (
        $Src,
        [int]$Key
    )

    $buf = Get-Base64SDecodedBytes $Src $Key
    $str = [System.Text.Encoding]::UTF8.GetString($buf)
    return $str
}

#------------------------------------------------------------------------------
function Get-XoredBytes {
    Param (
        [byte[]]$Src,
        [int]$Key
    )

    $n = $Key % 256
    $buf = New-Object byte[] $Src.Length

    for ($i=0; $i -lt $Src.Length; $i++) {
        $b = Get-Xor $Src[$i] $n
        $buf[$i] = $b
    }

    return $buf
}

function Get-Xor {
    Param (
        [byte]$V,
        [int]$N
    )

    $i = [int]$V -band 255
    $b = [byte]($i -bxor $N)

    return $b
}
