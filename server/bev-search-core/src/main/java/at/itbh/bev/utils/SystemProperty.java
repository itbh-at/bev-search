package at.itbh.bev.utils;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see <a href="https://github.com/juraj-blahunka/site-examples/tree/master/inject-jboss-system-properties">https://github.com/juraj-blahunka/site-examples/tree/master/inject-jboss-system-properties</a>
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface SystemProperty {

	/**
	 * Full name of the system property, for example "user.home"
	 */
	@Nonbinding String value();

}