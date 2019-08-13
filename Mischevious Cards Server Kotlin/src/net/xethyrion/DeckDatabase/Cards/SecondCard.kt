package net.xethyrion.DeckDatabase.Cards

class SecondCard private constructor() : Card(){
    constructor(Content: String, normal: Boolean) : this()
    {
        if(Content.isEmpty()){throw(Exception("SecondCard(Normal Constructor): Empty string passed to Content parameter."))}
        this.Content = Content
    }
    constructor(SecondCardString: String): this()
    {
        if(SecondCardString.isEmpty()){throw(Exception("SecondCard(String Constructor): Empty SecondCardString passed!"))}
        var arr = SecondCardString.split(" ")
        if(arr[0] != "<SC" || arr.size != 3 || arr.last() != ">"){throw(Exception("SecondCard(String Constructor): Invalid string format!"))}
        if(arr[1].length>3){Content = replace(arr[1])}else{throw(Exception("SecondCard(String Constructor): Invalid String Format! Content must be bigger than 3 letters"))}
    }
    override fun toString() : String {return "<SC ${replace(Content)} >"}
}