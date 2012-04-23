package com.dawsonsystems.linuxmceagent

/**
 *
 */
interface Player {
  void play(String url)
  //Stop the currently displaying media. Will hide the display screen.
  void stop()

}
