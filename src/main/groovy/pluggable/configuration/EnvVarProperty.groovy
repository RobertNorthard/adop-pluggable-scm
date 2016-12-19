
package pluggable.configuration;

public class EnvVarProperty {

  public String getPluggableSearchPath(){
      return System.getenv("SCM_PROVIDER_PLUGGABLE_PATH");
  }

  public String getPropertiesLocation(){
      return System.getenv("SCM_PROVIDER_PROPERTY_LOCATION");
  }
}
