Attribute VB_Name = "Util"
'==============================================================================
' Util.bas
' Copyright 2020 Takashi Harano
' Released under the MIT license
' https://github.com/takashiharano/util
'==============================================================================
Option Explicit

Public Const DEFAULT_LINE_SEPARATOR As String = vbLf

' StreamTypeEnum
Const adTypeBinary = 1
Const adTypeText = 2

' LineSeparatorsEnum
Const adCR = 13
Const adCRLF = -1
Const adLF = 10

' StreamWriteEnum
Const adWriteChar = 0
Const adWriteLine = 1

' SaveOptionsEnum
Const adSaveCreateNotExist = 1
Const adSaveCreateOverWrite = 2

'------------------------------------------------------------------------------
'# File
'------------------------------------------------------------------------------
''
' テキストファイルを読み込んで返します。
'
Public Function ReadTextFile(path As String, Optional charset As String = "UTF-8") As String
    Dim buf As String
    With CreateObject("ADODB.Stream")
        .charset = charset
        .Open
        .LoadFromFile path
        buf = .ReadText
        .Close
    End With
    ReadTextFile = buf
End Function

''
' テキストファイルを読み込んで配列で返します。
'
Public Function ReadTextFileAsArray(path As String, Optional charset As String = "UTF-8") As String()
    Dim buf As String
    buf = ReadTextFile(path, charset)
    buf = ReplaceLineSeparator(buf, vbLf)
    Dim arr() As String
    arr = Split(buf, vbLf)
    If arr(UBound(arr)) = "" Then
        ReDim Preserve arr(UBound(arr) - 1)
    End If
    ReadTextFileAsArray = arr
End Function

''
' テキストファイルを書き込みます。
'
Public Sub WriteTextFile(path As String, content As String, Optional charset As String = "UTF-8")
    With CreateObject("ADODB.Stream")
        .Type = adTypeText
        .charset = charset
        .LineSeparator = adLF
        .Open
        .WriteText content, adWriteChar

        .Position = 0
        .Type = adTypeBinary
        .Position = 3

        Dim tmpBuf() As Byte
        tmpBuf = .Read
        .Close

        .Open
        .Write tmpBuf

        .SaveToFile path, adSaveCreateOverWrite
        .Close
    End With
End Sub

''
' ファイルが存在するかを返します。
'
Public Function FileExists(path As String) As Boolean
    If Dir(path) = "" Then
        FileExists = False
    Else
        FileExists = True
    End If
End Function

''
' ファイルを削除します。
' 戻り値：削除したらTrue
'
Public Function DeleteFile(path As String) As Boolean
    DeleteFile = False
    If FileExists(path) Then
        Kill path
        DeleteFile = True
    End If
End Function

''
' フォルダを選択します。
' 選択ダイアログを表示、選択するとそのパスを返します。
' outCell:="A1" A1セルにパス文字列を設定します。
'
Public Function SelectFolderPath(Optional outCell As String = "", Optional ws As Worksheet = Nothing)
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If

    Dim path As String
    path = ""

    With Application.FileDialog(msoFileDialogFolderPicker)
        If .Show = True Then
            path = .SelectedItems(1)
        End If
    End With

    If path <> "" And outCell <> "" Then
        ws.Range(outCell).value = path
    End If

    SelectFolderPath = path
End Function

''
' ファイルパスを選択します。
' 選択ダイアログを表示、選択するとそのパスを返します。
' title:="ダイアログタイトル"
' fileFilter:="ファイル種別フィルター"
' outCell:="A1" A1セルにパス文字列を設定します。
'
Public Function SelectFilePath(Optional title As String = "", _
                               Optional fileFilter As String = "All Files (*.*),*.*", _
                               Optional outCell As String = "", _
                               Optional ws As Worksheet = Nothing)
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If

    Dim path As String
    path = Application.GetOpenFilename(fileFilter, , title)
    If path = "False" Then
        path = ""
    End If
    If path <> "" And outCell <> "" Then
        ws.Range(outCell).value = path
    End If
    SelectFilePath = path
End Function

