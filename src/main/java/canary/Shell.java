package canary;

import java.io.IOException;
import java.util.List;

import org.yaml.snakeyaml.error.YAMLException;

public class Shell {

  public static void main(String[] args) throws IOException {
    List<Root> roots;
    try {
      roots = Root.load();
    } catch (YAMLException e) {
      System.out.println("Invalid configuration: " + e.getMessage());
      return;
    }
    Scanner s = Scanner.of(roots);
    s.scan();
  }

}
