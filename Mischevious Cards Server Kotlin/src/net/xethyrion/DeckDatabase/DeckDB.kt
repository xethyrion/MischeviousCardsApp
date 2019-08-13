package net.xethyrion.DeckDatabase

import net.xethyrion.DeckDatabase.Cards.Deck
import net.xethyrion.UserDatabase.User
import java.io.File
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class DeckDB
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
        var Success = true
        try {
            F.writeText("$d\n")
        }catch (E:Exception){Success=false; throw(Exception("Can't interact with Decks.DB"));}
        if(Success){Decks.add(d)}
    }
    fun rand(from: Int, to: Int) : Int {
        return Random().nextInt(to - from) + from
    }
    fun getCards(AvailableDecks: ArrayList<String>, PreviousSelections: ArrayList<ArrayList<ArrayList<Int>>>? = null,Main: Boolean = false): ArrayList<ArrayList<Int>> {
        var prevSelections = PreviousSelections
        var count = 0
        var resultingdeckpositions = ArrayList<Int>()
        var resultingcardpositions = ArrayList<Int>()
        while(count < 10)
        {
            var currentDeck = rand(0,AvailableDecks.size); Decks.forEachIndexed { _deckindex, _deck -> if(_deck.ID == AvailableDecks[currentDeck]){currentDeck = _deckindex}}
            var currentCard = if(Main){rand(0,Decks[currentDeck].MainCards.size)}else{rand(0,Decks[currentDeck].SecondaryCards.size)}
            //Disallow Repetition
            var badcard = false
            prevSelections?.forEach { UserCards ->for(i in UserCards[0].indices){if(UserCards[0][i] == currentDeck && UserCards[1][i] == currentCard){badcard = true;break}}}
            if(!resultingcardpositions.isEmpty()){for(i in resultingcardpositions.indices){if(resultingdeckpositions[i] == currentDeck && resultingcardpositions[i] == currentCard){badcard = true;break}}}
            if(badcard){continue}
            resultingdeckpositions.add(currentDeck)
            resultingcardpositions.add(currentCard)
            count++
        }
        return arrayListOf(resultingdeckpositions, resultingcardpositions)

    }
    fun SelectCards(Users: ArrayList<User>): ArrayList<java.util.ArrayList<out java.util.ArrayList<out Serializable>>> {
        var AvailableDecks = ArrayList<String>(); Users.forEach { usr-> usr.Purchases!!.getDecks().forEach { Deck -> if(!AvailableDecks.contains(Deck)) AvailableDecks.add(Deck) }}
        var MainCards = getCards(AvailableDecks, null, true)
        var SecondaryCards = arrayListOf<ArrayList<ArrayList<Int>>>()
        Users.forEach{ _ -> SecondaryCards.add(getCards(AvailableDecks, SecondaryCards))}
        return arrayListOf(MainCards,SecondaryCards)
    }
    init{
        load()
    }
}