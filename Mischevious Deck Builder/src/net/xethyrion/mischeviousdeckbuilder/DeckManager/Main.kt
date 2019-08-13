package net.xethyrion.mischeviousdeckbuilder.DeckManager

import net.xethyrion.mischeviousdeckbuilder.DeckDatabase.Cards.Deck
import net.xethyrion.mischeviousdeckbuilder.DeckDatabase.Cards.MainCard
import net.xethyrion.mischeviousdeckbuilder.DeckDatabase.Cards.SecondCard
import net.xethyrion.mischeviousdeckbuilder.DeckDatabase.Database

var DB = Database()
fun addMainCard(): MainCard {
    print("\u001b[H\u001b[2J")
    println("Content: ")
    var C = readLine()
    println("Entries: ")
    var E = readLine()!!.toInt()
    return MainCard(C!!,E)
}
fun addSecondCard(): SecondCard {
    print("\u001b[H\u001b[2J")
    println("Content: ")
    var C = readLine()
    return SecondCard(C!!, true)
}
fun saveDeck(D: Deck)
{
    DB.new_deck(D)
}
fun listDecks()
{
    println("Mischievous Deck Builder")
    for(Deck in DB.Decks)
    {
        println("Deck: ${Deck.ID}")
        println("Main Cards:")
        Deck.MainCards.forEach{println(it.Content)}
        Deck.SecondaryCards.forEach{println(it.Content)}
    }
}
fun createDeck()
{
    print("\u001b[H\u001b[2J")
    println("Choose deck name:")
    var name = readLine()
    var Deck = Deck(name!!, ArrayList<MainCard>(), ArrayList<SecondCard>())
    var finished = false
    while(!finished)
    {
        print("\u001b[H\u001b[2J")
        println("Deck: $name")
        println("Main Cards:")
        Deck.MainCards.forEach {println(it.Content)}
        println("Secondary Cards:")
        Deck.SecondaryCards.forEach { println(it.Content) }
        println("Would you like to:")
        println("1: Add Main Card")
        println("2: Add Secondary Card")
        println("1: Save Deck")
        println("0: Exit")
        print("Your Choice: ")
        when(readLine()!!.toInt())
        {
            1->Deck.MainCards.add(addMainCard())
            2->Deck.SecondaryCards.add(addSecondCard())
            3->saveDeck(Deck)
            0->finished=true
        }

    }
}
fun lui()
{
    loop@ while(true)
    {
        print("\u001b[H\u001b[2J")
        listDecks()
        println("Your options:")
        println("1.Create Deck")
        println("2.Exit")
        when(readLine()!!.toInt())
        {
            1-> createDeck()
            0-> break@loop
        }
    }
}
fun main(args: Array<String>)
{
    lui()
}