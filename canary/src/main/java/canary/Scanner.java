package canary;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scanner {

  private static final Logger log = Logger.getLogger(Scanner.class.getName());

  private List<Root> roots;

  private Scanner(List<Root> roots) {
    this.roots = roots;
  }

  public static Scanner of(List<Root> roots) {
    return new Scanner(roots);
  }

  public void scan(ScannerStats totalStats) {
    DecimalFormat fmt = new DecimalFormat("#,##0");
    int i = 1;
    for (Root r : this.roots) {
      File dir = new File(r.getRoot());
      if (!dir.exists()) {
        log.log(Level.SEVERE, "Invalid root folder #" + i + ": " + dir + "; this folder doesn't exist.");
        totalStats.countCritical();
      } else if (!dir.isDirectory()) {
        log.log(Level.SEVERE,
            "Invalid root folder #" + i + ": " + dir + "; a file with that name exits, but it's not a folder.");
        totalStats.countCritical();
      } else {
        log.info("Scanning root folder #" + i + ": " + dir);
        ScannerStats stats = new ScannerStats();
        scanFolder(dir, r, stats);
        log.info(" * Summary: " + fmt.format(stats.getTotalCriticals()) + " criticals and "
            + fmt.format(stats.getTotalWarnings()) + " warnings -- " + fmt.format(stats.getTotalSubfolders())
            + " subfolders, " + fmt.format(stats.getTotalFiles()) + " files, " + fmt.format(stats.getTotalBytes())
            + " bytes");
        totalStats.aggregate(stats);
      }
      i++;
    }
  }

  private void scanFolder(File dir, Root root, ScannerStats stats) {
    File[] files = dir.listFiles();
    if (files != null) {
      if (files.length >= root.getCriticalThreshold()) {
        log.log(Level.SEVERE, " * The folder " + dir + " has " + files.length
            + " files in it, and that reached the critical threshold of " + root.getCriticalThreshold() + ".");
        stats.countCritical();
      } else if (files.length >= root.getWarningThreshold()) {
        log.warning(" * The folder " + dir + " has " + files.length
            + " files in it, and that reached the warning threshold of " + root.getWarningThreshold() + ".");
        stats.countWarning();
      }
      for (File f : files) {
        if (f.isDirectory()) {
          if (!root.excludes(f)) {
            stats.countSubfolder();
            scanFolder(f, root, stats);
          }
        } else {
          stats.countFile(f.length());
        }
      }
    }
  }

}
