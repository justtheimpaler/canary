package canary;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scanner {

  private static final Logger log = Logger.getLogger(Scanner.class.getName());

  private static DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private List<Root> roots;

  private Scanner(List<Root> roots) {
    this.roots = roots;
  }

  public static Scanner of(List<Root> roots) {
    return new Scanner(roots);
  }

  public void scan(ScannerStats totalStats) {
    for (Root r : this.roots) {
      File dir = new File(r.getRoot());
      if (!dir.exists()) {
        log.log(Level.SEVERE, "Invalid root folder: " + dir + "; this folder doesn't exist.");
        totalStats.countCritical();
      } else if (!dir.isDirectory()) {
        log.log(Level.SEVERE, "Invalid root folder: " + dir + "; a file with that name exits, but it's not a folder.");
        totalStats.countCritical();
      } else {
        log.info("Scanning root folder: " + dir);
        ScannerStats stats = new ScannerStats();
        scanFolder(dir, r, stats);
        log.info(" - Stats: counted " + stats.getTotalSubfolders() + " subfolders and " + stats.getTotalFiles()
            + " files --  with " + stats.getTotalCriticals() + " critical and " + stats.getTotalWarnings()
            + " warnings");
        totalStats.aggregate(stats);
      }
    }
  }

  private void scanFolder(File dir, Root root, ScannerStats stats) {
    File[] files = dir.listFiles();
    if (files != null) {
      if (files.length > root.getCriticalThreshold()) {
        log.log(Level.SEVERE, "The folder " + dir + " has " + files.length
            + " files in it, and that exceeds the critical threshold of " + root.getCriticalThreshold() + ".");
        stats.countCritical();
      } else if (files.length > root.getWarningThreshold()) {
        log.warning("The folder " + dir + " has " + files.length
            + " files in it, and that exceeds the warning threshold of " + root.getWarningThreshold() + ".");
        stats.countWarning();
      }
      for (File f : files) {
        if (f.isDirectory()) {
          if (!root.excludes(f)) {
            stats.countSubfolder();
            scanFolder(f, root, stats);
          }
        } else {
          stats.countFile();
        }
      }
    }
  }

}
