
package pluggable.configuration;

public class EnvVarProperty {

  private def bindings = null;

  private static final Object lock = new Object();
  private static final EnvVarProperty singleton;

  public static EnvVarProperty getInstance(){
    if(EnvVarProperty.singleton == null){
      synchronized(lock) {
        EnvVarProperty.singleton = new EnvVarProperty()
      }
    }

    return EnvVarProperty.singleton;
  }

  private EnvVarProperty(){ }

  public void setVariableBindings(def bindings){
    this.bindings = bingings;
  }

  public String getPluggablePath(){
      return this.bindings.SCM_PROVIDER_PLUGGABLE_PATH
  }

  public String getPropertiesPath(){
      return this.bindings.SCM_PROVIDER_PROPERTY_LOCATION
  }
}
