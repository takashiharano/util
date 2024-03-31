############################################################################
# SFTP File Cleaner
# Copyright 2022 Takashi Harano
# Released under the MIT license
# https://libutil.com/
############################################################################
# about_Execution_Policies (https://go.microsoft.com/fwlink/?LinkID=135170)
# > Set-ExecutionPolicy Unrestricted -Scope CurrentUser

$HOSTNAME = "localhost"
$USER = "user1"
#$PVTKEY = "~/.ssh/id_rsa"
$PORT = 22
$TARGET_DIR = "/home/user1/data"
$RETENTION_SEC = 86400

$FILENAME_DATE_FORMAT = "([0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}\.[0-9]{2}\.[0-9]{2})"

$NOW_DATE = Get-Date
$NOW = ([datetimeoffset]$NOW_DATE).ToUnixTimeSeconds()
$EXPIRES_AT = ${NOW} - ${RETENTION_SEC}

#---------------------------------------------------------------------------
<#
.SYNOPSIS
   Converts to unixtime
.DESCRIPTION
   Date-Time string to unixtime
.EXAMPLE
   ConvertTo-Unixtime "2022-11-15_12.34.56"
.INPUTS
   Date-Time string
.OUTPUTS
   Unixtime
#>
#---------------------------------------------------------------------------
function ConvertTo-Unixtime {
    Param (
        # 2022-11-15_12.34.56
        [string]$DatePart
    )

    # 20221115123456
    $datetime = ${DatePart} -replace "[:_/T\.\-]", ""

    # to Unixtime like 1668483296
    $tz = Get-TimeZone
    $tmpstamp = [datetime]::ParseExact($datetime, "yyyyMMddHHmmss", $null);
    $utcTime = [TimeZoneInfo]::ConvertTimeToUtc($tmpstamp, $tz);
    $ret = $utcTime | Get-Date -UFormat "%s";

    return $ret
}

#---------------------------------------------------------------------------
function Invoke-SFTP-Command {
    Param (
        [string]$Command
    )
    $cmd = "sftp.exe"
    $args = @()
    if ((${PVTKEY} -ne $null) -And (${PVTKEY} -ne "")) {
        $args += "-i"
        $args += ${PVTKEY}
    }
    $args += "-oPort=${PORT}"
    $args += "${USER}@${HOSTNAME}"
    echo ${Command} |  & $cmd $args
}

#---------------------------------------------------------------------------
function Invoke-SFTP-Remove {
    Param (
        [array]$Files
    )
    $cmd = "cd " + $TARGET_DIR + "`n"
    foreach ($file in $Files) {
        $cmd += "rm " + $file + "`n"
    }
    Invoke-SFTP-Command $cmd
}

#---------------------------------------------------------------------------
$cmd = "cd " + $TARGET_DIR + "`n"
$cmd += "ls -1"
$ret = Invoke-SFTP-Command $cmd

$textlist = ${ret} -split "`r`n"
$targetFiles = @()
foreach ($filename in $textlist) {
    $matched = $filename -match $FILENAME_DATE_FORMAT
    if ($matched) {
        $datePart = $Matches[1]
        $unixtime = ConvertTo-Unixtime $datePart

        if ($unixtime -lt $EXPIRES_AT) {
            $targetFiles += ${filename}
        }
    }
}

Invoke-SFTP-Remove $targetFiles
