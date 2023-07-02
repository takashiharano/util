Option Explicit

Public Sub EncodeTest()
    Call DoTest("abc", 0, "YWJj")
    Call DoTest("abc", 1, "YGNi")
    Call DoTest("abc", 2, "Y2Bh")
    Call DoTest("abc", 254, "n5yd")
    Call DoTest("abc", 255, "np2c")
    Call DoTest("abc", 256, "YWJj")
    Call DoTest("abc", 257, "YGNi")
    Call DoTest("abc", 510, "n5yd")
    Call DoTest("abc", 511, "np2c")
    Call DoTest("abc", 512, "YWJj")
    Call DoTest("abc", 513, "YGNi")
    
    Call DoTest("あいう", 0, "44GC44GE44GG")
    Call DoTest("あいう", 1, "4oCD4oCF4oCH")
    Call DoTest("あいう", 2, "4YOA4YOG4YOE")
    Call DoTest("あいう", 254, "HX98HX96HX94")
    Call DoTest("あいう", 255, "HH59HH57HH55")
    Call DoTest("あいう", 256, "44GC44GE44GG")
    Call DoTest("あいう", 257, "4oCD4oCF4oCH")
    Call DoTest("あいう", 510, "HX98HX96HX94")
    Call DoTest("あいう", 511, "HH59HH57HH55")
    Call DoTest("あいう", 512, "44GC44GE44GG")
    Call DoTest("あいう", 513, "4oCD4oCF4oCH")
End Sub

Private Sub DoTest(s As String, k As Integer, exp As String)
    Dim r As String
    r = Base64S.EncodeString(s, k)
    Dim res As String
    Dim status As String
    status = "NG"
    If r = exp Then
        status = "OK"
    End If
    res = "[" & status & "] " & s & " -> " & r
    Debug.Print res
End Sub
