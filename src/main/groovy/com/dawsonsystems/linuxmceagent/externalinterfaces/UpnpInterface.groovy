package com.dawsonsystems.linuxmceagent.externalinterfaces

import com.dawsonsystems.linuxmceagent.Player
import org.fourthline.cling.UpnpService
import org.fourthline.cling.UpnpServiceImpl
import org.fourthline.cling.model.meta.LocalDevice
import org.fourthline.cling.model.meta.LocalService
import org.fourthline.cling.support.lastchange.LastChangeAwareServiceManager
import org.fourthline.cling.support.avtransport.lastchange.AVTransportLastChangeParser
import org.fourthline.cling.binding.LocalServiceBinder
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder
import org.fourthline.cling.support.lastchange.LastChange
import org.fourthline.cling.model.meta.DeviceIdentity
import org.fourthline.cling.model.types.UDN
import org.fourthline.cling.model.types.UDADeviceType
import org.fourthline.cling.model.meta.DeviceDetails
import org.fourthline.cling.model.ModelUtil
import org.fourthline.cling.model.meta.ManufacturerDetails
import org.fourthline.cling.model.meta.ModelDetails
import org.fourthline.cling.model.meta.Icon
import org.fourthline.cling.model.ValidationException
import javax.imageio.ImageIO
import javax.swing.ImageIcon

/**
 * Presents a UPNP Interface to this agent.
 */
class UpnpInterface {

  final protected LocalServiceBinder binder = new AnnotationLocalServiceBinder();
  final private UpnpService upnpService;
  final protected LocalDevice device;
  final protected LastChange avTransportLastChange = new LastChange(new AVTransportLastChangeParser());
  final protected LastChangeAwareServiceManager<UpnpAvTransportService> avTransport;

  Player player

  UpnpInterface() {
    upnpService = new UpnpServiceImpl()

    // The AVTransport just passes the calls on to the backend players
    LocalService<UpnpAvTransportService> avTransportService = binder.read(UpnpAvTransportService.class);
    avTransport =
      new LastChangeAwareServiceManager<UpnpAvTransportService>(
              avTransportService,
              new AVTransportLastChangeParser()
      ) {
        @Override
        protected UpnpAvTransportService createServiceInstance() throws Exception {
          return new UpnpAvTransportService(avTransportLastChange, player);
        }
      }

    avTransportService.setManager(avTransport);
    try {

      device = new LocalDevice(
              new DeviceIdentity(UDN.uniqueSystemIdentifier("LinuxMCE Agent MediaRenderer")),
              new UDADeviceType("MediaRenderer", 1),
              new DeviceDetails(
                      "MediaRenderer on " + ModelUtil.getLocalHostName(false),
                      new ManufacturerDetails("Cling", "http://4thline.org/projects/cling/"),
                      new ModelDetails("Cling MediaRenderer", "App Name", "1", "http://4thline.org/projects/cling/mediarenderer/")
              ),
              [createDefaultDeviceIcon()] as Icon[],
              [avTransportService] as LocalService[]);

      upnpService.registry.addDevice(device)
    } catch (ValidationException ex) {
      throw new RuntimeException(ex);
    }
  }

  protected Icon createDefaultDeviceIcon() {
    String iconPath = "images/linuxmcelogo.png";
    try {
      return new Icon(
              "image/png",
              48, 48, 8,
              URI.create("icon.png"),
              ClassLoader.getSystemClassLoader().getResourceAsStream(iconPath))
    } catch (IOException ex) {
      throw new RuntimeException("Could not load icon: " + iconPath, ex);
    }
  }
}
