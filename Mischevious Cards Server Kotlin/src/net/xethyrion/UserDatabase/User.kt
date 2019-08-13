package net.xethyrion.UserDatabase

import net.xethyrion.UserDatabase.Shop.Purchases
import net.xethyrion.Utilities.Validation

class User private constructor() : Validation()
{
    var ID = -1
    var Name: String = ""
    var Email: String = ""
    var Purchases: Purchases? = null
    override fun toString(): String
    {
        var result = ""
        result += "$ID "
        result += "$Name "
        result += "$Email "
        result += Purchases.toString()
        return result
    }
    constructor(UserString: String): this()
    {
        var Sarr = UserString.split(" ")
        var PurchaseS = false
        var PurchaseF = false
        var PurchasesString = ""
        loop@ for(S in Sarr)
        {
            when(S)
            {
                "<P"->{PurchaseS = true; PurchasesString+="$S "}
                else->{
                        if(ID==-1){try{ID = S.toInt(); continue@loop}catch(E:Exception){}}
                        if(Name.isEmpty()){Name = S; continue@loop}
                        if(Email.isEmpty()){Email = S; continue@loop}
                        if(S==">"){PurchasesString+="$S "; PurchaseF = true; PurchaseS = false ; Purchases= Purchases(PurchasesString); continue@loop}
                        if(PurchaseS){PurchasesString+="$S "; continue@loop}
                        if(!PurchaseF && !PurchaseS){throw(Exception("User: Invalid String Format, $S can't be processed."))}
                }
            }
        }
        if(ID == -1 || Name.isEmpty() || Email.isEmpty() || Purchases == null){throw(Exception("User: Invalid String Format!"))}
    }
    constructor(id: Int, name: String, email: String, purchases: Purchases): this()
    {
        if(name.isName()){Name=name}
        else{throw(Exception("Invalid Name!"))}
        if(email.isEmail()){Email=email}
        else{throw(Exception("Invalid Email!"))}
        Purchases = purchases
        ID = id
    }
}