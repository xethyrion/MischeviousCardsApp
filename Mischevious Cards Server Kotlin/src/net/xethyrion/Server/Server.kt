package net.xethyrion.Server

import net.xethyrion.UserDatabase.User
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.util.*
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import kotlin.collections.ArrayList


class Server(Port: Int)
{
    private var Running = true
    private var Server: ServerSocket? = null
    private var PORT = Port
    private var Clients = ArrayList<Client>()
    var Rooms = ArrayList<Room>()
    private fun load_clients()
    {
        for(i in 0..999)
        {
            Clients.add(Client())
        }
    }
    private fun load_rooms()
    {
        for(i in 0..199)
        {
            Rooms.add(Room())
        }
    }
    private fun Find_Spot(): Int {
        for(i in 0..999)
        {
            if(!Clients[i].Running){Clients[i].Running = true;return i}
        }
        return -1
    }
    private fun googleVerifyToken(input: String): GoogleIdToken? {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        var Verifier = GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .setAudience(Collections.singletonList("621125505135-fe0rcmfvju48r7kl7u4k8nqmqddam2bs.apps.googleusercontent.com"))
                .build()
        val idToken = Verifier.verify(input)
        return idToken
    }
    private fun register_google(cmdarr: List<String>): Int {
        println("TOKEN IS: ${cmdarr[2]}")
        var idToken = googleVerifyToken(cmdarr[2])
        println("idTOKEN IS: ${idToken}")
        if(idToken == null){println("UNOFFICIAL DETECTED!!! TOKEN IS NULL"); return -666}
        else{
            val payload = idToken.payload
            var thisemail = payload.email
            var res = DB.Register(cmdarr[1], thisemail)
            return res
        }
    }
    fun Start()
    {
        load_clients()
        load_rooms()
        Server = ServerSocket()
        Server!!.reuseAddress = true
        Server!!.bind(InetSocketAddress(PORT))
        while(Running)
        {
            var UnProcessedClient = Server!!.accept()
            UnProcessedClient.reuseAddress = false
            var x = Find_Spot()
            var Th = Thread(Runnable {   //Authentication Thread
                var client = UnProcessedClient
                println("Incoming Connection from ${client.inetAddress.hostAddress}")

                var Authenticated = false
                var Registering = false
                client.soTimeout = 5000
                var cmdarr: List<String>? = null
                var IN  = Scanner(client!!.getInputStream())
                var OUT = PrintWriter(BufferedWriter(OutputStreamWriter(client!!.getOutputStream())), true)
                var UsrRes: User? = null
                try
                {
                    cmdarr = IN.nextLine().split(" ")
                    if(cmdarr[0] != "A" && cmdarr[0] != "R"){/*Todo: Ban Client*/println("UNOFFICIAL DETECTED"); client.close(); Clients[x].Running = false}
                    else
                    {
                        client.soTimeout = 0;
                        when(cmdarr[0])
                        {
                            "R"->{
                                try{
                                    var Result = register_google(cmdarr)
                                    Registering = true
                                    if(Result == -666){client.close()}
                                    else{OUT.println("R ${Result}")}
                                }catch(E:Exception){println("UNOFFICIAL DETECTED: EXCEPTION: $E, ${E.message}"); client.close(); Clients[x].Running = false /*Todo: Ban Client*/}
                            }
                            "A"->{
                                try{
                                    var idToken = googleVerifyToken(cmdarr[1])
                                    if(idToken!=null) {
                                        UsrRes = DB.Authenticate(idToken.payload.email);
                                        Authenticated = true
                                    }
                                }catch(E:Exception){println("UNOFFICIAL DETECTED"); client.close(); Clients[x].Running = false/*Todo: Ban Client*/}
                            }
                        }
                    }
                }
                catch(e: Exception){println("Client did not authenticate in time.")}
                if(x != -1 && Authenticated){
                    println("${cmdarr!![1]} Authenticated successfully!")
                    OUT.println("AS $UsrRes")
                    Clients[x].Start(client, UsrRes as User) // Clients run on their own thread so this thread basically closes itself after authentication :)
                }
                else if(x!=-1 && !Authenticated)
                {
                    if(!Registering){OUT.println("AF")};client.close()
                }
                else{OUT.println("SF");client.close()}
            })
            GAuthSem.add_thread(Th)
        }
        for(client in Clients)
        {
            client.Running = false
        }

    }
}