' Base64S
' Copyright 2023 Takashi Harano
' Released under the MIT license
' Base64S (with "S" standing for "Secure") is a derivative encoding scheme of Base64.

Option Explicit

''
' Plain text to Base64 encoded string with XORing source and key
'
Public Function EncodeString(str As String, key As Integer) As String
    Dim arr() As Byte
    Dim ret As String
    arr = StringToUtf8Bytes(str)
    ret = Encode(arr, key)
    EncodeString = ret
End Function

Public Function Encode(arr() As Byte, key As Integer) As String
    Dim i As Integer
    Dim arrLen As Integer
    Dim buf() As Byte
    arrLen = UBound(arr)
    ReDim buf(arrLen)
    Dim n As Integer
    n = key Mod 256
    For i = 0 To arrLen
        buf(i) = arr(i) Xor n
    Next
    Encode = EncodeBase64(buf)
End Function

''
' Base64 encoded string to Plain text with XORing source and key
'
Public Function DecodeString(str As String, key As Integer) As String
    Dim arr() As Byte
    Dim ret As String
    arr = Decode(str, key)
    ret = Utf8BytesToString(arr)
    DecodeString = ret
End Function

Public Function Decode(src As String, key As Integer) As Byte()
    Dim i As Integer
    Dim bufLen As Integer
    Dim buf() As Byte
    Dim arr() As Byte
    buf = DecodeBase64(src)
    bufLen = UBound(buf)
    ReDim arr(bufLen)
    Dim n As Integer
    n = key Mod 256
    For i = 0 To bufLen
        arr(i) = buf(i) Xor n
    Next
    Decode = arr
End Function

''
' Plain text to Base64 encoded string
'
Public Function EncodeBase64String(str As String) As String
    Dim arr() As Byte
    Dim ret As String
    arr = StringToUtf8Bytes(str)
    ret = EncodeBase64(arr)
    EncodeBase64String = ret
End Function

''
' Base64 encoded string to Plain text
'
Public Function DecodeBase64String(str As String) As String
    Dim arr() As Byte
    Dim ret As String
    arr = DecodeBase64(str)
    ret = Utf8BytesToString(arr)
    DecodeBase64String = ret
End Function

Public Function EncodeBase64(ByRef arr() As Byte) As String
    Dim arrLen As Integer
    Dim str As String
    Dim b0 As Integer
    Dim b1 As Integer
    Dim b2 As Integer
    Dim tbl(64) As Byte
    Dim i As Integer
    Dim codePoints(3) As Integer
    Dim idx As Integer

    tbl(64) = 61
    tbl(63) = 47
    tbl(62) = 43
    For i = 0 To 61
        If i < 26 Then
            tbl(i) = i + 65
        ElseIf i < 52 Then
            tbl(i) = i + 71
        Else
            tbl(i) = i - 4
       End If
    Next i

    str = ""
    arrLen = UBound(arr)
    For i = 0 To arrLen Step 3
        b0 = 0
        b1 = 0
        b2 = 0
        If i > arrLen Then
          Exit For
        End If
        b0 = arr(i) And 255
        If ((i + 1) <= arrLen) Then
            b1 = arr(i + 1) And 255
        End If
        If ((i + 2) <= arrLen) Then
            b2 = arr(i + 2) And 255
        End If

        codePoints(0) = tbl(b0 \ 2 ^ 2)
        codePoints(1) = tbl((b0 And 3) * 2 ^ 4 Or b1 \ 2 ^ 4)

        If ((i + 1) <= arrLen) Then
            idx = ((b1 And 15) * 2 ^ 2) Or (b2 \ 2 ^ 6)
        Else
            idx = 64
        End If
        codePoints(2) = tbl(idx)

        If ((i + 2) <= arrLen) Then
            idx = (b2 And 63)
        Else
            idx = 64
        End If
        codePoints(3) = tbl(idx)

        str = str & ChrW(codePoints(0)) & ChrW(codePoints(1)) & ChrW(codePoints(2)) & ChrW(codePoints(3))
    Next

    EncodeBase64 = str
End Function

Public Function DecodeBase64(ByRef str As String) As Byte()
    Dim arr() As Byte
    Dim i As Integer
    Dim j As Integer
    Dim c As Integer
    Dim idx As Integer
    Dim tbl(127) As Byte
    Dim buf(3) As Byte

    If Len(str) = 0 Then
        Exit Function
    End If

    For i = 1 To Len(str)
        c = Asc(Mid(str, i, 1))
        If Not (((c >= 48) And (c <= 57)) Or ((c >= 65) And (c <= 90)) Or ((c >= 97) And (c <= 122)) Or (c = 43) Or (c = 47) Or (c = 61)) Then
            MsgBox "Invalid char: " & c & " at " & i
        End If
    Next

    tbl(61) = 64
    tbl(47) = 63
    tbl(43) = 62
    For i = 0 To 61
        If i < 26 Then
            idx = i + 65
        ElseIf i < 52 Then
            idx = i + 71
        Else
            idx = i - 4
        End If
        tbl(idx) = i
    Next

    For i = 1 To Len(str) Step 4
        For j = 0 To 3
            If (i + j) > Len(str) Then
                Exit For
            End If
            buf(j) = tbl(Asc(Mid(str, i + j, 1)))
        Next
        Call ArrayPush(arr, ((buf(0) * 2 ^ 2) Or (buf(1) And 63) \ 2 ^ 4))
        Call ArrayPush(arr, (((buf(1) And 15) * 2 ^ 4) Or (buf(2) And 63) \ 2 ^ 2))
        Call ArrayPush(arr, (((buf(2) And 3) * 2 ^ 6) Or (buf(3) And 63)))
    Next

    If buf(3) = 64 Then
        Call ArrayPop(arr)
        If buf(2) = 64 Then
            Call ArrayPop(arr)
        End If
    End If

    DecodeBase64 = arr
End Function

Private Function ArrayPush(ByRef arr As Variant, val As Variant)
    On Error GoTo ArrInit
    ReDim Preserve arr(UBound(arr) + 1)
    arr(UBound(arr)) = val
    Exit Function
ArrInit:
    ReDim arr(0)
    arr(0) = val
End Function

Private Function ArrayPop(ByRef arr As Variant)
    ReDim Preserve arr(UBound(arr) - 1)
End Function

Private Function StringToUtf8Bytes(ByRef sData As String) As Byte()
    Dim arr() As Byte
    With CreateObject("ADODB.Stream")
        .Mode = 3 'adModeReadWrite
        .Open
        .Type = 2 'adTypeText
        .Charset = "UTF-8"
        .WriteText sData
        .Position = 0
        .Type = 1 'adTypeBinary
        .Position = 3 'Skip BOM
        arr = .Read
        .Close
    End With
    StringToUtf8Bytes = arr
End Function

Private Function Utf8BytesToString(ByRef arr() As Byte) As String
    Dim str As String
    With CreateObject("ADODB.Stream")
        .Mode = 3 'adModeReadWrite
        .Open
        .Type = 1 'adTypeBinary
        .Write arr
        .Position = 0
        .Type = 2 'adTypeText
        .Charset = "UTF-8"
        str = .ReadText
        .Close
    End With
    Utf8BytesToString = str
End Function
