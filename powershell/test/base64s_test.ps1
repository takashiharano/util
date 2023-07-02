. "..\base64s.ps1"

$s = Get-Base64SEncodedString "abc" 0
Write-Host $s

$s = Get-Base64SEncodedString "abc" 1
Write-Host $s

$s = Get-Base64SEncodedString "abc" 2
Write-Host $s

$s = Get-Base64SEncodedString "abc" 254
Write-Host $s

$s = Get-Base64SEncodedString "abc" 255
Write-Host $s

$s = Get-Base64SEncodedString "abc" 256
Write-Host $s

$s = Get-Base64SEncodedString "abc" 257
Write-Host $s

$s = Get-Base64SEncodedString "abc" 510
Write-Host $s

$s = Get-Base64SEncodedString "abc" 511
Write-Host $s

$s = Get-Base64SEncodedString "abc" 512
Write-Host $s

$s = Get-Base64SEncodedString "abc" 513
Write-Host $s

Write-Host "-------------------------------"
$s = Get-Base64SDecodedString "YWJj" 0
Write-Host $s

$s = Get-Base64SDecodedString "YGNi" 1
Write-Host $s

$s = Get-Base64SDecodedString "Y2Bh" 2
Write-Host $s

$s = Get-Base64SDecodedString "n5yd" 254
Write-Host $s

$s = Get-Base64SDecodedString "np2c" 255
Write-Host $s

$s = Get-Base64SDecodedString "YWJj" 256
Write-Host $s

$s = Get-Base64SDecodedString "YGNi" 257
Write-Host $s

$s = Get-Base64SDecodedString "n5yd" 510
Write-Host $s

$s = Get-Base64SDecodedString "np2c" 511
Write-Host $s

$s = Get-Base64SDecodedString "YWJj" 512
Write-Host $s

$s = Get-Base64SDecodedString "YGNi" 513
Write-Host $s

Write-Host "-------------------------------"
$s = Get-Base64SEncodedString "‚ ‚¢‚¤" 1
Write-Host $s

Write-Host "-------------------------------"
$s = Get-Base64SDecodedString "4oCD4oCF4oCH" 1
Write-Host $s

Write-Host "-------------------------------"
[byte[]]$b = Get-Content "C:\test\img.jpg" -Encoding Byte
$s = Get-Base64SEncodedString $b 1
Write-Host $s

$b = Get-Base64SDecodedBytes $s 1
Set-Content "C:\tmp\img.jpg" -Value $b -Encoding Byte
