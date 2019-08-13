package net.xethyrion.Server

import net.xethyrion.UserDatabase.User

class Room
{
    var Clients = arrayListOf(Client())
    var count = 0
    var InUse = false
    var Open = false
    var ID = -1
    var ClientCmdBuffer = ArrayList<String>()
    var InnerSemaphore = Semaphore()
    fun Receive_Command(FromClient: Client, cmd: String)
    {
        var which = 0
        Clients.forEachIndexed { IDX, CL -> if(CL.Usr!!.Email==FromClient.Usr!!.Email){which = IDX;}}
        InnerSemaphore.add_thread(Thread(Runnable { ClientCmdBuffer.add("$which cmd") }))
    }
    fun Close()
    {
        println("Trying to close room\n" +
                "Players in room: $count")
        if(count == 0)
        {
            Clients.clear()
            InUse = false
            Open = false
            ID = -1
        }
        else{throw(Exception("Tried To Close Room while players were still in it!")) }
    }
    fun add_client(Cli: Client)
    {
        if(Open && Clients.size < 8)
        {
            Clients.add(Cli)
            Cli.isInRoom = true
            if(Clients.isNotEmpty()) {
                for (cl in Clients) {
                    if(cl.OUT != null)
                    {
                        if(cl.Usr!!.Name != Cli.Usr!!.Name){Cli.OUT!!.println("PJ ${cl.Usr!!.Name}")}
                        cl.OUT!!.println("PJ ${Cli.Usr!!.Name}")
                    }
                }
            }
            else
            {
                Cli.OUT!!.println("PJ ${Cli.Usr!!.Name}")
            }
            count++
        }
        else
        {
            Cli.OUT!!.println("RF")//room is full cannot join
        }
    }
    fun remove_client(Cli: Client)
    {
        Clients.remove(Cli)
        count--
        if(count==0)
        {
            Close()
        }
        else
        {
            for(Cl in Clients)
            {
                if(Cl.OUT == null){println("Client OUTPUT for ${Cl.Usr!!.Name} IS NULL!!!!")}
                else {
                    Cl.OUT?.println("PL ${Cli.Usr!!.Name}")
                }
            }
        }
        Cli.isInRoom = false
    }

    fun start_game()
    {
        Open = false
        var CurrentUsers = ArrayList<User>()
        Clients.forEach { Client-> CurrentUsers.add(Client.Usr!!)}
        var GC = DDB.SelectCards(CurrentUsers)
        var MCit = 0; fun nextMcard(): Int { return MCit++ }
        Clients.forEachIndexed{ idex, cl ->  cl.OUT?.println("GS ${GC[0][0][MCit]} ${GC[0][1][MCit]} P ${GC[1][0][idex]} C ${GC[1][1][idex]}")}
        Thread{
            var GameOver = false
            while(!GameOver)
            {
                Thread.sleep(1000 * 30)
                Clients.forEach { cl-> cl.OUT?.println("MC ${nextMcard()}") }
            }
        }.start()
    }

}