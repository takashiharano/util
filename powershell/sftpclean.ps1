############################################################################
# SFTP File Cleaner
# Copyright 2022 Takashi Harano
# Released under the MIT License
# https://libutil.com/
#
# Created: 2022-11-15
# Updated: 2025-08-03
#
# Usage:
#  .\sftpclean.ps1
############################################################################
# about_Execution_Policies (https://go.microsoft.com/fwlink/?LinkID=135170)
# > Set-ExecutionPolicy Unrestricted -Scope CurrentUser

$RemoteHost = "localhost"
$Port = 22
$SftpUser = "user1"
$PrivateKey = "$HOME\.ssh\id_ed25519"
$RemoteDir = "/tmp1/d1"
$RetentionSec = 86400
$TimeoutSec = 15

#--- Timestamp Pattern and Format ---

# e.g, 20250801-123456 -> 2025-08-01 12:34:56
$TimestampRegex = '([0-9]{8}-[0-9]{6})'
$DateCleanupScript = {
    param($s)
    return ($s -replace '([0-9]{4})([0-9]{2})([0-9]{2})-([0-9]{2})([0-9]{2})([0-9]{2})', '$1-$2-$3 $4:$5:$6')
}
$DatetimeFormat = 'yyyy-MM-dd HH:mm:ss'

# e.g., 2025-08-01_12.34.56 -> 2025-08-01 12:34:56
# $TimestampRegex = '([0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}\.[0-9]{2}\.[0-9]{2})'
# $DateCleanupScript = {
#     param($s)
#     return ($s -replace '_', ' ' -replace '\.', ':')
# }

#---------------------------------------------------------------------------
<#
.SYNOPSIS
    Converts a timestamp string to Unix time (seconds since epoch).

.DESCRIPTION
    Accepts a timestamp string, applies cleanup formatting, and parses
    it as a datetime object. Converts to Unix time using local timezone context.

.PARAMETER rawDateTimeString
    The original timestamp string in raw form (e.g., "20240801-123456" or "2024-08-01_12.34.56").

.INPUTS
    System.String

.OUTPUTS
    System.Int64

.EXAMPLE
    PS> ConvertTo-Unixtime "2022-11-15_12.34.56"
    1668500096
#>
#---------------------------------------------------------------------------
function ConvertTo-Unixtime {
    param (
        [string]$rawDateTimeString
    )

    $dateTimeString = & $DateCleanupScript $rawDateTimeString

    try {
        $dt = [datetime]::ParseExact($dateTimeString, $DatetimeFormat, $null)
        $unixtime = ([datetimeoffset]$dt).ToUnixTimeSeconds()
    } catch {
        Write-Warning "Invalid datetime format: $rawDateTimeString -> $dateTimeString"
        $unixtime = -1
    }

    return $unixtime
}

#---------------------------------------------------------------------------
<#
.SYNOPSIS
    Executes a set of SFTP commands via sftp.exe.

.PARAMETER sftpScript
    A string containing one or more newline-delimited SFTP commands.

.EXAMPLE
    Invoke-SFTP-Command "cd /tmp`nls -1"
#>
function Invoke-SFTP-Command {
    param (
        [string]$sftpScript
    )

    $cmd = "sftp.exe"
    $args = @("-oPort=$Port")
    $args += "-oConnectTimeout=$TimeoutSec"
    if ($PrivateKey) {
        $args += @("-i", "$PrivateKey")
    }
    $args += "$SftpUser@$RemoteHost"

    $output = echo $sftpScript | & $cmd $args
    if (-not $output) {
        Write-Error "SFTP command failed or returned no output."
        exit 9
    }

    return $output
}

#---------------------------------------------------------------------------
<#
.SYNOPSIS
    Retrieves list of expired files from remote directory based on timestamp pattern.

.EXAMPLE
    $expired = Get-Expired-Files
#>
function Get-Expired-Files {
    param (
        [long]$expiresAt
    )

    $sftpScript = @"
cd $RemoteDir
ls -1
"@

    Write-Host "Connecting to $SftpUser@$RemoteHost"
    $output = Invoke-SFTP-Command $sftpScript
    $lines = $output -split "`r?`n" | Where-Object { $_ -ne "" }

    $expired = @()
    foreach ($line in $lines) {
        if ($line -match $TimestampRegex) {
            $ts = $Matches[1]
            $ut = ConvertTo-Unixtime $ts
            if ($ut -eq -1) { continue }
            if ($ut -lt $expiresAt) {
                $expired += $line
            }
        }
    }

    return $expired
}

#---------------------------------------------------------------------------
function Remove-Expired-Files {
    param (
        [array]$fileList
    )

    if (-not $fileList -or $fileList.Count -eq 0) {
        Write-Host "No files to be removed."
        return
    }

    Write-Host "Target files to be removed:"
    $fileList | ForEach-Object { Write-Host "  $_" }
    Write-Host "Total: $($fileList.Count) file(s)"

    $rm_script = "cd $RemoteDir`n"
    foreach ($filename in $fileList) {
        $rm_script += "rm $filename`n"
    }

    Invoke-SFTP-Command $rm_script
    Write-Host "Cleanup complete"
}

#---------------------------------------------------------------------------
function Main {
    if (-not (Test-Path $PrivateKey)) {
        Write-Error "Private key '$PrivateKey' does not exist."
        exit 1
    }

    $currentTimestamp = [datetimeoffset]::Now.ToUnixTimeSeconds()
    $expiresAt = $currentTimestamp - $RetentionSec

    $filesToDelete = Get-Expired-Files -expiresAt $expiresAt
    Remove-Expired-Files -fileList $filesToDelete
}

Main
