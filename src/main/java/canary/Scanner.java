package canary;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Scanner {

  private static DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private List<Root> roots;

  private Scanner(List<Root> roots) {
    this.roots = roots;
  }

  public static Scanner of(List<Root> roots) {
    return new Scanner(roots);
  }

  public void scan() {
    for (Root r : this.roots) {
      File dir = new File(r.getRoot());
      if (!dir.exists()) {
        critical("Invalid root folder: " + dir + "; this folder doesn't exist.");
      } else if (!dir.isDirectory()) {
        critical("Invalid root folder: " + dir + "; a file with that name exits, but it's not a folder.");
      } else {
        info("Scanning root folder: " + dir);
        ScannerStats stats = new ScannerStats();
        scanFolder(dir, r, stats);
        info(" - Stats: counted " + stats.getTotalSubfolders() + " subfolders and " + stats.getTotalFiles()
            + " files --  with " + stats.getTotalCriticals() + " critical and " + stats.getTotalWarnings()
            + " warnings");
      }
    }
  }

  private void scanFolder(File dir, Root root, ScannerStats stats) {
    File[] files = dir.listFiles();
    if (files != null) {
      if (files.length > root.getCriticalThreshold()) {
        critical("The folder " + dir + " has " + files.length
            + " files in it, and that exceeds the critical threshold of " + root.getCriticalThreshold() + ".");
        stats.countCritical();
      } else if (files.length > root.getWarningThreshold()) {
        warn("The folder " + dir + " has " + files.length + " files in it, and that exceeds the warning threshold of "
            + root.getWarningThreshold() + ".");
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

  private static class ScannerStats {

    private long totalSubfolders = 0;
    private long totalFiles = 0;
    private long totalWarnings = 0;
    private long totalCriticals = 0;

    public void countSubfolder() {
      this.totalSubfolders++;
    }

    public void countFile() {
      this.totalFiles++;
    }

    public void countWarning() {
      this.totalWarnings++;
    }

    public void countCritical() {
      this.totalCriticals++;
    }

    public long getTotalSubfolders() {
      return totalSubfolders;
    }

    public long getTotalFiles() {
      return totalFiles;
    }

    public long getTotalWarnings() {
      return totalWarnings;
    }

    public long getTotalCriticals() {
      return totalCriticals;
    }

  }

  private void info(String message) {
    System.out.println("INFO " + FMT.format(LocalDateTime.now()) + " - " + message);
  }

  private void warn(String message) {
    System.out.println("WARN " + FMT.format(LocalDateTime.now()) + " - " + message);
  }

  private void critical(String message) {
    System.out.println("CRITICAL " + FMT.format(LocalDateTime.now()) + " - " + message);
  }

}
