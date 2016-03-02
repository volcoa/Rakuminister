package util

import scala.collection.mutable.ListBuffer

object WSCall {

  var serversList = List[String]()
  var serverIndex = 0

  def createServersList(serverListFilePath: String): Unit ={
    var servers = new ListBuffer[String]()
    for (serverLine <- scala.io.Source.fromFile(serverListFilePath).getLines()) {
      servers += serverLine
    }
    serversList = servers.toList
  }

  def getNextServer(): String = {
    if(serverIndex < serversList.length-1){
      serverIndex = serverIndex + 1
    }else {
      serverIndex = 0
    }
    var server = serversList(0)
    try {
      server = serversList(serverIndex)
    } catch {
      case e: Exception => server = serversList(0)
    }
    return server
  }
}
