
import pluggable.scm.*;
import pluggable.scm.gerrit.*;

import java.lang.*;
import java.lang.reflect.*;
import java.util.Properties;

public class GerritSCMProviderTest extends GroovyTestCase {

  public void testGetScmUrlHttp(){
    GerritSCMProvider scmProvider = new GerritSCMProvider("10.0.0.1",80,
     GerritSCMProtocol.HTTP, "ADOP Gerrit", "jenkins", "true", "10.0.0.1",
     "jenkins", 22, "All-Projects/permissions", "All-Projects/permissions-with-review");

    assertEquals scmProvider.getScmUrl(), "http://10.0.0.1:80/"
  }

  public void testGetScmUrlSsh(){
    GerritSCMProvider scmProvider = new GerritSCMProvider("10.0.0.1",22,
    GerritSCMProtocol.SSH, "ADOP Gerrit", "jenkins", "true", "10.0.0.1",
    "jenkins", 22, "All-Projects/permissions", "All-Projects/permissions-with-review");

    assertEquals scmProvider.getScmUrl(), "ssh://jenkins@10.0.0.1:22/"
  }
}
