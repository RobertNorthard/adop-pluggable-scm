
import pluggable.scm.*;
import pluggable.scm.bitbucket.*;

import java.lang.*;
import java.lang.reflect.*;
import java.util.Properties;

public class ButBucketCMProviderTest extends GroovyTestCase {

  public void testGetScmUrlHttpWithoutContext(){
    BitbucketSCMProvider scmProvider = new BitbucketSCMProvider(
        443,
        BitbucketSCMProtocol.HTTPS,
        "10.0.0.1",
        "/",
        BitbucketSCMProtocol.HTTPS,
        443
      );

    assertEquals "https://10.0.0.1:443/scm/", scmProvider.getScmUrl()
  }

  public void testGetScmUrlHttpWithContext(){
    BitbucketSCMProvider scmProvider = new BitbucketSCMProvider(
        443,
        BitbucketSCMProtocol.HTTPS,
        "10.0.0.1",
        "/bitbucket",
        BitbucketSCMProtocol.HTTPS,
        443
      );

    assertEquals "https://10.0.0.1:443/bitbucket/scm/", scmProvider.getScmUrl()
  }

  public void testGetScmUrlSshWithoutContext(){
    BitbucketSCMProvider scmProvider = new BitbucketSCMProvider(
        22,
        BitbucketSCMProtocol.SSH,
        "10.0.0.1",
        "/",
        BitbucketSCMProtocol.HTTPS,
        443
      );

    assertEquals "ssh://git@10.0.0.1:22/", scmProvider.getScmUrl()
  }

  public void testGetScmUrlSshWithContext(){
    BitbucketSCMProvider scmProvider = new BitbucketSCMProvider(
        22,
        BitbucketSCMProtocol.SSH,
        "10.0.0.1",
        "/bitbucket",
        BitbucketSCMProtocol.HTTPS,
        443
      );

    assertEquals "ssh://git@10.0.0.1:22/bitbucket/", scmProvider.getScmUrl()
  }
}
