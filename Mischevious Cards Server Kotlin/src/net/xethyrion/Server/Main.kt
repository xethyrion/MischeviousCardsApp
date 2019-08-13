package net.xethyrion.Server

import net.xethyrion.DeckDatabase.DeckDB
import net.xethyrion.UserDatabase.Shop.Purchases
import net.xethyrion.UserDatabase.User
import net.xethyrion.UserDatabase.UserDB

var GServ: Server? = null
var GRoomSem = Semaphore()
var GAuthSem = Semaphore()
var DB = UserDB()
var DDB = DeckDB()

fun main(args: Array<String>) {
    var GameCards = DDB.SelectCards(arrayListOf(
            User(0, "Alex", "whatever@gmail.com", Purchases()),
            User(1, "Castor", "whatever@gmail.com", Purchases()),
            User(2, "Simo", "whisy@gmail.com", Purchases())))
    var MCards = GameCards[0]
    var SCards = GameCards[1]

    println("Main Cards: $MCards")
    println("Secondary: $SCards")
    //GServ = Server(6660)
    //println("Starting server..")
    //GServ!!.Start()

}