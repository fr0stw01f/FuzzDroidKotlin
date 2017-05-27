package de.tu_darmstadt.sse.sharedclasses.networkconnection


object NetworkConnectionInitiator {

    var syncToken = Any()
    var serverCommunicator: ServerCommunicator? = null
        private set

    fun initNetworkConnection() {
        serverCommunicator = ServerCommunicator(syncToken)
    }
}
