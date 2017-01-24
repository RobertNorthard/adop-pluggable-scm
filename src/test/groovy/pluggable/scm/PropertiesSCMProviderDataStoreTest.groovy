
import pluggable.scm.*;
import pluggable.scm.gerrit.*;

import java.lang.*;
import java.lang.reflect.*;
import java.util.Properties;

public class PropertiesSCMProviderDataStoreTest extends GroovyTestCase {

  public void testGetScmType(){
    PropertiesSCMProviderDataStore scmProviderDataStore = new PropertiesSCMProviderDataStore();
    scmProviderDataStore.setPropertiesLocation("src/test/resources/properties/");
    Properties properties = scmProviderDataStore.get("adop-gerrit-http");

    assertEquals  "gerrit", properties.getProperty("scm.type")
  }
}
