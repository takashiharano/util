############################################################################
# Character Encoding Converter
# Copyright 2024 Takashi Harano
# https://libutil.com/
# License: MIT
############################################################################
# Usage:
#   convenc.ps1 IN_PATH IN_ENC_NAME OUT_PATH OUT_ENC_NAME
# e.g.,
#   .\convenc.ps1 c:\tmp\a.txt utf-8 c:\tmp\b.txt shift_jis
############################################################################
# about_Execution_Policies (https://go.microsoft.com/fwlink/?LinkID=135170)
# > Set-ExecutionPolicy Unrestricted -Scope CurrentUser

# [Encoding Name]
# CP    Name
# -----------------
#   932 shift_jis
# 50222 iso-2022-jp
# 51932 euc-jp
# 65001 utf-8
#  1200 utf-16
# 20290 IBM290
#---------------------------------------------------------------------------
function Convert-Encoding {
    <#
        .SYNOPSIS
           Converts character encoding
        .DESCRIPTION
           Read a text file from srcPath, converts the encoding, write to the outPath.
        .EXAMPLE
           Convert-Encoding "a.txt" "shift_jis" "a.txt" "utf-8"
        .OUTPUTS
           Converted text file
    #>
    Param (
        [string]$srcPath,
        [string]$srcEncodingName,
        [string]$outPath,
        [string]$outEncodingName
    )
    $srcEncoding = [Text.Encoding]::GetEncoding($srcEncodingName)
    $outEncoding = [Text.Encoding]::GetEncoding($outEncodingName)
    [Byte[]]$srcByteArray = Get-Content $srcPath -Encoding Byte
    $content = $srcEncoding.GetString($srcByteArray)
    Write-Output $content | ForEach-Object {$outEncoding.GetBytes($_)} | Set-Content -Path $outPath -Encoding Byte
}

#---------------------------------------------------------------------------
$ArgsLen = $args.Length
if ($ArgsLen -lt 4) {
    Write-Host "Usage: convenc.ps1 IN_PATH IN_ENC_NAME OUT_PATH OUT_ENC_NAME"
    exit 1
}

$SrcPath = $args[0]
$SrcEncodingName = $args[1]
$OutPath = $args[2]
$OutEncodingName = $args[3]

Write-Host "[IN ] Path = $SrcPath : Encoding = $SrcEncodingName"
Write-Host "[OUT] Path = $OutPath : Encoding = $OutEncodingName"
Write-Host "Converting..."
Convert-Encoding $SrcPath $SrcEncodingName $OutPath $OutEncodingName
Write-Host "Done."
