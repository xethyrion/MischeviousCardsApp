package net.xethyrion.UserDatabase

fun tests(args: Array<String>) {
    /*
    var Test = User(0,"Alex", "Alex@Gmail.com", Purchases())
    println("${Test.Purchases}")
    var Test2 = User(0, "Alex", "Alex@Gmail.com", Purchases(Test.Purchases.toString()))
    println("${Test2.Purchases}")
    var UTest = User(0, "Alex", "Alex@Gmail.com", Purchases())
    var UTest2 = User(UTest.toString())
    println(UTest2.toString())
    */
    var DB = UserDB()
    println(DB.Register("Xethyrion", "xeth@xstelar.net"))
    println(DB.Register("Whisy", "whisy06@gmail.com"))
    println(DB.Authenticate("xeth@stelar.net"))

}