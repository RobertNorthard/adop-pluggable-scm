package pluggable.scm.bitbucket;

import pluggable.scm.SCMProvider;
import pluggable.scm.SCMProviderFactory;
import pluggable.scm.SCMProviderInfo;

/**
* The Bitbucket SCM factory class is responsible for parsing the
* providers properties and instantiating a BitbucketSCMProvider.
*/
@SCMProviderInfo(type="bitbucket")
public class BitbucketSCMProviderFactory implements SCMProviderFactory {

  /**
  * A factory method which return an SCM Provider instantiated with the
  * the provided properties.
  *
  * @param scmProviderProperties - properties for the SCM provider.
  * @return SCMProvider configured from the provided SCM properties.
  **/
  public SCMProvider create(Properties scmProviderProperties){
    String scmProtocol = scmProviderProperties.getProperty("scm.protocol");
    int scmPort = Integer.parseInt(scmProviderProperties.getProperty("scm.port"));

    // Env veriables for Bitbucket REST
    String bitbucketEndpoint = scmProviderProperties.getProperty("bitbucket.endpoint");
    String bitbucketEndpointContext = scmProviderProperties.getProperty("bitbucket.endpoint.context");
    String bitbucketProtocol = scmProviderProperties.getProperty("bitbucket.protocol");
    int bitbucketPort = Integer.parseInt(scmProviderProperties.getProperty("bitbucket.port"));

    return new BitbucketSCMProvider(
            scmPort,
            BitbucketSCMProtocol.valueOf(this.validateProperties("scm.protocol", scmProtocol.toUpperCase())),
            this.validateProperties("bitbucket.endpoint", bitbucketEndpoint),
            bitbucketEndpointContext,
            BitbucketSCMProtocol.valueOf(this.validateProperties("bitbucket.protocol", bitbucketProtocol.toUpperCase())),
            bitbucketPort
    );
  }

  /**
  * Return valid value.
  * @param key
  * @param value
  * @return Valid value
  * @throw IllegalArgumentException
  *           If the value ir null or empty without those params scripts can't work.
  */
  public String validateProperties(String key, String value){
    if(value == null || value.equals("")){
        throw new IllegalArgumentException("Please make sure " + key + " exist and have valid value.");
    }
    return value;
  }
}
