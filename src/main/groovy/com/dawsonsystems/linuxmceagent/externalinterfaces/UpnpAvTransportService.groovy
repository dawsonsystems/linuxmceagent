package com.dawsonsystems.linuxmceagent.externalinterfaces

import org.fourthline.cling.support.avtransport.AbstractAVTransportService
import org.fourthline.cling.support.lastchange.LastChange
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes
import org.fourthline.cling.support.avtransport.AVTransportException
import org.fourthline.cling.support.model.MediaInfo
import org.fourthline.cling.support.model.TransportInfo
import org.fourthline.cling.support.model.PositionInfo
import org.fourthline.cling.support.model.DeviceCapabilities
import org.fourthline.cling.support.model.TransportSettings
import org.fourthline.cling.support.model.TransportAction
import com.dawsonsystems.linuxmceagent.Player
import org.fourthline.cling.support.model.StorageMedium
import org.fourthline.cling.support.model.PlayMode

/**
 * Created with IntelliJ IDEA.
 * User: david
 * Date: 20/04/12
 * Time: 23:24
 * To change this template use File | Settings | File Templates.
 */
class UpnpAvTransportService extends AbstractAVTransportService {
  private MediaInfo currentMediaInfo = new MediaInfo();
  private volatile TransportInfo currentTransportInfo = new TransportInfo();
  private PositionInfo currentPositionInfo = new PositionInfo();

  UnsignedIntegerFourBytes playerId = new UnsignedIntegerFourBytes(4294967295 - 1500)

  Player player

  protected UpnpAvTransportService(LastChange lastChange, Player player) {
    super(lastChange)
    this.player = player
  }

  @Override
  public void setAVTransportURI(UnsignedIntegerFourBytes instanceId,
                                String currentURI,
                                String currentURIMetaData) throws AVTransportException {
    URI uri;

    try {
      uri = new URI(currentURI);
      println "Told To play $currentURI"

      //TODO, this starts playing straight away....
      player.play(currentURI)

    } catch (Exception ex) {
      println "Couldn't play this... $currentURI"
    }
  }

  @Override
  void setNextAVTransportURI(UnsignedIntegerFourBytes instanceId, String nextURI, String nextURIMetaData) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  MediaInfo getMediaInfo(UnsignedIntegerFourBytes instanceId) {
    return currentMediaInfo
  }

  @Override
  TransportInfo getTransportInfo(UnsignedIntegerFourBytes instanceId) {
    return currentTransportInfo
  }

  @Override
  PositionInfo getPositionInfo(UnsignedIntegerFourBytes instanceId) {
    return currentPositionInfo
  }

  @Override
  DeviceCapabilities getDeviceCapabilities(UnsignedIntegerFourBytes instanceId) {
    return new DeviceCapabilities([StorageMedium.NETWORK] as StorageMedium[]);
  }

  @Override
  TransportSettings getTransportSettings(UnsignedIntegerFourBytes instanceId) {
    return new TransportSettings(PlayMode.NORMAL)
  }

  @Override
  void stop(UnsignedIntegerFourBytes instanceId) {
    prinltn "Stop"
  }

  @Override
  void play(UnsignedIntegerFourBytes instanceId, String speed) {
    println "Play"
  }

  @Override
  void pause(UnsignedIntegerFourBytes instanceId) {
    println "Pause"
  }

  @Override
  void record(UnsignedIntegerFourBytes instanceId) {
    println "Record"
  }

  @Override
  void seek(UnsignedIntegerFourBytes instanceId, String unit, String target) {
    println "Seek"
  }

  @Override
  void next(UnsignedIntegerFourBytes instanceId) {
    println "Next"
  }

  @Override
  void previous(UnsignedIntegerFourBytes instanceId) {
    println "Previous "
  }

  @Override
  void setPlayMode(UnsignedIntegerFourBytes instanceId, String newPlayMode) {
    println "Set PlayMode"
  }

  @Override
  void setRecordQualityMode(UnsignedIntegerFourBytes instanceId, String newRecordQualityMode) {
    println "Set RecordQualityMode"
  }

  @Override
  protected TransportAction[] getCurrentTransportActions(UnsignedIntegerFourBytes instanceId) {
    println "getCurrentTransportActions()"
    //we may be lying....
    return TransportAction.values()
  }

  @Override
  UnsignedIntegerFourBytes[] getCurrentInstanceIds() {
    println "getCurrentInstanceIds()"
    return [playerId] as UnsignedIntegerFourBytes[]  //To change body of implemented methods use File | Settings | File Templates.
  }
}
