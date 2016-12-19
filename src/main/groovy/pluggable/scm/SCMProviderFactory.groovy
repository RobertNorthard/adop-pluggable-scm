
package pluggable.scm;

import java.util.Properties;

public interface SCMProviderFactory {

    /**
    * Return a SCM Provider configured according to the provided SCM properties.
    *
    * @param scmProviderProperties - properties for the SCM provider.
    * @return SCMProvider configured from the provided SCM properties.
    **/
    public SCMProvider create(Properties scmProviderProperties);
}
