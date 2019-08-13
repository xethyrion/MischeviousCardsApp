package net.xethyrion.mischeviousdeckbuilder.DeckDatabase

import net.xethyrion.mischeviousdeckbuilder.DeckDatabase.Cards.Deck
import java.io.File

class Database
{
    var F = File("Decks.db")
    var Decks = ArrayList<Deck>()
    private fun load()
    {
        if(!F.exists()){F.createNewFile()}
        if(F.isFile && F.canRead() && F.length() > 0){F.forEachLine{Decks.add(Deck(it))}}
    }
    fun new_deck(d: Deck)
    {
        F.writeText("$d\n")
    }
    init{
        load()
    }
}