''
' パスからファイル名の部分を返します。
' "C:\dir\file.txt" -> "bar.txt"
' "file.txt"        -> "bar.txt"
'
Public Function GetFileName(path) As String
    Dim pos As Long
    pos = InStrRev(path, "\")
    GetFileName = Mid(path, pos + 1)
End Function

''
' パスから拡張子を返します。
' "C:\dir\file.txt" -> "txt"
' "file.txt"        -> "txt"
'
Public Function GetFileExtension(path) As String
    Dim fileName As String
    Dim pos As Long
    fileName = GetFileName(path)
    pos = InStrRev(fileName, ".")
    GetFileExtension = Mid(fileName, pos + 1)
End Function

''
' 指定されたフォルダを親パス含めて生成します。
'
' 絶対: "C:\a\b\c"
' 相対: "a\b\c"
'
Public Sub MkDirs(path As String)
    Dim i As Long
    Dim depth As Long
    Dim targetPath As String
    Dim dirs() As String
    dirs = Split(path, "\")
    depth = UBound(dirs)
    targetPath = ""
    For i = 0 To depth
        targetPath = targetPath & dirs(i) & "\"
        If Dir(targetPath, vbDirectory) = "" Then
            MkDir (targetPath)
        End If
    Next i
End Sub

'------------------------------------------------------------------------------
'# Workbook
'------------------------------------------------------------------------------
''
' ExcelのBookが開いているか返します。
'
Public Function IsBookOpened(path) As Boolean
    IsBookOpened = False
    If path = "" Then
        Exit Function
    End If
    On Error Resume Next
    Open path For Append As #1
    Close #1
    If Err.Number > 0 Then
        IsBookOpened = True
    End If
End Function

''
' ExcelのBookを開きます。
'
Public Function OpenBook(path) As Workbook
    Set OpenBook = Nothing
    On Error GoTo ErrHandler
    If IsBookOpened(path) Then
        Dim fileName As String
        fileName = GetFileName(path)
        Set OpenBook = Workbooks(fileName)
    Else
        Set OpenBook = Workbooks.Open(path)
    End If
ErrHandler:
End Function

''
' ExcelのBookを閉じます。
'
Public Function CloseBook(path)
    If path = "" Or IsBookOpened(path) = False Then
        CloseBook = False
        Exit Function
    End If
    Dim fileName As String
    fileName = GetFileName(path)
    Workbooks(fileName).Close
    CloseBook = True
End Function

'------------------------------------------------------------------------------
'# 配列操作
'------------------------------------------------------------------------------
''
' 配列に要素をpushします。
'
Public Sub ArrPush(ByRef arr As Variant, val As Variant)
    On Error GoTo ArrInit
    ReDim Preserve arr(UBound(arr) + 1)
    arr(UBound(arr)) = val
    Exit Sub
ArrInit:
    ReDim arr(0)
    arr(0) = val
End Sub

''
' 配列のサイズを返します。
'
Public Function GetArraySize(ByRef arr As Variant, Optional dimension As Long = 1) As Long
    GetArraySize = -1
    On Error Resume Next
    If IsEmptyArray(arr) Then
        GetArraySize = 0
    Else
        GetArraySize = UBound(arr, dimension) - LBound(arr, dimension) + 1
    End If
    On Error GoTo 0
End Function

''
' 配列の次元数を返します。
'
Public Function GetArrayDimensions(ByRef arr As Variant) As Long
    Dim i As Long
    Dim n As Long
    i = 0
    n = 0
    On Error Resume Next
    Do While Err.Number = 0
        i = i + 1
        n = UBound(arr, i)
    Loop
    On Error GoTo 0
    GetArrayDimensions = i - 1
End Function

''
' 配列が空であるかを返します。
'
Public Function IsEmptyArray(arr As Variant) As Boolean
    IsEmptyArray = False
    Dim i As Long
    On Error GoTo ErrHandler
    i = UBound(arr)
    Exit Function
ErrHandler:
    IsEmptyArray = True
End Function

''
' 配列を重複要素のないSetコレクションに変換して返します。
'
Public Function Array2Set(ByRef arr As Variant) As Variant
    Dim dic As Object
    Set dic = CreateObject("Scripting.Dictionary")
    Dim val As Variant
    Dim i As Long
    For i = 0 To UBound(arr)
        val = arr(i)
        If dic.Exists(val) Then
            dic.Item(val) = dic.Item(val) + 1
        Else
            dic.Item(val) = 1
        End If
    Next
    Array2Set = dic.Keys()
End Function

'------------------------------------------------------------------------------
'# セル値
'------------------------------------------------------------------------------
''
' 指定されたセルの値を返します。(Rangeラッパー)
' GetCellValue("A1")
'
Public Function GetCellValue(ref As String, Optional ws As Worksheet = Nothing) As Variant
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If
    GetCellValue = Range(ref).value
End Function

''
' 指定されたセルの値を設定します。(Rangeラッパー)
' SetCellValue("A1", "abc")
'
Public Sub SetCellValue(ref As String, val As Variant, Optional ws As Worksheet = Nothing)
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If
    Range(ref).value = val
End Sub

''
' シートの値をA1から存在する範囲全て取得して返します。
'
Public Function GetSheetValues(Optional sheetName As String = "", Optional ws As Worksheet = Nothing) As Variant
    If ws Is Nothing Then
        If sheetName = "" Then
            Set ws = ActiveSheet
        Else
            Set ws = Sheets(sheetName)
        End If
    End If

    Dim lastRowN As Long
    Dim lastColN As Long
    Dim lastColA As String

    lastRowN = GetLastRowOfSheet(sheetName, ws)
    lastColN = GetLastColOfSheet(sheetName, ws)
    lastColA = XlsColN2A(lastColN)

    Dim addr As String
    addr = "A1:" & lastColA & lastRowN

    Dim vals As Variant
    vals = GetRangeValues(addr, ws:=ws)

    GetSheetValues = vals
End Function

''
' 指定されたセル範囲の値を2次元配列で返します。
'
' GetRangeValues("A1:B2")
' -> ret(1, 1) = A1
'    ret(1, 2) = B1
'
' GetRangeValues("A1:B2", transpose=True)
' -> ret(1, 1) = A1
'    ret(1, 2) = A2
'
' GetRangeValues("A1:B2", idxOrigin=0)
' -> ret(0, 0) = A1
'    ret(0, 1) = B1
'
' GetRangeValues("A1:B2", transpose=True, idxOrigin=0)
' -> ret(0, 0) = A1
'    ret(0, 1) = A2
'
Public Function GetRangeValues(refs As String, _
                               Optional transpose As Boolean = False, _
                               Optional idxOrigin As Long = 1, _
                               Optional ws As Worksheet = Nothing) As Variant
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If

    Dim rangeVals As Variant
    rangeVals = ws.Range(refs)
    If Not IsMultiRange(refs) Then
        rangeVals = SingleValueToArray(rangeVals)
    End If

    If transpose = False And idxOrigin = 1 Then
        GetRangeValues = rangeVals
        Exit Function
    End If

    Dim x As Long
    Dim y As Long
    Dim d1 As Long
    Dim d2 As Long
    Dim d1Max As Long
    Dim d2Max As Long

    Dim rowLen As Long
    Dim colLen As Long
    rowLen = UBound(rangeVals, 1)
    colLen = UBound(rangeVals, 2)

    If transpose Then
        d1Max = colLen
        d2Max = rowLen
    Else
        d1Max = rowLen
        d2Max = colLen
    End If

    Dim adj As Long
    If idxOrigin <= 1 Then
        adj = (1 - idxOrigin) * (-1)
    Else
        adj = idxOrigin - 1
    End If

    d1Max = d1Max + adj
    d2Max = d2Max + adj

    Dim ret As Variant
    ReDim ret(idxOrigin To d1Max, idxOrigin To d2Max)

    d1 = idxOrigin
    d2 = idxOrigin
    For x = 1 To colLen
        If transpose Then
            d2 = idxOrigin
        Else
            d1 = idxOrigin
        End If

        For y = 1 To rowLen
            ret(d1, d2) = rangeVals(y, x)
            If transpose Then
                d2 = d2 + 1
            Else
                d1 = d1 + 1
            End If
        Next y

        If transpose Then
            d1 = d1 + 1
        Else
            d2 = d2 + 1
        End If
    Next x

    GetRangeValues = ret
End Function

''
' 指定されたセル範囲に2次元配列の値を展開します。
'
' SetRangeValues("A1:B2", values, transpose)
' -> 範囲かvaluesの範囲の小さい範囲に展開
'
' SetRangeValues("A1", values, transpose)
' -> A1を起点にvaluesを全展開
'
' Optional
'  transpose=False : [行][列]
'  transpose=True  : [列][行]
'
Public Sub SetRangeValues(refs As String, values As Variant, _
                          Optional transpose As Boolean = False, _
                          Optional ws As Worksheet = Nothing)
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If

    Dim refRowLen As Long
    Dim refColLen As Long
    Dim valRowOrigin As Long
    Dim valColOrigin As Long
    Dim valRowMax As Long
    Dim valColMax As Long
    Dim valRowLen As Long
    Dim valColLen As Long
    Dim stColA As String
    Dim stRow As Long
    Dim edColA As String
    Dim edRow As Long
    Dim rangeRowMax As Long
    Dim rangeColMax As Long
   rangeRowMax = -1
   rangeColMax = -1

    refRowLen = CountRowRange(refs)
    refColLen = CountColRange(refs)

    If transpose Then
        valRowOrigin = LBound(values, 2)
        valColOrigin = LBound(values, 1)
        valRowMax = UBound(values, 2)
        valColMax = UBound(values, 1)
    Else
        valRowOrigin = LBound(values, 1)
        valColOrigin = LBound(values, 2)
        valRowMax = UBound(values, 1)
        valColMax = UBound(values, 2)
    End If

    valRowLen = valRowMax - valRowOrigin + 1
    valColLen = valColMax - valColOrigin + 1

    Dim targetRefs As String
    targetRefs = refs
    If IsMultiRange(refs) Then
        Dim wkRefs As Variant
        Dim stRef As String
        Dim edRef As String
        wkRefs = Split(refs, ":")
        stRef = wkRefs(0)
        edRef = wkRefs(1)
        stColA = GetCellRefA(stRef)
        stRow = GetCellRefN(stRef)
        edColA = GetCellRefA(edRef)
        edRow = GetCellRefN(edRef)

        If refRowLen > valRowLen Then
            edRow = stRow + valRowLen - 1
        End If

        If refColLen > valColLen Then
            edColA = RelativeXlsColA(stColA, valColLen - 1)
        End If

        targetRefs = stColA & stRow & ":" & edColA & edRow
    Else
        stColA = GetCellRefA(refs)
        stRow = GetCellRefN(refs)
        edColA = RelativeXlsColA(stColA, valColLen - 1)
        edRow = stRow + valRowLen - 1
        targetRefs = refs & ":" & edColA & edRow
        refRowLen = valRowLen
        refColLen = valColLen
    End If

    Dim rowLen As Long
    rowLen = refRowLen
    If rowLen > valRowLen Then
        rowLen = valRowLen
    End If

    Dim colLen As Long
    colLen = refColLen
    If colLen > valColLen Then
        colLen = valColLen
    End If

    If rangeRowMax = -1 Then
        Dim adjR As Long
        If valRowOrigin <= 1 Then
            adjR = 1 - valRowOrigin
        Else
            adjR = valRowOrigin
        End If
        rangeRowMax = rowLen + adjR
    End If

    If rangeColMax = -1 Then
        Dim adjC As Long
        If valColOrigin <= 1 Then
            adjC = 1 - valColOrigin
        Else
            adjC = valColOrigin
        End If
        rangeColMax = colLen + adjC
    End If

    Dim rangeVals As Variant
    ReDim rangeVals(1 To rangeRowMax, 1 To rangeColMax)

    Dim refRowIdx As Long
    Dim refColIdx As Long
    Dim valRowIdx As Long
    Dim valColIdx As Long
    Dim i As Long
    Dim j As Long
    refRowIdx = 1
    valRowIdx = valRowOrigin
    For i = 1 To rowLen
        refColIdx = 1
        valColIdx = valColOrigin
        For j = 1 To colLen
            Dim val As Variant
            If transpose Then
                val = values(valColIdx, valRowIdx)
            Else
                val = values(valRowIdx, valColIdx)
            End If
            rangeVals(refRowIdx, refColIdx) = val
            valColIdx = valColIdx + 1
            refColIdx = refColIdx + 1
        Next j
        valRowIdx = valRowIdx + 1
        refRowIdx = refRowIdx + 1
    Next i

    ws.Range(targetRefs) = rangeVals
End Sub

''
' セル範囲内の値を1次元配列に格納します。(行方向→)
'
' RowToArray("A1:B3")
' RowToArray("Sheet1!A1:B3")
' -> [A1, B1, A2, B2, A3, B3]
'
Public Function RowToArray(refs As String, Optional ws As Worksheet = Nothing) As Variant
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If

    Dim sheetName As String
    Dim pos As Long
    sheetName = ""
    pos = InStr(refs, "!")
    If pos > 0 Then
        sheetName = Left(refs, pos - 1)
        Set ws = Sheets(sheetName)
        refs = Mid(refs, pos + 1)
    End If

    Dim rangeVals As Variant
    Dim rowLen As Long
    Dim colLen As Long

    rangeVals = ws.Range(refs)
    Dim arr As Variant

    If Not IsMultiRange(refs) Then
        ReDim arr(0)
        arr(0) = rangeVals
        RowToArray = arr
        Exit Function
    End If

    rowLen = UBound(rangeVals, 1)
    colLen = UBound(rangeVals, 2)
    ReDim arr(colLen * rowLen - 1)

    Dim i As Long
    Dim j As Long
    Dim cnt As Long
    cnt = 0
    For i = 1 To rowLen
        For j = 1 To colLen
            arr(cnt) = rangeVals(i, j)
            cnt = cnt + 1
        Next j
    Next i
    RowToArray = arr
End Function

''
' セル範囲内の値を1次元配列に格納します。(列方向↓)
'
' ColToArray("A1:B3")
' ColToArray("Sheet1!A1:B3")
' -> [A1, A2, A3, B1, B2, B3]
'
Public Function ColToArray(refs As String, Optional ws As Worksheet = Nothing) As Variant
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If

    Dim sheetName As String
    Dim pos As Long
    sheetName = ""
    pos = InStr(refs, "!")
    If pos > 0 Then
        sheetName = Left(refs, pos - 1)
        Set ws = Sheets(sheetName)
        refs = Mid(refs, pos + 1)
    End If

    Dim rangeVals As Variant
    Dim rowLen As Long
    Dim colLen As Long

    rangeVals = ws.Range(refs)
    Dim arr As Variant

    If Not IsMultiRange(refs) Then
        ReDim arr(0)
        arr(0) = rangeVals
        ColToArray = arr
        Exit Function
    End If

    rowLen = UBound(rangeVals, 1)
    colLen = UBound(rangeVals, 2)
    ReDim arr(colLen * rowLen - 1)

    Dim i As Long
    Dim j As Long
    Dim cnt As Long
    cnt = 0
    For i = 1 To colLen
        For j = 1 To rowLen
            arr(cnt) = rangeVals(j, i)
            cnt = cnt + 1
        Next j
    Next i
    ColToArray = arr
End Function

''
' 1次元配列の値を行に展開します。
'
' ArrayToRow(arr, "A1")
'   A      B      C
' 1 arr(0) arr(1) arr(2) ...
' 2
'
Public Sub ArrayToRow(arr As Variant, targetStartCell As String, Optional ws As Worksheet = Nothing)
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If

    Dim arrLastIdx As Long
    arrLastIdx = UBound(arr)

    Dim rangeArr As Variant
    ReDim rangeArr(1, arrLastIdx)
    Dim i As Long
    For i = 0 To arrLastIdx
        rangeArr(0, i) = arr(i)
    Next i

    Dim sheetName As String
    Dim pos As Long
    sheetName = ""
    pos = InStr(targetStartCell, "!")
    If pos > 0 Then
        sheetName = Left(targetStartCell, pos - 1)
        targetStartCell = Mid(targetStartCell, pos + 1)
    End If

    Dim targetEndCell As String
    targetEndCell = RelativeCellAddr(targetStartCell, arrLastIdx, 0)

    Dim rangeAddr As String
    rangeAddr = targetStartCell & ":" & targetEndCell

    If sheetName <> "" Then
        Set ws = Sheets(sheetName)
    End If

    ws.Range(rangeAddr) = rangeArr
End Sub

''
' 1次元配列の値を列に展開します。
'
' ArrayToCol(arr, "A1")
'   A      B      C
' 1 arr(0)
' 2 arr(1)
' 3 arr(2)
'   :
'
Public Sub ArrayToCol(arr As Variant, targetStartCell As String, Optional ws As Worksheet = Nothing)
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If

    Dim arrLastIdx As Long
    arrLastIdx = UBound(arr)

    Dim rangeArr As Variant
    ReDim rangeArr(arrLastIdx, 1)
    Dim i As Long
    For i = 0 To arrLastIdx
        rangeArr(i, 0) = arr(i)
    Next i

    Dim sheetName As String
    Dim pos As Long
    sheetName = ""
    pos = InStr(targetStartCell, "!")
    If pos > 0 Then
        sheetName = Left(targetStartCell, pos - 1)
        targetStartCell = Mid(targetStartCell, pos + 1)
    End If

    Dim targetEndCell As String
    targetEndCell = RelativeCellAddr(targetStartCell, 0, arrLastIdx)

    Dim rangeAddr As String
    rangeAddr = targetStartCell & ":" & targetEndCell

    If sheetName <> "" Then
        Set ws = Sheets(sheetName)
    End If

    ws.Range(rangeAddr) = rangeArr
End Sub

''
' 指定された範囲の列方向↓の値を行方向→に展開します。
'
' ColToRow("A", "B1")
' ColToRow("A1:A3", "B1")
' ColToRow("Sheet1!A1:A3", "Sheet2!B1")
'
Public Sub ColToRow(fmAddr As String, toAddr As String)
    Dim sheetName As String
    sheetName = ""
    Dim pos As Long
    pos = InStr(fmAddr, "!")
    If pos > 0 Then
        sheetName = Left(fmAddr, pos - 1)
        fmAddr = Mid(fmAddr, pos + 1)
    End If

    Dim ws As Worksheet
    Set ws = Nothing
    If sheetName <> "" Then
    Set ws = Sheets(sheetName)
    End If

    If IsOnlyColName(fmAddr) Then
        Dim fmCol As String
        Dim a() As String
        a = SplitRefAddrs(fmAddr)
        fmCol = a(1)

        Dim firstRow As Long
        Dim lastRow As Long
        firstRow = GetFirstRow(fmCol, ws)
        lastRow = GetLastRow(fmCol, ws)

        fmAddr = fmCol & firstRow & ":" & fmCol & lastRow
    End If

    Dim arr As Variant
    arr = ColToArray(fmAddr, ws)
    Call ArrayToRow(arr, toAddr)
End Sub

''
' 指定された範囲の行方向→の値を列方向↓に展開します。
'
' RowToCol("1", "A2")
' RowToCol("A1:C1", "A2")
' RowToCol("Sheet1!A1:C1", "Sheet2!A2")
'
Public Sub RowToCol(fmAddr As String, toAddr As String)
    Dim sheetName As String
    sheetName = ""
    Dim pos As Long
    pos = InStr(fmAddr, "!")
    If pos > 0 Then
        sheetName = Left(fmAddr, pos - 1)
        fmAddr = Mid(fmAddr, pos + 1)
    End If

    Dim ws As Worksheet
    Set ws = Nothing
    If sheetName <> "" Then
    Set ws = Sheets(sheetName)
    End If

    If IsOnlyRowNumber(fmAddr) Then
        Dim fmRow As Long
        Dim a() As String
        a = SplitRefAddrs(fmAddr)
        fmRow = a(2)

        Dim firstCol As String
        Dim lastCol As String
        firstCol = GetFirstCol(fmRow, ws)
        lastCol = GetLastCol(fmRow, ws)

        fmAddr = firstCol & fmRow & ":" & lastCol & fmRow
    End If

    Dim arr As Variant
    arr = RowToArray(fmAddr, ws)
    Call ArrayToCol(arr, toAddr)
End Sub

''
' 選択されている範囲を指定されたセルを先頭に行方向→に展開します。
'
Public Sub SelectionToRow(dest As String)
    Dim vals As Variant
    vals = GetSelectedValues()
    Call ArrayToRow(vals, dest)
End Sub

''
' 選択されている範囲を指定されたセルを先頭に列方向↓に展開します。
'
Public Sub SelectionToCol(dest As String)
    Dim vals As Variant
    vals = GetSelectedValues()
    Call ArrayToCol(vals, dest)
End Sub

''
' 選択されている範囲の値を1次元配列として返します。
'
Public Function GetSelectedValues() As Variant
    Dim cnt As Long
    Dim i As Long
    cnt = Selection.count
    Dim vals As Variant
    ReDim vals(cnt)
    For i = 1 To cnt
        vals(i - 1) = Selection(i)
    Next i
    GetSelectedValues = vals
End Function

''
' 指定された列範囲の値を重複要素のないSetコレクションに変換して返します。
' ExtractUniqueValues("A1:B20")
'
Public Function ExtractUniqueValues(refs As String, Optional ws As Worksheet = Nothing) As Variant
    Dim arr As Variant
    arr = ColToArray(refs)
    ExtractUniqueValues = Array2Set(arr)
End Function

''
' 指定された列範囲の値を重複要素のないSetコレクションに変換し、指定されたセルを起点に列方向↓に表示します。
' Call PrintUniqueValues("A1:B20", "B1")
' Call PrintUniqueValues("A:A", "B1")
'
Public Sub PrintUniqueValues(refs As String, outStartCellAddr As String, Optional ws As Worksheet = Nothing)
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If
    Dim s As Variant
    s = ExtractUniqueValues(refs)
    Call ArrayToCol(s, outStartCellAddr, ws)
End Sub

''
' 選択されている範囲の値を重複要素のないSetコレクションに変換し、指定されたセルを起点に列方向↓に表示します。
'
Public Sub SelectionToUniqueValues(dest As String, Optional ws As Worksheet = Nothing)
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If
    Dim vals As Variant
    vals = GetSelectedValues()
    Dim s As Variant
    s = Array2Set(vals)
    Call ArrayToCol(s, dest, ws)
End Sub

''
' 単一の値を2次元配列に格納します。
'
Private Function SingleValueToArray(value As Variant)
    Dim arr As Variant
    ReDim arr(1 To 1, 1 To 1)
    arr(1, 1) = value
    SingleValueToArray = arr
End Function

'------------------------------------------------------------------------------
'# 文字列操作
'------------------------------------------------------------------------------
''
' 文字列のパターンマッチを検査します。
' ("ABC123", "\d") -> True
' ("ABC123", "abc") -> False
' ("ABC123", "abc", True) -> True
' ("A(B)", "A(B)", escMeta:=True) -> True
' ("A(B)", "A(B)", escMeta:=False) -> False
'
Public Function Match(target As String, pattern As String, Optional ignoreCase As Boolean = False, Optional escMeta As Boolean = False) As Boolean
    If escMeta Then
        pattern = EscapeMetaChars(pattern)
    End If
    Dim regexp As Object
    Set regexp = CreateObject("VBScript.RegExp")
    With regexp
        .pattern = pattern
        .ignoreCase = ignoreCase
        .Global = True
    End With
    Match = regexp.Test(target)
End Function

''
' 文字列のパターンにマッチした部分を抜き出します。
' ("abc123xyz", ".+?(\d+).*") -> "123"
'
Public Function GetPattern(target As String, pattern As String) As String
    Dim regexp As Object
    Set regexp = CreateObject("VBScript.RegExp")
    With regexp
        .pattern = pattern
        .ignoreCase = False
        .Global = True
    End With

    Dim result As String
    result = ""
    Dim matches As Variant
    Set matches = regexp.Execute(target)
    If matches.count > 0 Then
        Dim m As Variant
        Set m = matches(0)
        result = m.SubMatches(0)
    End If

    GetPattern = result
End Function

''
' 正規表現のメタ文字をエスケープして返します。
'
Public Function EscapeMetaChars(str As String) As String
    str = Replace(str, "\", "\\")
    str = Replace(str, "^", "\^")
    str = Replace(str, "$", "\$")
    str = Replace(str, "(", "\(")
    str = Replace(str, ")", "\)")
    str = Replace(str, "[", "\[")
    str = Replace(str, "]", "\]")
    str = Replace(str, ".", "\.")
    str = Replace(str, "*", "\*")
    str = Replace(str, "+", "\+")
    str = Replace(str, "?", "\?")
    str = Replace(str, "|", "\|")
    str = Replace(str, "{", "\{")
    str = Replace(str, "}", "\}")
    EscapeMetaChars = str
End Function

''
' 改行コードを変換して返します。
'
Function ReplaceLineSeparator(text As String, separator As String) As String
    Dim newText As String
    newText = Replace(text, vbCrLf, vbLf)
    newText = Replace(text, vbCr, vbLf)
    newText = Replace(text, vbLf, separator)
    ReplaceLineSeparator = newText
End Function

''
' 文字列の長さを幅でカウントします。
' 半角=1/全角=2
'
Public Function LenW(str As String) As Long
    Dim ch As String
    Dim cd As Integer
    Dim cnt As Long
    Dim i As Long
    cnt = 0
    For i = 1 To Len(str)
        ch = Mid(str, i, 1)
        cd = Asc(ch)
        If cd >= 0 And cd <= 255 Then
            cnt = cnt + 1
        Else
            cnt = cnt + 2
        End If
    Next
    LenW = cnt
End Function

''
' リスト内にある文字列の最長の文字数を返します。
'
' textList
'     文字列のリスト
' byWidth
'     文字数ではなく文字幅(半角=1/全角=2)で扱います。
'
Public Function CountLongestLen(textList() As String, Optional byWidth As Boolean = True)
    Dim longestLen As Long
    Dim i As Long
    Dim textLen As Long
    Dim text As String
    longestLen = 0
    For i = 0 To UBound(textList)
        text = textList(i)
        If byWidth Then
            textLen = LenW(text)
        Else
            textLen = Len(text)
        End If
        If textLen > longestLen Then
            longestLen = textLen
        End If
    Next
    CountLongestLen = longestLen
End Function

''
' 指定された文字列でパディングします。
'
' str
'     元の文字列
' padding
'     パディング用文字列
' totalLen
'     パディング後のトータル文字数(半角=1/全角=2でカウント)
' direction
'     パディング位置
'     "L" = Pad to the Left
'     "R" = Pad to the Right
' byWidth
'     文字数ではなく文字幅(半角=1/全角=2)で扱います。
'
Public Function PadString(str As String, padding As String, totalLen As Long, direction As String, Optional byWidth As Boolean = True)
    Dim ret As String
    Dim strLen As Long

    If byWidth Then
        strLen = LenW(str)
    Else
        strLen = Len(str)
    End If

    ret = str
    If strLen < totalLen Then
        Dim n As Long
        n = totalLen - strLen
        Dim pad As String
        Dim i As Long
        pad = String(n, padding)
        If direction = "L" Then
            ret = pad & str
        Else
            ret = str & pad
        End If
    End If

    PadString = ret
End Function

''
' 文字列を1文字ずつに分解した配列にして返します。
'
Public Function String2Array(str As String) As String()
    Dim strLen As Long
    strLen = Len(str)
    Dim arr() As String
    ReDim arr(strLen)
    Dim i As Long
    For i = 0 To strLen
        arr(i) = Mid(str, i + 1, 1)
    Next i
    String2Array = arr
End Function

''
' 指定された文字集合で指定された文字列が何番目に出現するかを返します。
' ("ABC", "A")  -> 1
' ("ABC", "B")  -> 2
' ("ABC", "AA") -> 4
'
Public Function StrPIndex(tbl As String, ptn As String) As Long
    Dim ptnLen As Long
    Dim rdx As Long
    Dim idx As Long
    Dim i As Long

    ptnLen = Len(ptn)
    rdx = Len(tbl)
    idx = 0

    Dim d As Long
    Dim c As String
    Dim v As Long
    Dim n As Long
    For i = 0 To (ptnLen - 1)
        d = ptnLen - i
        c = Mid(ptn, d, 1)
        v = InStr(tbl, c)
        If v = 0 Then
           StrPIndex = 0
           Exit Function
        End If
        n = v * (rdx ^ i)
        idx = idx + n
    Next
    StrPIndex = idx
End Function

''
' 文字集合を順列で並べた場合の指定されたインデックスに値する文字列を返します。
' ("ABC", 1) -> "A"
' ("ABC", 2) -> "B"
' ("ABC", 4) -> "AA"
'
Public Function StrP(strTbl As String, idx As Long) As String
    If idx < 1 Then
        StrP = ""
        Exit Function
    End If

    Dim tbl() As String
    tbl = String2Array(strTbl)
    Dim tbllen As Long
    tbllen = Len(strTbl)

    Dim a() As Long
    Call ArrPush(a, -1)

    Dim i As Long
    For i = 0 To idx - 1
        Dim j As Long
        j = 0

        Dim carryFlag As Boolean
        carryFlag = True

        Dim aLen As Long
        aLen = UBound(a) + 1
        Do While j < aLen
            If carryFlag = True Then
                a(j) = a(j) + 1
                If a(j) > (tbllen - 1) Then
                    a(j) = 0
                    If (aLen <= j + 1) Then
                        Call ArrPush(a, -1)
                        aLen = UBound(a) + 1
                    End If
                Else
                    carryFlag = False
                End If
            End If
            j = j + 1
        Loop
    Next
    Dim s As String
    s = ""
    For i = UBound(a) To 0 Step -1
        s = s & tbl(a(i))
    Next
    StrP = s
End Function

'------------------------------------------------------------------------------
'# セル参照アドレス
'------------------------------------------------------------------------------
''
' セル参照の英字部分を返します。
' GetCellRefA("A1") -> "A"
' GetCellRefA("$A$1") -> "A"
'
Public Function GetCellRefA(ref As String) As String
    Dim reg
    Set reg = CreateObject("VBScript.RegExp")

    With reg
        .pattern = "\$"
        .ignoreCase = True
        .Global = True
    End With

    Dim a As String
    a = reg.Replace(ref, "")
    reg.pattern = "\d+$"
    a = reg.Replace(a, "")

    GetCellRefA = a
End Function

''
' セル参照の数字部分を返します。
' GetCellRefA("A1") -> 1
' GetCellRefA("$A$1") -> 1
' GetCellRefA("A") -> 0
'
Public Function GetCellRefN(ref As String) As Long
    Dim reg
    Set reg = CreateObject("VBScript.RegExp")

    With reg
        .pattern = "\$"
        .ignoreCase = True
        .Global = True
    End With

    Dim a As String
    a = reg.Replace(ref, "")
    reg.pattern = "^[A-Za-z]+"
    a = reg.Replace(a, "")
    If a = "" Then
        a = "0"
    End If

    GetCellRefN = a
End Function

''
' セル番号をセル名に変換します。
'  1 -> "A"
'  2 -> "B"
' 26 -> "Z"
' 27 -> "AA"
'
Public Function XlsColN2A(index As Long) As String
    XlsColN2A = StrP("ABCDEFGHIJKLMNOPQRSTUVWXYZ", index)
End Function

''
' セル名をセル番号に変換します。
'  "A" ->  1
'  "B" ->  2
'  "Z" -> 26
' "AA" -> 27
'
Public Function XlsColA2N(col As String) As Long
    XlsColA2N = StrPIndex("ABCDEFGHIJKLMNOPQRSTUVWXYZ", col)
End Function

''
' セル名からのオフセットの位置にあるセル名を返します。
'  "A",  1 -> "B"
'  "B", -1 -> "A"
'
Public Function RelativeXlsColA(col As String, offset As Long) As String
    Dim n As Long
    n = XlsColA2N(col) + offset
    RelativeXlsColA = XlsColN2A(n)
End Function

''
' セルアドレスからのオフセットの位置にあるアドレスを返します。
'  "A1",  1,  2 -> "B3"
'  "B3", -1. -2 -> "A1"
'
Public Function RelativeCellAddr(orgAddr As String, colOffset As Long, rowOffset) As String
    Dim a As String
    Dim n As Long
    a = GetCellRefA(orgAddr)
    n = GetCellRefN(orgAddr)

    Dim targetA As String
    Dim targetN As Long
    targetA = RelativeXlsColA(a, colOffset)
    targetN = n + rowOffset

    If targetA = "" Or targetN = 0 Then
        RelativeCellAddr = ""
    Else
        RelativeCellAddr = targetA & targetN
    End If
End Function

''
' セル参照範囲に含まれる行数を返します。
' CountRowRange("A1:B3") -> 3
' CountRowRange("A1")    -> 1
'
Public Function CountRowRange(refs As String) As Long
    Dim wkRefs As Variant
    Dim stRef As String
    Dim edRef As String
    Dim stN As String
    Dim edN As String

    wkRefs = Split(refs, ":")
    If UBound(wkRefs) < 1 Then
        CountRowRange = 1
        Exit Function
    End If
    stRef = wkRefs(0)
    edRef = wkRefs(1)
    stN = GetCellRefN(stRef)
    edN = GetCellRefN(edRef)

    If stN > edN Then
        Dim tmp As String
        tmp = stN
        stN = edN
        edN = tmp
    End If

    Dim count As Long
    count = edN - stN + 1

    CountRowRange = count
End Function

''
' セル参照範囲に含まれる列数を返します。
' CountColRange("A1:B3") -> 2
' CountColRange("A1")    -> 1
'
Public Function CountColRange(refs As String) As Long
    Dim wkRefs As Variant
    Dim stRef As String
    Dim edRef As String
    Dim stA As String
    Dim stColIdx As Long
    Dim edA As String
    Dim edColIdx As Long

    wkRefs = Split(refs, ":")
    If UBound(wkRefs) < 1 Then
        CountColRange = 1
        Exit Function
    End If
    stRef = wkRefs(0)
    edRef = wkRefs(1)
    stA = GetCellRefA(stRef)
    edA = GetCellRefA(edRef)

    stColIdx = XlsColA2N(stA)
    edColIdx = XlsColA2N(edA)

    If stColIdx > edColIdx Then
        Dim tmp As String
        tmp = stColIdx
        stColIdx = edColIdx
        edColIdx = tmp
    End If

    Dim count As Long
    count = edColIdx - stColIdx + 1

    CountColRange = count
End Function

''
' セル参照が複数セルであるかを返します。
' "A1"    -> False
' "A1:A1" -> False
' "A1:B2" -> True
' "A:A"   -> True
' "1:1"   -> True
'
Public Function IsMultiRange(refs As String) As Boolean
    IsMultiRange = False

    Dim wkRefs As Variant
    Dim stRef As String
    Dim edRef As String
    Dim stA As String
    Dim stN As Long
    Dim edA As String
    Dim edN As Long

    wkRefs = Split(refs, ":")
    If UBound(wkRefs) < 1 Then
        Exit Function
    End If

    stRef = wkRefs(0)
    edRef = wkRefs(1)
    stA = GetCellRefA(stRef)
    stN = GetCellRefN(stRef)
    edA = GetCellRefA(edRef)
    edN = GetCellRefN(edRef)

    If stRef <> edRef Or stA = "" Or edA = "" Or stN = 0 Or edN = 0 Then
        IsMultiRange = True
    End If
End Function

''
' セル参照アドレスをパーツに分解します。
'
' "Sheet1!A1:B2"
' -> (0) "Sheet1"
'    (1) "A"
'    (2) "1"
'    (3) "B"
'    (4) "2"
'
' "A:2" -> (0)"", (1)"A", (2)"0", (3)"", (4)"2"
Public Function SplitRefAddrs(refs) As String()
    Dim sheetName As String
    Dim wkRefs As Variant
    Dim stRef As String
    Dim edRef As String
    Dim stA As String
    Dim stN As String
    Dim edA As String
    Dim edN As String

    sheetName = ""
    Dim pos As Long
    pos = InStr(refs, "!")
    If pos > 0 Then
        sheetName = Left(refs, pos - 1)
        refs = Mid(refs, pos + 1)
    End If

    wkRefs = Split(refs, ":")
    stRef = wkRefs(0)
    edRef = ""
    If UBound(wkRefs) >= 1 Then
      edRef = wkRefs(1)
    End If

    stA = GetCellRefA(stRef)
    stN = GetCellRefN(stRef)

    edA = GetCellRefA(edRef)
    edN = GetCellRefN(edRef)

    Dim addrParts(4) As String
    addrParts(0) = sheetName
    addrParts(1) = stA
    addrParts(2) = stN
    addrParts(3) = edA
    addrParts(4) = edN

    SplitRefAddrs = addrParts
End Function

''
' セル参照アドレスが行番号だけであるかを返します。
'
' "1"     -> True
' "A"     -> False
' "A1"    -> False
' "A1:B2" -> False
'
Public Function IsOnlyRowNumber(refs As String) As Boolean
    IsOnlyRowNumber = False
    Dim a() As String
    a = SplitRefAddrs(refs)
    If a(1) = "" And a(2) <> "0" And a(3) = "" And a(4) = "0" Then
        IsOnlyRowNumber = True
    End If
End Function

''
' セル参照アドレスが列名だけであるかを返します。
'
' "A"     -> True
' "A1"    -> False
' "A1:B2" -> False
' "1"     -> False
'
Public Function IsOnlyColName(refs As String) As Boolean
    IsOnlyColName = False
    Dim a() As String
    a = SplitRefAddrs(refs)
    If a(1) <> "" And a(2) = "0" And a(3) = "" And a(4) = "0" Then
        IsOnlyColName = True
    End If
End Function

'------------------------------------------------------------------------------
'# ワークシート
'------------------------------------------------------------------------------
''
' 指定されたシートの先頭行を返します。
'
Public Function GetFirstRowOfSheet(Optional sheetName As String = "", Optional ws As Worksheet = Nothing) As Long
    If ws Is Nothing Then
        If sheetName = "" Then
            Set ws = ActiveSheet
        Else
            Set ws = Sheets(sheetName)
        End If
    End If
    Dim usedRng As Range
    Dim firstRow As Range
    Set usedRng = ws.UsedRange
    Set firstRow = usedRng.rows(1).EntireRow
    GetFirstRowOfSheet = firstRow.row
End Function

''
' 指定されたシートの最終行を返します。
'
Public Function GetLastRowOfSheet(Optional sheetName As String = "", Optional ws As Worksheet = Nothing) As Long
    If ws Is Nothing Then
        If sheetName = "" Then
            Set ws = ActiveSheet
        Else
            Set ws = Sheets(sheetName)
        End If
    End If
    Dim usedRng As Range
    Dim lastRow As Range
    Set usedRng = ws.UsedRange
    Set lastRow = usedRng.rows(usedRng.rows.count).EntireRow
    GetLastRowOfSheet = lastRow.row
End Function

''
' 指定されたシートの先頭列を返します。(A=1, B=2, ...)
'
Public Function GetFirstColOfSheet(Optional sheetName As String = "", Optional ws As Worksheet = Nothing) As Long
    If ws Is Nothing Then
        If sheetName = "" Then
            Set ws = ActiveSheet
        Else
            Set ws = Sheets(sheetName)
        End If
    End If
    Dim usedRng As Range
    Dim firstCol As Range
    Set usedRng = ws.UsedRange
    Set firstCol = usedRng.Columns(1).EntireColumn
    GetFirstColOfSheet = firstCol.Column
End Function

''
' 指定されたシートの最終列を返します。(A=1, B=2, ...)
'
Public Function GetLastColOfSheet(Optional sheetName As String = "", Optional ws As Worksheet = Nothing) As Long
    If ws Is Nothing Then
        If sheetName = "" Then
            Set ws = ActiveSheet
        Else
            Set ws = Sheets(sheetName)
        End If
    End If
    Dim usedRng As Range
    Dim lastCol As Range
    Set usedRng = ws.UsedRange
    Set lastCol = usedRng.Columns(usedRng.Columns.count).EntireColumn
    GetLastColOfSheet = lastCol.Column
End Function

''
' 指定された列の先頭行を返します。
'
Public Function GetFirstRow(col As String, Optional ws As Worksheet = Nothing) As Long
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If
    Dim n As Long
    Dim row As Long
    n = XlsColA2N(col)
    If ws.Cells(1, n).value <> "" Then
        GetFirstRow = 1
        Exit Function
    End If
    row = ws.Cells(1, n).End(xlDown).row
    If row = ws.rows.count And ws.Cells(ws.rows.count, n).value = "" Then
        row = -1
    End If
    GetFirstRow = row
End Function

''
' 指定された列の最終行を返します。
'
Public Function GetLastRow(col As String, Optional ws As Worksheet = Nothing) As Long
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If
    Dim n As Long
    Dim row As Long
    n = XlsColA2N(col)
    row = ws.Cells(ws.rows.count, n).End(xlUp).row
    GetLastRow = row
End Function

''
' 指定された行の先頭列を返します。("A", "B", ...)
'
Public Function GetFirstCol(row As Long, Optional ws As Worksheet = Nothing) As String
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If
    If ws.Cells(row, 1).value <> "" Then
        GetFirstCol = "A"
        Exit Function
    End If
    Dim n As Long
    Dim col As String
    n = ws.Cells(row, 1).End(xlToRight).Column
    If n = ws.Columns.count And ws.Cells(row, ws.Columns.count).value = "" Then
        col = ""
    Else
        col = XlsColN2A(n)
    End If
    GetFirstCol = col
End Function

''
' 指定された行の最終列を返します。("A", "B", ...)
'
Public Function GetLastCol(row As Long, Optional ws As Worksheet = Nothing) As String
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If
    Dim n As Long
    Dim col As String
    n = ws.Cells(row, ws.Columns.count).End(xlToLeft).Column
    col = XlsColN2A(n)
    GetLastCol = col
End Function

''
' 指定されたセルに計算式が存在するかを返します。
'
Public Function HasFormula(ref As String, Optional ws As Worksheet = Nothing) As Boolean
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If
    HasFormula = ws.Range(ref).HasFormula
End Function

''
' シートの表示位置を左上に設定します。
'
Public Sub ScrollToUpperLeft(Optional ws As Worksheet = Nothing)
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If
    ws.Activate
    ActiveWindow.ScrollColumn = 1
    ActiveWindow.ScrollRow = 1
End Sub

''
' シートの表示位置をA1に設定します。
'
Public Sub ScrollToA1Cell(Optional ws As Worksheet = Nothing)
    If ws Is Nothing Then
        Set ws = ActiveSheet
    End If
    ws.Activate
    Application.GoTo Reference:=Cells(1, 1), Scroll:=True
End Sub

''
' ブック内のすべての表示位置をA1に戻します。
'
Public Sub ResetAllSheetsToA1(Optional wb As Workbook = Nothing)
    If wb Is Nothing Then
        Set wb = ActiveWorkbook
    End If
    Dim ws As Worksheet
    Dim i
    For i = wb.Sheets.count To 1 Step -1
        Set ws = wb.Sheets(i)
        ws.Activate
        Application.GoTo Reference:=ws.Cells(1, 1), Scroll:=True
    Next i
End Sub

'------------------------------------------------------------------------------
'# Date Time
'------------------------------------------------------------------------------
''
' Unix timeを返します。
'
' offset
'  ローカルタイムゾーンのUTCからのオフセット(分)
'
Public Function GetUnixTime(Optional offset As Long = 0)
    GetUnixTime = DateDiff("s", #1/1/1970#, Now()) - offset * 60
End Function

''
' ローカルタイムゾーン情報を返します。
'
Public Function GetTimeZoneInfo() As Object
    Dim locator As Object
    Dim service As Object 'SWbemServices
    Dim tzSet As Object
    Set locator = CreateObject("WbemScripting.SWbemLocator")
    Set service = locator.ConnectServer()
    Set tzSet = service.ExecQuery("Select * From Win32_TimeZone")
    Set GetTimeZoneInfo = tzSet
End Function

''
' ローカルタイムゾーンのオフセットを分で返します。
'
Public Function GetLocalTimeZoneOffset() As Long
    Dim tzSet As Object
    Dim tz As Object
    Set tzSet = GetTimeZoneInfo()
    GetLocalTimeZoneOffset = 0
    For Each tz In tzSet
        GetLocalTimeZoneOffset = tz.Bias
        Exit For
    Next
End Function

''
' ローカルタイムゾーン名を返します。
'
Public Function GetLocalTimeZoneName() As String
    Dim tzSet As Object
    Dim tz As Object
    Set tzSet = GetTimeZoneInfo()
    GetLocalTimeZoneName = ""
    For Each tz In tzSet
        GetLocalTimeZoneName = tz.StandardName
        Exit For
    Next
End Function

'------------------------------------------------------------------------------
'# Clipboard
'------------------------------------------------------------------------------
''
' クリップボードの値を取得します。
' 画像等テキストでないものが入っている場合は空文字を返します。
'
Public Function GetClipboard() As String
    GetClipboard = ""
    With CreateObject("Forms.TextBox.1")
        .MultiLine = True
        If .CanPaste Then
            .Paste
            GetClipboard = .text
        End If
    End With
End Function

''
' クリップボードにテキストを設定します。
'
Public Sub SetClipboard(text As String)
    With CreateObject("Forms.TextBox.1")
        .MultiLine = True
        .text = text
        .SelStart = 0
        .SelLength = .TextLength
        .Copy
    End With
End Sub

''
' クリップボードが持っている値の形式を返します。
' https://docs.microsoft.com/ja-jp/office/vba/api/excel.xlclipboardformat
'
Public Function GetClipboardFormat() As Long
    Dim fmt As Variant
    fmt = Application.ClipboardFormats
    GetClipboardFormat = fmt(1)
End Function

''
' クリップボードが値を持っているかを返します。
'
Public Function HasClipboardValue() As Boolean
    HasClipboardValue = True
    If GetClipboardFormat() = -1 Then
        HasClipboardValue = False
    End If
End Function

'------------------------------------------------------------------------------
'# HTTP
'------------------------------------------------------------------------------
Public Function HttpRequest(url As String, Optional method As String = "GET", Optional data As String = "") As Object
    Dim q As String
    If method = "GET" And data <> "" Then
        q = "?" & data
        url = url & q
    End If

    Dim httpReq As Object
    Set httpReq = CreateObject("MSXML2.XMLHTTP")
    httpReq.Open method, url

    httpReq.setRequestHeader "If-Modified-Since", "Thu, 01 Jun 1970 00:00:00 GMT"

    If method = "POST" And data <> "" Then
        httpReq.setRequestHeader "Content-Type", "application/x-www-form-urlencoded"
        httpReq.Send data
    Else
        httpReq.Send
    End If

    Do While httpReq.readyState < 4
        DoEvents
    Loop

    Set HttpRequest = httpReq
End Function

'------------------------------------------------------------------------------
'# Debug
'------------------------------------------------------------------------------
''
' イミディエイトウィンドウにログ出力します。
'
Public Sub DbgLog(v As Variant, Optional dumpBytes As Boolean = False)
    Dim t As String
    t = "[" & Format(Now, "yyyy-mm-dd hh:mm:ss") & "] "
    Dim s As String
    Dim i As Long
    Dim vType As Integer
    Dim arrSize As Long
    vType = VarType(v)
    arrSize = -1
    On Error Resume Next
    If IsEmptyArray(v) Then
        arrSize = 0
    Else
        arrSize = UBound(v) - LBound(v) + 1
    End If
    On Error GoTo 0
    If arrSize > 0 Then
        ' Array Dump
        For i = LBound(v) To UBound(v)
            Debug.Print t & "(" & i & ") = " & v(i)
        Next i
    Else
        If vType = 9 Then
            s = "Nothing"
        Else
            s = v
        End If
        If dumpBytes Then
            ' String Byte Array Dump
            Dim h As String
            Dim b() As Byte
            b = s
            s = ""
            For i = 0 To UBound(b)
                If i > 0 Then
                    s = s & " "
                End If
                h = Hex(b(i))
                If Len(h) = 1 Then
                    s = s & "0"
                End If
                s = s & h
            Next
        End If
        Debug.Print t & s
    End If
End Sub
