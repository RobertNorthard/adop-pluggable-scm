package pluggable.scm;

import java.util.Properties;

/**
* The SCM provider factory definition.
* It is responsible for parsing the providers properties and instantiating
* the correct SCM provider.
*/
public interface SCMProviderFactory {

    /**
    * A factory method which return a SCM Provider instantiated according
    * to the provided SCM properties.
    *
    * @param scmProviderProperties - properties for the SCM provider.
    * @return SCMProvider instantiated the provided SCM properties.
    **/
    public SCMProvider create(Properties scmProviderProperties);
}
