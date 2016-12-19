
import pluggable.scm.*;
import pluggable.scm.gerrit.*;

public class GerritSCMProviderTest extends GroovyTestCase {

  public void testGetScmUrlHttp(){
    GerritSCMProvider scmProvider = new GerritSCMProvider("10.0.0.1", 80, GerritSCMProtocol.HTTP, "ADOP Gerrit", "jenkins");
    assertEquals scmProvider.getScmUrl(), "http://10.0.0.1:80/"
  }

  public void testGetScmUrlSsh(){
    GerritSCMProvider scmProvider = new GerritSCMProvider("10.0.0.1",22, GerritSCMProtocol.SSH, "ADOP Gerrit", "jenkins");
    assertEquals scmProvider.getScmUrl(), "ssh://jenkins@10.0.0.1:22/"
  }
}
