package com.dawsonsystems.linuxmceagent

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent
import javax.swing.JFrame
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.MediaPlayer
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
 * Player implemented using VLC and some java2d.
 */
class VlcPlayer implements Player {

  //Yay! pretending to understanding content based on the file extension...
  private static def PICTURE_TYPES = ["png", "jpg", "gif"]
  private static def MOVIE_TYPES = ["mpeg", "mpg"]

  private EmbeddedMediaPlayerComponent mediaPlayerComponent
  private JFrame frame

  @Override
  void play(String url) {

    if (isPicture(url)) {
      showImage(url)
    } else if (isMovie(url)) {
      playMovie(url)
    } else {
      println "Couldn't understand URL $url"
    }
  }

  boolean isPicture(String url) {
    def extension = url.substring(url.lastIndexOf(".") + 1)
    println "Checking $extension"
    PICTURE_TYPES.contains(extension)
  }

  boolean isMovie(String url) {
    def extension = url.substring(url.lastIndexOf(".") + 1)
    println "Checking $extension"
    MOVIE_TYPES.contains(extension)
  }

  void showImage(String url) {
    BufferedImage img = null
    try {
      img = ImageIO.read(new URL(url))

      GraphicsDevice myDevice = GraphicsEnvironment.localGraphicsEnvironment.defaultScreenDevice

      frame = new JFrame("Wibble")
      frame.undecorated = true
      frame.ignoreRepaint = true

      myDevice.fullScreenWindow = frame

      int height, width
      (height, width) = getNewDimensions(img)

      int top, left
      (top, left) = getLocation(height, width)

      frame.graphics.drawImage(img, left, top, width, height, null)

      println "Loaded image $url"

    } catch (IOException ex) {
      println "Failed to show image $url with error ${ex.message}"
      ex.printStackTrace()
    }
  }

  def getLocation(int height, int width) {
    int top = 0
    int left = 0

    if (width < frame.width) {
      left = (frame.width - width) / 2
    }
    if (height < frame.height) {
      top = (frame.height - height) / 2
    }
    return [top, left]
  }

  def getNewDimensions(BufferedImage img) {

    def scale = Math.min(frame.width/img.width, frame.height/img.height)
    def scaledwidth = img.width * scale
    def scaledheight = img.height * scale

    return [scaledheight, scaledwidth]
  }

  void playMovie(String url) {
    GraphicsDevice myDevice = GraphicsEnvironment.localGraphicsEnvironment.defaultScreenDevice

    frame = new JFrame("Wibble")
    frame.undecorated = true

    myDevice.fullScreenWindow = frame

    EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
    mediaPlayerComponent.mediaPlayer.fullScreen=true

    frame.contentPane = mediaPlayerComponent

    frame.setLocation(100, 100);
    frame.setSize(1050, 600);
    frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
    frame.visible = true

    mediaPlayerComponent.mediaPlayer.playMedia(url);
    mediaPlayerComponent.mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
      @Override
      void stopped(MediaPlayer mediaPlayer) {
        println "STOPPED"
        frame.hide()

      }

      @Override
      void finished(MediaPlayer mediaPlayer) {
        println "FINISHED"
        if (frame) {
          frame.hide()
        }
      }
    })
  }

  @Override
  void stop() {
    if (mediaPlayerComponent) {
      mediaPlayerComponent.release()
      mediaPlayerComponent = null
    }
    if (frame) {
      frame.hide()
      frame.dispose()
      frame = null
    }
  }
}
