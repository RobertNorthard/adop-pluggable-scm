package pluggable.scm;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.*;
import pluggable.scm.helpers.Logger;
import pluggable.configuration.EnvVarProperty;

/**
* The SCMProviderHandler is responsible for dispatching SCM provider
* requests to the correctSCM provider factory.
*/
public class SCMProviderHandler {

  private static String SCM_PROVIDER_FILE_EXTENSION = ".groovy";

  /**
  * Return an implementation of SCM provider for the provided unique
  *   SCM provider id. The method uses the SCM Provider data store to infer
  *   the SCM providers configuration.
  * @param scmProviderId - the unique id of the SCM provider.
  * @param variableBindings - map to inject environment variables/SCM provider library configuration
  * @return the inferred SCM provider for the provided unique SCM provider Id.
  **/

  public static SCMProvider getScmProvider(String scmProviderId, def variableBindings) {

    if(scmProviderId == null || scmProviderId.equals("")){
      throw new IllegalArgumentException("SCM provider id must be provided.")
    }

    EnvVarProperty envVarProperty = EnvVarProperty.getInstance();
    envVarProperty.setVariableBindings(variableBindings);

    Class<SCMProvider> scmProviderClass = null;
    SCMProviderFactory scmProviderFactory = null;
    SCMProvider scmProvider = null;

    Logger.info("Finding SCM provider for SCM provider id: " + scmProviderId);

    // assume properties datastore by default
    SCMProviderDataStore scmProviderDataStore = new PropertiesSCMProviderDataStore();

    Properties scmProviderProperties = scmProviderDataStore.get(scmProviderId);

    if(scmProviderProperties == null){
      throw IllegalArgumentException("SCM provider properties not found.")
    }

    Logger.info("Found properties datastore for SCM provider id: " + scmProviderId);

    String scmProviderType = scmProviderProperties.getProperty("scm.type");

    Logger.info("SCM provider type: " + scmProviderType);

    if(scmProviderType == null || scmProviderType.equals("")){
        throw new IllegalArgumentException("SCM provider property, 'scm.type' must be specified.");
    }

    Logger.info("Inferring SCM provider.");

    scmProviderClass = SCMProviderHandler.findScmProvider(scmProviderType, SCMProviderHandler
                                            .findClasses(new File(envVarProperty.getPluggablePath()),""));

    if(scmProviderClass == null){
        throw new IllegalArgumentException("SCM provider for scm.type=" + scmProviderType + " cannot be found.");
    }

    Logger.info("Using class " + scmProviderClass.getName() + " SCM provider for type: " + scmProviderType);

    scmProviderFactory = (SCMProviderFactory)scmProviderClass.newInstance();
    scmProvider = scmProviderFactory.create(scmProviderProperties);

    return scmProvider;
  }

  /**
  * Return a collection of all classes in a directory and specified package by
  * recursively searching.
  *
  * @param directory - directory to recursively search for class files.
  * @param packageName - Java package name to search.
  * @return a collection of class files in the specified directory.
            If the directory does not exist an empty collection is returned.
  **/
  private static List<Class> findClasses(File directory, String packageName) {

    ClassLoader classLoader = SCMProviderHandler.class.getClassLoader();

    List<Class> classes = new ArrayList<Class>();

    File[] allFiles = null;
    String fileName = null;
    StringBuffer className = new StringBuffer("");
    String classPackageName = null;

    /*
      base condition - if directory return classes else files to load
      into the class loader.
    */
    if (!directory.exists()) {
       return classes;
    }else{
      allFiles = directory.listFiles();
    }

    for(File file: allFiles){

      fileName = file.getName();

      /*
        all class names start with packageName.
        e.g. pluggable.scm.gerrit.GerritSCMProvider
      */
      className.append(packageName);
      className.append(".");

      // recursively looks for more files if directory else load classes.
      if (file.isDirectory()) {

        className.append(fileName);

         classes.addAll(
            findClasses(file, className.toString()));

     } else if (fileName.endsWith(SCMProviderHandler.SCM_PROVIDER_FILE_EXTENSION)) {

        className.append(
            fileName.substring(0,
              fileName.length() - SCMProviderHandler.SCM_PROVIDER_FILE_EXTENSION.length()));

        classPackageName = getPackageName(className.toString());

         classes.add(
           Class.forName(classPackageName));
     }
     className = new StringBuffer("");
    }
    return classes;
  }

  /**
  * Return a SCMProvider with the specified provider type.
  * Returns null if the SCM provider is not found.
  * This method assumes there is only one implementation of the SCM type.
  *
  * @param providerName - SCM provider type.
  * @return SCM provider with the specified provider type.
  *         Returns null if the SCM provider is not found.
  *
  * @throws IllegalArgumentException
  *         If provider type is null or not specified.
  *         If collection of classes is null.
  *
  **/
  private static Class findScmProvider(String providerType, List<Class> classes){

    if(providerType == null || providerType.equals("")){
      throw new IllegalArgumentException("Provider type must be specified.");
    }

    if(classes == null){
      throw new IllegalArgumentException("A collection of classes must be provided.");
    }

    for (Class foundClass : classes){
      if(foundClass.isAnnotationPresent(SCMProviderInfo.class)){
        if(foundClass.getAnnotation(SCMProviderInfo.class).type().equals(providerType)){
          return foundClass;
        }
      }
    }
    return null;
  }

  /**
  * Utility method which converts file path name to a package name.
  * e.g. pluggable/scm/helpers to pluggable.scm.helpers.
  *
  * @param pathName - pathName to convert to package name.
  * @return a package name corresponding to the provided file path name.
  *
  */
  private static String getPackageName(String pathName){
    return pathName
                .replace('/','.')
                .replaceFirst('.', '');
  }
}
