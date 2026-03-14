package canary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.yaml.snakeyaml.error.YAMLException;

public class Canary {

  private static final Logger log = Logger.getLogger(Canary.class.getName());

  private static final String DEFAULT_CONFIG_FILE = "canary.yaml";

  static {
    JULCustomFormatter.initialize();
  }

  public static BuildInformation BUILD_INFO;

  public static void main(String[] args) throws IOException {
    BUILD_INFO = new BuildInformation();

    log.info("Canary " + BUILD_INFO.getVersion() + " - build " + BUILD_INFO.getBuildId());

    String configFile = args.length == 0 ? DEFAULT_CONFIG_FILE : args[0];
    log.info("Loading configuration from: " + configFile);
    List<Root> roots;
    try {
      roots = Root.load(configFile);
      log.info("Roots: " + roots.size() + " roots to scan");
    } catch (FileNotFoundException e) {
      log.log(Level.SEVERE, e.getMessage());
      log.info("Aborting scan.");
      return;
    } catch (YAMLException e) {
      log.log(Level.SEVERE, "Invalid configuration: " + e.getMessage());
      log.info("Aborting scan.");
      return;
    }
    Scanner s = Scanner.of(roots);
    ScannerStats totalStats = new ScannerStats();
    long start = System.currentTimeMillis();
    s.scan(totalStats);
    long elapsed = System.currentTimeMillis() - start;
    log.info("Grand total of " + totalStats.getTotalCriticals() + " critical and " + totalStats.getTotalWarnings()
        + " warnings -- Scanned " + totalStats.getTotalSubfolders() + " subfolders and " + totalStats.getTotalFiles()
        + " files -- Scan time: " + formatDuration(elapsed));
  }

  // Build Information retrieval

  public static class BuildInformation {

    private static final String BUILD_INFORMATION_PROPERTIES = "build-information.propertises";

    private String version = null;
    private String buildId = null;

    // Constructor

    BuildInformation() throws IOException {
      try {
        Properties props = new Properties();
        File p = new File(BUILD_INFORMATION_PROPERTIES);
        if (!p.exists()) {
          throw new FileNotFoundException("Could not find build information.");
        }
        props.load(ClassLoader.getSystemResourceAsStream(BUILD_INFORMATION_PROPERTIES));
        this.version = (String) props.get("version");
        this.buildId = (String) props.get("build.id");
      } catch (RuntimeException | IOException e) {
        log.log(Level.INFO, "Could not read the build information file.", e);
        throw e;
      }
    }

    // Getters

    public String getVersion() {
      return this.version;
    }

    public String getBuildId() {
      return this.buildId;
    }

  }

  private static String formatDuration(long duration) {
    long HH = TimeUnit.MILLISECONDS.toHours(duration);
    long MM = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
    long SS = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
    long MS = duration % 1000;
    return String.format("%02d:%02d:%02d.%03d", HH, MM, SS, MS);
  }

}
