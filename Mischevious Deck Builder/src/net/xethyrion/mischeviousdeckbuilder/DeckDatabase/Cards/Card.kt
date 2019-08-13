package net.xethyrion.mischeviousdeckbuilder.DeckDatabase.Cards

open class Card {
    var Content = ""
    protected fun replace(Str: String): String {
        val meh = Str
        if(meh.contains("\\s".toRegex())){ return Str.replace("\\s".toRegex(), "-")}
        else{return meh.replace("-".toRegex(), " ")}
    }
}