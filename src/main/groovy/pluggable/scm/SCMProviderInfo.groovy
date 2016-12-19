
package pluggable.scm;

import java.lang.annotation.*;

/**
* Annotation to mark SCM providers.
*
* @author Robert Northard <robertnorthard@googlemail.com>
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SCMProviderInfo {

  /**
  * Return a string representation of the SCM type implemented by the SCM
  * provider.
  *
  * @return a string representation of the SCM type implemented by the SCM
  *         provider.
  */
  String type();

}
