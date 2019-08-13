package net.xethyrion.Utilities

open class Validation{
    fun String.isName(): Boolean
    {
        if(this.matches("^[a-zA-Z0-9]{3,10}+$".toRegex())){return true}
        return false
    }
    fun String.isEmail(): Boolean
    {
        fun repeats(S: String): Boolean {
            for(s in S.indices)
            {
                if(s != S.length-1)
                {
                    if((S[s] == '-' && S[s+1] == '-') ||
                       (S[s] == '_' && S[s+1] == '_') ||
                       (S[s] == '.' && S[s+1] == '.') ||
                       (S[s] == '.' && S[s+1] == '_') ||
                       (S[s] == '.' && S[s+1] == '-') ||
                       (S[s] == '-' && S[s+1] == '_') ||
                       (S[s] == '_' && S[s+1] == '-')){return true}

                }
            }
            return false
        }
        if(this.matches("^[A-Z0-9a-z.\\-_]{3,64}+[@]+[a-zA-Z0-9]{3,64}+([\\.]?([a-zA-Z0-9]{2,4})+){1,2}".toRegex()) && !repeats(this)){return true}
        return false
    }
}