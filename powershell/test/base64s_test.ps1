. "..\base64s.ps1"

function Test-Encoding {
    Param (
        $Src,
        $Key
    )
    $s = Get-Base64sEncodedString $Src $Key
    Write-Host $s
}

function Test-Decoding {
    Param (
        $B64,
        $Key
    )
    $s = Get-Base64sDecodedString $B64 $Key
    Write-Host $s
}

Write-Host "-------------------------------"
Write-Host "Encode"
Write-Host "-------------------------------"
Test-Encoding "abc" ""
Test-Encoding "abc" "x"
Test-Encoding "abc" "xyz"
Test-Encoding "abc" "xyz1"
Test-Encoding "a" "A2345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234#"
Test-Encoding "‚ ‚¢‚¤" "x"
Test-Encoding "‚ ‚¢‚¤" "xyz"
Test-Encoding "‚ ‚¢‚¤" "xyz123456a"

Write-Host "-------------------------------"
Write-Host "Decode"
Write-Host "-------------------------------"
Test-Decoding "YWJj" ""
Test-Decoding "ABkaGw==" "x"
Test-Decoding "ABkbGQ==" "xyz"
Test-Decoding "ARkbGc4=" "xyz1"
Test-Decoding "/iDNzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczLysnIx8bPzs3My8rJyMfGz87NzMvKycjHxs/OzczL3A==" "A2345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234#"
Test-Decoding "AJv5+pv5/Jv5/g==" "x"
Test-Decoding "AJv4+Jv4/pv4/A==" "xyz"
Test-Decoding "AZv4+NKzt9e0sJ4=" "xyz123456a"

Write-Host "w/ Wrong key:"
Test-Decoding "ABkbGQ==" "123"

Write-Host "-------------------------------"
[byte[]]$b = Get-Content "C:\test\img.jpg" -Encoding Byte
$s = Get-Base64sEncodedString $b "xyz"
Write-Host $s

$b = Get-Base64SDecodedBytes $s "xyz"
Set-Content "C:\tmp\img.jpg" -Value $b -Encoding Byte
