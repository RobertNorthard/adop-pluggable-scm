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
    String bitbucketHost = scmProviderProperties.getProperty("bitbucket.host");
    String bitbucketEndpoint = scmProviderProperties.getProperty("bitbucket.endpoint");
    String bitbucketProtocol = scmProviderProperties.getProperty("bitbucket.protocol");
    String bitbucketCredentialId = scmProviderProperties.getProperty("bitbucket.credentialId");
    int bitbucketPort = Integer.parseInt(scmProviderProperties.getProperty("bitbucket.port"));

    return new BitbucketSCMProvider(
            scmPort,
            BitbucketSCMProtocol.valueOf(this.validateProperties("scm.protocol", scmProtocol.toUpperCase())),
            this.validateProperties("bitbucket.host", bitbucketHost),
            this.validateBitbucketEndpoint(bitbucketEndpoint),
            BitbucketSCMProtocol.valueOf(this.validateProperties("bitbucket.protocol", bitbucketProtocol.toUpperCase())),
            bitbucketPort,
            this.validateProperties("bitbucket.credentialId", bitbucketCredentialId)
    );
  }

  /**
  * Return valid value.
  * @param key
  * @param value
  * @return Valid value
  * @throw IllegalArgumentException
  *           If the value ir null or empty without those params scrpts can't work.
  */
  public String validateProperties(String key, String value){
    if(value == null || value.equals("")){
        throw new IllegalArgumentException("Please make sure " + key + " exist and have valid value.");
    }
    return value;
  }

  /**
  * Set default endpoint if can't find in properties file.
  * @param endpoint bitbucket host endpoint.
  * @return default value / or endpoint from properties file.
  */
  public String validateBitbucketEndpoint(String endpoint){
      return ((endpoint == null || endpoint.equals("")) ? "/" :  "/" + endpoint);
  }
}
