package canary;

public class ScannerStats {

  private long totalSubfolders = 0;
  private long totalFiles = 0;
  private long totalWarnings = 0;
  private long totalCriticals = 0;

  // aggregation

  public void aggregate(ScannerStats other) {
    this.totalSubfolders += other.totalSubfolders;
    this.totalFiles += other.totalFiles;
    this.totalWarnings += other.totalWarnings;
    this.totalCriticals += other.totalCriticals;
  }

  // counting

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

  // getters

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
