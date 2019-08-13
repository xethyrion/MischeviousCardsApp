package net.xethyrion.UserDatabase.Shop
class Purchases()
{
    enum class Functionality(var Aquired: Boolean)
    {
        Premium(false) // No Ads
    }
    enum class Decks(var Aquired: Boolean)
    {
        Classic(Aquired = true)
    }
    enum class Skins(var Aquired: Boolean)
    {
        Classic(false), //Classic Skin
        Shadow(false)   //Shadow  Skin
    }
    enum class Products(var Count: Int)
    {
        Wildcard(0) //How many wildcards
    }

    override fun toString(): String
    {
        var FinalResult = ""
        FinalResult += "<P "
        FinalResult += "F "
        for(i in Functionality.values()){FinalResult += if(i.Aquired){"t "}else{"f "}}
        FinalResult += "D "
        for(i in Decks.values()){FinalResult += if(i.Aquired){"t "}else{"f "}}
        FinalResult += "S "
        for(i in Skins.values()){FinalResult += if(i.Aquired){"t "}else{"f "}}
        FinalResult += "P "
        for(i in Products.values()){FinalResult+="${i.Count} "}
        FinalResult += ">"
        return FinalResult
    }
    constructor(x: String) : this()
    {
        fun String.isNumber(): String {
            try{this.toInt(); return "Int"}catch(E:Exception){}
            return this
        }
        var which = ""
        var Fu = 0
        var Dk = 0
        var Sk = 0
        var Pr = 0
        var xarr = x.split(" ")
  loop@ for(S in xarr)
        {
            when(S.isNumber())
            {
                "<P" -> {continue@loop}
                 "F" -> {which = S}
                 "D" -> {which = S}
                 "S" -> {which = S}
                 "P" -> {which = S}
              "t" -> {
                            when(which)
                            {
                                "F"->{Functionality.values()[Fu].Aquired = true; Fu++}
                                "D"->{Decks.values()[Dk].Aquired = true; Dk++}
                                "S"->{Skins.values()[Sk].Aquired = true; Sk++}
                            }
                        }
             "f" -> {
                             when(which)
                             {
                                 "F"->{Fu++}
                                 "D"->{Dk++}
                                 "S"->{Sk++}
                             }
                        }
                "Int"-> {
                             var Cnt = S.toInt()
                             when(which)
                             {
                                 "P"->{Products.values()[Pr].Count = Cnt; Pr++}
                             }
                        }
                ">" -> {break@loop;}
                else -> {
                    throw(Exception("Illegal options passed to purchases secondary Constructor!"))
                }
            }
        }
    }
    fun buyDeck(Name: String): Boolean
    {
        Decks.values().forEach {
            if(it.name == Name)
            {
                it.Aquired = true
                return true
            }
        }
        return false
    }
    fun getDecks(): ArrayList<String> {
        var result = ArrayList<String>()
        Decks.values().forEach {
            if(it.Aquired)result.add(it.name) }
        return result
    }
}