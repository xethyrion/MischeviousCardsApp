package net.xethyrion.Server

class Semaphore{
    private var Threads = ArrayList<Thread>()
    var ClearingThreads = false
    var LockThreads = false
    fun add_thread(T: Thread)
    {
        while(ClearingThreads){
            Thread.sleep(1)
        }
        LockThreads = true
        Threads.add(T)
        LockThreads = false
    }
    init{
        Thread(Runnable{
            while(true)
            {
                if(!LockThreads && !Threads.isEmpty())
                {
                    if(Threads.first().state == Thread.State.NEW){try{Threads.first().start()}catch(E:Exception){println(E.message)}}
                    if(Threads.first().state == Thread.State.TERMINATED)
                    {
                        ClearingThreads = true
                        Threads.removeAt(0)
                        ClearingThreads = false
                    }
                }
                Thread.sleep(10)
            }
        }).start()
    }
}