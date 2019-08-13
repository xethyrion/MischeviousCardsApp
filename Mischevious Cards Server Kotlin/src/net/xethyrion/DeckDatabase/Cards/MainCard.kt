package net.xethyrion.DeckDatabase.Cards

class MainCard private constructor() : Card() {
    var Entries: Int = -1
    constructor(Content: String, Entries: Int) : this()
    {
        if(Entries<1){throw(Exception("MainCard(Normal Constructor): Invalid Int passed to Entries parameter: Number must be greater than 0!"))}
        if(Content.isEmpty()){throw(Exception("MainCard(Normal Constructor): Empty string passed to Content parameter."))}
        this.Content = Content
        this.Entries = Entries
    }
    constructor(MainCardString: String): this()
    {
        if(MainCardString.isEmpty()){throw(Exception("MainCard(String Constructor): Empty MainCardString passed!"))}
        var arr = MainCardString.split(" ")
        if(arr[0] != "<MC" || arr.size != 4 || arr.last() != ">"){throw(Exception("MainCard(String Constructor): Invalid string format!"))}
        if(arr[1].length>3){Content = replace(arr[1])}else{throw(Exception("MainCard(String Constructor): Invalid String Format! Content must be bigger than 3 letters"))}
        try{Entries = arr[2].toInt()}catch(e: Exception){throw(Exception("MainCard(String Constructor): Invalid string format! Entries is not INT!"))}
    }
    override fun toString() : String {return "<MC ${replace(Content)} $Entries >"}
}