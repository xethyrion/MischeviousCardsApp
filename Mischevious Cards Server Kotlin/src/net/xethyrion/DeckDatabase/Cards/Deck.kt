package net.xethyrion.DeckDatabase.Cards

class Deck private constructor()
{
    var ID = ""
    var MainCards = ArrayList<MainCard>()
    var SecondaryCards = ArrayList<SecondCard>()
    constructor(ID: String, Maincards: ArrayList<MainCard>, Secondarycards: ArrayList<SecondCard>):this()
    {
        //if(Maincards.isEmpty() || Secondarycards.isEmpty()){throw(Exception("Deck(Normal Constructor): Maincards or Secondarycards empty!"))}
        if(ID.isEmpty()){throw(Exception("Deck(Normal Constructor: Id String is empty!"))}
        this.ID = ID
        this.MainCards = Maincards
        this.SecondaryCards = Secondarycards
    }
    constructor(DeckString: String) : this()
    {
        if(DeckString.isEmpty()){throw(Exception("Deck(String Constructor): Deck String is empty"))}
        var arr = DeckString.split(" ")
        if(arr.first() != "<D" || arr.last() != ">" || arr.size <2){throw(Exception("Deck(String Constructor): Invalid Format!"))}
        ID = arr[1]
        var main = false
        var second = false
        var tempstr = ""
        for(i in arr.indices)
        {
            if(i<1){continue}
            if(arr[i]=="<MC"){tempstr+="${arr[i]} "; main = true; continue}
            if(arr[i]=="<SC"){tempstr+="${arr[i]} "; second = true; continue}
            if(arr[i]==">" && main){tempstr+=arr[i]; MainCards.add(MainCard(tempstr)); main = false; tempstr=""; continue}
            if(arr[i]==">" && second){tempstr+=arr[i]; SecondaryCards.add(SecondCard(tempstr)); second = false; tempstr=""; continue}
            if(main || second){tempstr+="${arr[i]} "; continue}
            if(arr[i] == ">"){break}
        }
    }
    override fun toString(): String {
        var result = ""
        result+="<D "
        result+="$ID "
        for(M in MainCards){result+="$M "}
        for(S in SecondaryCards){result+="$S "}
        result+=">"
        return result
    }
}