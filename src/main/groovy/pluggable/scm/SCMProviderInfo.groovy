
package pluggable.scm;

import java.lang.annotation.*;
/**
* Annoation to mark SCM providers.
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SCMProviderInfo {

  String name();

}
