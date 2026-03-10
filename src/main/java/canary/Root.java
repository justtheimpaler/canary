package canary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class Root {

  private static final String DEFAULT_CONFIG_FILE = "canary.yaml";

  private String root;
  private String[] excludeNamePatterns;
  private String[] excludeRelativePaths;
  private long warningThreshold;
  private long criticalThreshold;

  private List<Pattern> namePatterns;

  public static List<Root> load() throws IOException {
    return load(DEFAULT_CONFIG_FILE);
  }

  public static List<Root> load(String configFile) throws IOException {
    Yaml yaml = new Yaml(new Constructor(Root.class, new LoaderOptions()));
//    Yaml yaml = new Yaml(new SafeConstructor(Root.class, new LoaderOptions()));
    List<Root> roots = new ArrayList<>();
    try (InputStream is = new FileInputStream(configFile)) {
      Iterable<Object> all = yaml.loadAll(is);
      System.out.println(all.getClass().getName());
      for (Object o : all) {
        Root root = (Root) o;
        System.out.println(o.getClass().getName() + ": " + o);
        roots.add(root);
        root.validate();
      }
    }

    return roots;
  }

  private void validate() {
    this.namePatterns = Arrays.stream(this.excludeNamePatterns).map(n -> Pattern.compile(n))
        .collect(Collectors.toList());
  }

  public boolean excludes(File f) {
    for (Pattern p : this.namePatterns) {
      Matcher m = p.matcher(f.getName());
      if (m.find()) {
        return true;
      }
    }
    return false;
  }

  // Getters and Setters

  public String getRoot() {
    return root;
  }

  public void setRoot(String root) {
    this.root = root;
  }

  public String[] getExcludeNamePatterns() {
    return excludeNamePatterns;
  }

  public void setExcludeNamePatterns(String[] excludeNamePatterns) {
    this.excludeNamePatterns = excludeNamePatterns;
  }

  public String[] getExcludeRelativePaths() {
    return excludeRelativePaths;
  }

  public void setExcludeRelativePaths(String[] excludeRelativePaths) {
    this.excludeRelativePaths = excludeRelativePaths;
  }

  public long getWarningThreshold() {
    return warningThreshold;
  }

  public void setWarningThreshold(long warningThreshold) {
    this.warningThreshold = warningThreshold;
  }

  public long getCriticalThreshold() {
    return criticalThreshold;
  }

  public void setCriticalThreshold(long criticalThreshold) {
    this.criticalThreshold = criticalThreshold;
  }

  public List<Pattern> getNamePatterns() {
    return namePatterns;
  }

  public void setNamePatterns(List<Pattern> namePatterns) {
    this.namePatterns = namePatterns;
  }

  @Override
  public String toString() {
    return "Root [root=" + root + ", excludeNamePatterns=" + Arrays.toString(excludeNamePatterns)
        + ", excludeRelativePaths=" + Arrays.toString(excludeRelativePaths) + ", warningThreshold=" + warningThreshold
        + ", criticalThreshold=" + criticalThreshold + ", namePatterns=" + namePatterns + "]";
  }

}
