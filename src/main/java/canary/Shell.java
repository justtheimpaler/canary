package canary;

import java.io.IOException;
import java.util.List;

public class Shell {

  public static void main(String[] args) throws IOException {
    List<Root> roots = Root.load();
    Scanner s = Scanner.of(roots);
    s.scan();
  }

}
