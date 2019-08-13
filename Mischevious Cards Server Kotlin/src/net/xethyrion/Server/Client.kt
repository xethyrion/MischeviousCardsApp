package net.xethyrion.Server

import net.xethyrion.UserDatabase.User
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.util.*

class Client
{
    var Running = false
    var CThread: Thread? = null
    var sock: Socket? = null
    var OUT: PrintWriter? = null
    var IN: Scanner? = null
    var isInRoom = false
    var whichroom = -1
    var Usr: User? = null
    private fun Command_Receive(cmd: String): Boolean {
        var cmdarr = cmd.split(" ")
        when(cmdarr[0])
        {
            "S"->{
                var msg = ""
                if(cmdarr.size>1)
                {
                    var firstword = true
                    for (word in cmdarr) {
                        if (firstword){firstword = false; continue }
                        msg += " $word"
                    }
                    println("Said: $msg")
                }
            } // Useless say command
            "H"->{
                if(!isInRoom)
                {
                    var TH = Thread(Runnable{
                        for(i in 0..199)
                        {
                            if(!(GServ!!.Rooms[i].InUse))
                            {
                                isInRoom = true
                                GServ!!.Rooms[i].InUse = true
                                GServ!!.Rooms[i].Open = true
                                GServ!!.Rooms[i].add_client(this)
                                GServ!!.Rooms[i].ID = i
                                println("Created room with id: $i")
                                OUT!!.println("H ${i}")
                                whichroom = i
                                break
                            }
                        }
                    })
                    GRoomSem.add_thread(TH)
                }
                else
                {
                    Running=false
                }
            } //Host Room
            "J"->{
                if(!isInRoom) {
                    var TH = Thread(Runnable {
                        if (cmdarr.size != 2) {
                            Running = false
                        }
                        println("${this.Usr!!.Name}, Trying to join room ${cmdarr[1]}")
                        var id = cmdarr[1].toInt()
                        var found = false
                        if (GServ!!.Rooms[id].InUse && GServ!!.Rooms[id].Open) {
                            GServ!!.Rooms[id].add_client(this)
                            whichroom = id
                            OUT!!.println("J $whichroom")
                            found = true
                        }
                        if (found && !(GServ!!.Rooms[id].Open)) {
                            this.OUT!!.println("J -2")
                        }//GAME ALREADY STARTED
                        if (!found) {
                            this.OUT!!.println("J -1")
                        }//NO SUCH ROOM
                    })
                    GRoomSem.add_thread(TH)
                }
                else
                {
                    Running=false
                }
            } //Join Room
            "LR"->{
                var TH = Thread(Runnable{
                    if(cmdarr.size != 1){Running =  false}
                    if(isInRoom){
                        println("${this.Usr!!.Name}, leaving room $whichroom")
                        GServ!!.Rooms[whichroom].remove_client(this)
                    }

                })
                GRoomSem.add_thread(TH)
            } //Leave Room
            else -> {
                Running = false
            }
        }
        return true
    }
    fun Start(cli: Socket, usr: User)
    {
        Usr = usr
        sock = cli
        Running = true
        CThread = Thread(Runnable{
            OUT = PrintWriter(BufferedWriter(OutputStreamWriter(sock!!.getOutputStream())), true)
            IN  = Scanner(sock!!.getInputStream())
            while (Running) {
                try{Command_Receive(IN!!.nextLine())}catch(E: Exception){println("Client Disconnected");Command_Receive("LR");while(isInRoom){
                    Thread.sleep(1000)
                }; Running=false}
                OUT!!.println("Received!")
            }
            if(isInRoom){Command_Receive("LR")}
            while(isInRoom){
                Thread.sleep(1000)
            }
            Usr = null
            sock = null
            CThread = null
            OUT = null
            IN = null
            isInRoom = false
            Running = false
            println("Closed this client.")
        })
        CThread!!.start()
    }
    fun Stop()
    {
        Running = false
    }
}