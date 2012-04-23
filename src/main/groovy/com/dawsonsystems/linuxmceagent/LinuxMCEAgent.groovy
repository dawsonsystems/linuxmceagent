package com.dawsonsystems.linuxmceagent

import uk.co.caprica.vlcj.binding.LibVlc
import uk.co.caprica.vlcj.runtime.RuntimeUtil
import com.sun.jna.Native
import javax.swing.SwingUtilities
import com.sun.jna.NativeLibrary
import com.dawsonsystems.linuxmceagent.externalinterfaces.DCEInterface
import com.dawsonsystems.linuxmceagent.externalinterfaces.UpnpInterface
import com.dawsonsystems.linuxmceagent.externalinterfaces.DesktopInterface

/**
 * Doesn't do much.
 * * Initialise the player
 * * Connect to dcerouter
 * * Boot the various interfaces
 * * Bind them all together.
 *
 * A simple DI container in all but name.
 */
class LinuxMCEAgent {

  static void main(def args) {
    System.setProperty("vlcj.log", "DEBUG")
    NativeLibrary.addSearchPath(
            RuntimeUtil.getLibVlcLibraryName(), "/home/david/libvlc/lib"
    );
    Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

    //Doesnm't affect AWT components, only swing.
    //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    //UIManager.setLookAndFeel(NimbusLookAndFeel.class.name);

    new LinuxMCEAgent(args)
  }

  private Player player
  private DCEServerConnection dceServerConnection
  private DCEInterface dceInterface
  private UpnpInterface upnpInterface
  private DesktopInterface desktopInterface

  private LinuxMCEAgent(String[] args) {

    dceServerConnection = new DCEServerConnection()

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        player = new VlcPlayer()
        desktopInterface = new DesktopInterface(player, LinuxMCEAgent.this)
        upnpInterface = new UpnpInterface(player: player)
        dceInterface = new DCEInterface(player)
        //player.play("/home/david/Media/Camera/prg001/mov002.mpg")
        //player.play("file:///home/david/Media/Pictures/halloween-mortalkombat.jpg")
      }
    });
  }

  public close() {
    //dceServerConnection.close()
    //dceInterface.close()
    //upnpInterface.close()
    player.stop()
    desktopInterface.close()

    System.exit(0)
  }
}
