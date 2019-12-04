package org.hl7.fhir.igtools.publisher.utils;

import java.io.IOException;

import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.VersionUtilities;

public class VersionRedirectorGenerator {

  private String folder;

  public VersionRedirectorGenerator(String folder) {
    super();
    this.folder = folder;
  }
  
  public void execute(String version, String path) throws IOException {
    executeVer(version, path);
    if (VersionUtilities.isSemVer(version)) {
      executeVer(VersionUtilities.getMajMin(version), path);
    }
  }

  private void executeVer(String version, String path) throws IOException {
    if (!path.endsWith("/"+version)) {
      String vp = Utilities.path(folder, version);
      Utilities.createDirectory(vp);
      String wc = makeWebConfig(path);
      TextFile.stringToFile(wc, Utilities.path(vp, "web.config"));
    }
  }

  private String makeWebConfig(String path) {
    return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + 
        "<configuration>\n" + 
        "  <system.webServer>\n" + 
        "    <httpRedirect enabled=\"true\" destination=\""+path+"\" />\n" + 
        "  </system.webServer>\n" + 
        "</configuration>";
  }
}
