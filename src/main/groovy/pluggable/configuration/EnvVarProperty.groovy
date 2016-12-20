
package pluggable.configuration;

/**
* A singleton class representing a EnvVarProperty object.
* This class is responsible for store injected/binded
* environment variables to used by the SCM pluggable library.
*
*/
public class EnvVarProperty {

  private def bindings = null;

  private static final Object lock = new Object();
  private static EnvVarProperty singleton = null;

  /**
  * A singleton method for the EnvVarProperty object.
  * @return an instance of an EnvVarProperty.
  */
  public static EnvVarProperty getInstance(){
    if(EnvVarProperty.singleton == null){
      synchronized(lock) {
        EnvVarProperty.singleton = new EnvVarProperty()
      }
    }

    return EnvVarProperty.singleton;
  }

  /**
  * Provate constructor for class EnvVarProperty as it's a singleton.
  */
  private EnvVarProperty(){ }

  /**
  * A setter method used to inject variables to store.
  *
  * @param variables - variables to store.
  */
  public void setVariableBindings(def variables){
      this.bindings = variables;
  }

  /**
  * Return a string representation of the SCM provider's pluggable search
  * path.
  *
  * @return a string representation of the SCM provider's pluggable search
  * path.
  */
  public String getPluggablePath(){

      if(!this.checkDirectoryExists(this.bindings.SCM_PROVIDER_PLUGGABLE_PATH)){
        throw new IllegalArgumentException(
          "Invalid environment variable - SCM_PROVIDER_PLUGGABLE_PATH must be a valid directory.");
      }

      return this.bindings.SCM_PROVIDER_PLUGGABLE_PATH
  }

  /**
  * Return a string representation of the SCM provider's properties file search
  * path.
  *
  * @return a string representation of the SCM provider's properties file search
  * path.
  */
  public String getPropertiesPath(){

      if(!this.checkDirectoryExists(this.bindings.SCM_PROVIDER_PROPERTIES_PATH)){
        throw new IllegalArgumentException(
          "Invalid environment variable - SCM_PROVIDER_PROPERTIES_PATH value must be a valid directory.");
      }

      return this.bindings.SCM_PROVIDER_PROPERTIES_PATH
  }

  /**
  * Return STDOUT logger.
  *
  * @return STDOUT logger.
  */
  public PrintStream getLogger(){
    return this.bindings.out;
  }

  /**
  * Return true if the specified path is a directory and exists.
  *
  * @param directoryPath - the file path to the directory.
  * @return true if path is a directory and exists.
  */
  private boolean checkDirectoryExists(String directoryPath){
      File directory = new File(directoryPath);

      if(directory.exists() && directory.isDirectory()) {
          return true;
      }else{
          return false;
      }
  }
}
