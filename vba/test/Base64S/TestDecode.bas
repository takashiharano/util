Option Explicit

Public Sub EncodeTest()
    Call DoTest("YWJj", 0, "abc")
    Call DoTest("YGNi", 1, "abc")
    Call DoTest("Y2Bh", 2, "abc")
    Call DoTest("n5yd", 254, "abc")
    Call DoTest("np2c", 255, "abc")
    Call DoTest("YWJj", 256, "abc")
    Call DoTest("YGNi", 257, "abc")
    Call DoTest("n5yd", 510, "abc")
    Call DoTest("np2c", 511, "abc")
    Call DoTest("YWJj", 512, "abc")
    Call DoTest("YGNi", 513, "abc")

    Call DoTest("44GC44GE44GG", 0, "あいう")
    Call DoTest("4oCD4oCF4oCH", 1, "あいう")
    Call DoTest("4YOA4YOG4YOE", 2, "あいう")
    Call DoTest("HX98HX96HX94", 254, "あいう")
    Call DoTest("HH59HH57HH55", 255, "あいう")
    Call DoTest("44GC44GE44GG", 256, "あいう")
    Call DoTest("4oCD4oCF4oCH", 257, "あいう")
    Call DoTest("HX98HX96HX94", 510, "あいう")
    Call DoTest("HH59HH57HH55", 511, "あいう")
    Call DoTest("44GC44GE44GG", 512, "あいう")
    Call DoTest("4oCD4oCF4oCH", 513, "あいう")
End Sub

Private Sub DoTest(s As String, k As Integer, exp As String)
    Dim r As String
    r = Base64S.DecodeString(s, k)
    Dim res As String
    Dim status As String
    status = "NG"
    If r = exp Then
        status = "OK"
    End If
    res = "[" & status & "] " & s & " -> " & r
    Debug.Print res
End Sub
