package com.dawsonsystems.linuxmceagent.externalinterfaces

import org.linuxmce.dce.DCEConnection
import org.linuxmce.dce.MessageHandler
import org.linuxmce.dce.Message
import org.linuxmce.dce.DCE
import com.dawsonsystems.linuxmceagent.Player

/**
 * Allow the DCE world to send messages to the agent.
 * Agent->DCE interaction is managed by DCEServerConnection
 */
class DCEInterface {

  private DCEConnection dceConnection
  private Player player

  public DCEInterface(Player player) {
    this.player = player
    dceConnection = DCEConnection.create("192.168.1.111", "38", new MediaMessageHandler(player))

    dceConnection.connect()
    println "Connected to the LinuxMCE DCE ROUTER"
  }

  //TODO, issue events to the router when we play media etc.
}


class MediaMessageHandler implements MessageHandler {

  Player player

  MediaMessageHandler(Player player) {
    this.player = player
  }

  @Override
  void handleCommand(Message message) {
    try {

    //TODO, implement me now!!
    println "Doing...."
    switch(message.id) {
      //case DCE.Command.CMD_PLAY_MEDIA.value:
      case 37:
        //boo, conversion to groovy has shagged the type inference.  need to improve/ rewrite
          //println "Play Media : ${message.getParameterValue(DCE.CommandParam.CMD_PARAM_MEDIAURL.value)}"

        String url = message.getParameterValue(59)

        println "Play Media : ${url}"
        player.play(url)

        break
        default:
          println "UNKNOWN MESSAGE FROM ROUTER: ${message}"
    }
    } catch (Exception ex) {
      ex.printStackTrace()
      throw ex
    }

  }
}
