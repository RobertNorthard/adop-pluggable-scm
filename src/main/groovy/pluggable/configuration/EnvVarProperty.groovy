
package pluggable.configuration;

public class EnvVarProperty {

  private final Map<String, String> env = null;

  public EnvVarProperty(){
      env = System.env();
  }

  public String getPluggableSearchPath(){
      return env['SCM_PROVIDER_PLUGGABLE_PATH'];
  }

  public String getPropertiesLocation(){
      return env['SCM_PROVIDER_PROPERTY_LOCATION'];
  }
}
