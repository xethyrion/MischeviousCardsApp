package net.xethyrion.Utilities.Log

import java.io.File
import java.time.LocalDateTime

val LOG_ERROR = 0
val LOG_WARNING = 1
val LOG_PURCHASE = 2
val LOG_PURCHASE_ERR = 2
fun Log(Type: Int, Message:String)
{
    when(Type)
    {
        LOG_ERROR ->  {println("ERROR: $Message");            File("Logs/Error.log").appendText("${LocalDateTime.now()} - $Message\n")}
        LOG_WARNING ->  {println("WARNING: $Message");          File("Logs/Warning.log").appendText("${LocalDateTime.now()} - $Message\n")}
        LOG_PURCHASE ->  {println("PURCHASE: $Message");         File("Logs/Purchase.log").appendText("${LocalDateTime.now()} - $Message\n")}
        LOG_PURCHASE_ERR -> {println("PURCHASE ERROR: $Message");   File("Logs/Purchase_Error.log").appendText("${LocalDateTime.now()} - $Message\n")}
        else->throw(Exception("No type passed to Log Function!"))
    }
}