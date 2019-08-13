package net.xethyrion.UserDatabase


import net.xethyrion.DeckDatabase.Cards.Deck
import net.xethyrion.UserDatabase.Shop.Purchases
import net.xethyrion.Utilities.Log.*
import net.xethyrion.Utilities.Validation
import java.io.File

class UserDB(): Validation()
{
    var Decks = ArrayList<Deck>()

    private var Users = ArrayList<User>()
    private var count = 0
    private fun alreadyExists(name: String, email: String): Int {
        for(i in Users.indices)
        {
            if(Users[i].Email == email){return -2}
            if(Users[i].Name == name){return -1}
        }
        return 1
    }
    private fun loadUser(Usr: User){count++; Users.add(Usr);}
    private fun writeUser(U: User){File("Users.db").appendText("${U.ID} ${U.Name} ${U.Email} ${U.Purchases}\n") }
    private fun find_email(S: String): Int {
        for(U in Users.indices)
        {
            if(Users[U].Email == S){return U}
        }
        return -1
    }

/** Authenticate(Email)
 *  @Email: String(Email)
 *  Returns: User
 *  Throws: (EXCEPTION)
 *      Bad Email String---------> When regex doesn't match the Email
 *      Authentication Failed----> When there's no such User in UserDB
 */ fun Authenticate(Email: String): User {
        if(!Email.isEmail()){Log(LOG_ERROR, "UserDB/Authenticate: Bad Email String ($Email)"); throw Exception("Authentication: Bad Email String");}
        var result = find_email(Email)
        if(result == -1){Log(LOG_ERROR, "UserDB/Authentication Failed! ($Email)"); throw Exception("Authentication Failed")}
        return Users[result]
    }

/** Register(DName, DEmail)
 *  @DName: String (Desired Username)
 *  @DEmail: String (Desired Email)
 *  Returns: (INT)
 *      1 if registration is successful
 *     -1 if name is taken
 *     -2 if email is taken
 *  Throws: (EXCEPTION)
 *  Bad Name String passed
 *  Bad Email String passed
 */ fun Register(DName:String, DEmail:String): Int {
        val exists = alreadyExists(DName, DEmail)
        if(exists != 1) return exists//-1 = Name exists/-2 Email Exists
        var NewUsr = try{User(count, DName, DEmail, Purchases())}catch(E: Exception){println("UserDB->Register->User(): $E");Log(LOG_ERROR, "UserDB->Register->User(): $E"); return -3; null}
        if(NewUsr != null){loadUser(NewUsr); writeUser(NewUsr)}
        return 1
    }

    private fun load()
    {
        var DBFile = File("Users.db")
        if(!DBFile.exists()){DBFile.createNewFile()}
        if(DBFile.exists() && DBFile.isFile && DBFile.canRead())
        {
            if(DBFile.length()>0)
            {
                DBFile.forEachLine {
                    loadUser(User(it))
                }
            }
        }
    }
    init{load()}
}