package com.dawsonsystems.linuxmceagent.externalinterfaces

import com.dawsonsystems.linuxmceagent.Player
import java.awt.SystemTray
import java.awt.PopupMenu
import java.awt.TrayIcon
import java.awt.MenuItem
import java.awt.CheckboxMenuItem
import java.awt.Menu
import java.awt.AWTException
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.awt.Image
import javax.swing.JOptionPane
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JMenu
import javax.swing.JPopupMenu
import javax.swing.JMenuItem
import java.awt.Component
import com.dawsonsystems.linuxmceagent.helper.JXTrayIcon
import com.dawsonsystems.linuxmceagent.LinuxMCEAgent

/**
 * Manages the desktop integration to allow right click stuff to work somehow..
 *
 * Might need more than one for each desktop..
 */
class DesktopInterface {

  private Player player
  private LinuxMCEAgent agent
  private TrayIcon trayIcon

  DesktopInterface(Player player, LinuxMCEAgent agent) {
    this.player = player
    this.agent = agent

    setupSystemTray()
  }

  private setupSystemTray() {
    if (!SystemTray.isSupported()) {
      System.out.println("SystemTray is not supported");
      return;
    }
    final PopupMenu popup = new PopupMenu();
    trayIcon=
      new TrayIcon(loadImage("images/linuxmcelogo.png"));
    final SystemTray tray = SystemTray.getSystemTray();

    // Create a pop-up menu components
    MenuItem orbiterItem = new MenuItem("Orbiter");
    MenuItem exitItem = new MenuItem("Exit");

    //Add components to pop-up menu
    popup.add(orbiterItem);
    popup.addSeparator();
    popup.add(exitItem);

    exitItem.addActionListener(new ActionListener() {
      void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        agent.close()
      }
    })

    trayIcon.setPopupMenu(popup);

    try {
      tray.add(trayIcon);
    } catch (AWTException e) {
      System.out.println("TrayIcon could not be added.");
    }
  }

  void close() {
    SystemTray.getSystemTray().remove(trayIcon)
  }

  static Image loadImage(String file) {
    BufferedImage img = null;
    try {
      img = ImageIO.read(ClassLoader.getSystemClassLoader().getResource(file));
    } catch (IOException e) {
      e.printStackTrace()
    }
    return img
  }
}
