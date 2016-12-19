
package pluggable.scm;

import java.util.List;
import java.util.ArrayList;

import pluggable.configuration.EnvVarProperty;

/**
* SCP handler is responsible for dispatching get SCM provider requests to the correct
* SCM provider factory.
*
*/
public class SCMProviderHandler {

  private static String SCM_PROVIDER_FILE_EXTENSION = ".groovy"

  /**
  * Return an implementation of SCM provider for the provided unique
  *   SCM provider id. The method uses the SCM Provider data store to infer
  *   the SCM providers configuration.
  * @param scmProviderId - the unique id of the SCM provider.
  * @return the infferred SCM provider for the provided unique SCM provider Id.
  **/
  public static SCMProvider getScmProvider(String scmProviderId) {

    if(scmProviderId == null || scmProviderId.equals("")){
      throw new IllegalArgumentException("SCM provider id must be provided.")
    }

    Class<SCMProvider> scmProviderClass = null;
    SCMProviderFactory scmProviderFactory = null;
    SCMProvider scmProvider = null;
    EnvVarProperty envVarProperty = new EnvVarProperty();

    // assume properties datastore by default
    SCMProviderDataStore scmProviderDataStore = new PropertiesSCMProviderDataStore();

    Properties scmProviderProperties = scmProviderDataStore.get(scmProviderId);

    if(scmProviderProperties == null){
      throw IllegalArgumentException("SCM provider properties not found.")
    }

    String scmProviderType = scmProviderProperties.getProperty("scm.type");

    if(scmProviderType == null || scmProviderType.equals("")){
        throw new IllegalArgumentException("SCM provider property, 'scm.type' must be specified.");
    }

    scmProviderClass = SCMProviderHandler
                        .findScmProvider(scmProviderType, SCMProviderHandler
                                            .findClasses(new File(envVarProperty.getPluggableSearchPath()),""));

    if(scmProviderClass == null){
        throw new IllegalArgumentException("SCM provider for scm.type=" + scmProviderType + " cannot be found.");
    }

    scmProviderFactory = (SCMProviderFactory)scmProviderClass.newInstance();
    scmProvider = scmProviderFactory.create(scmProviderProperties);

    return scmProvider;
  }

  /**
  * Return a collection of all classes in a directory and specified package.
  *
  * @param directory directory to recursively search for class files.
  * @param packageName Java package name to search.
  * @return a collection of class files.
  **/
  private static List<Class> findClasses(File directory, String packageName) {

    ClassLoader classLoader = SCMProviderHandler.class.getClassLoader();
    List<Class> classes = new ArrayList<Class>();

    if (!directory.exists()) {
       return classes;
    }

    File[] allFiles = directory.listFiles();

    for(File file: allFiles){

      String fileName = file.getName();

      if (file.isDirectory()) {
         classes.addAll(findClasses(file, packageName + "." + file.getName()));
     } else if (fileName.endsWith(SCMProviderHandler.SCM_PROVIDER_FILE_EXTENSION)) {

         String className = packageName + "." + fileName.substring(0, fileName.length() - SCMProviderHandler.SCM_PROVIDER_FILE_EXTENSION.length())
         className = className.replace('/','.')
                                .replaceFirst('.', '');
         classes.add(Class.forName(className));
     }
    }
    return classes;
  }

  /**
  * Find SCM provider with the specified provider type. Returns null is the SCM provider is not found.
  *
  * @param providerName SCM provider type.
  * @return SCM provider with the specified provider type. Returns null if the SCM provider is not found.
  **/
  private static Class findScmProvider(String providerType, List<Class> classes){
    for (Class foundClass : classes){
      if(foundClass.isAnnotationPresent(SCMProviderInfo.class)){
        if(foundClass.getAnnotation(SCMProviderInfo.class).name().equals(providerType)){
          return foundClass;
        }
      }
    }
    return null;
  }
}
