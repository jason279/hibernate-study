package jason.self.fms.service.cartridge.api;

import org.apache.commons.lang3.StringUtils;

public class DomainNameUtil {
	static public final String SEPARATOR = ".";

	static public boolean isValidDomainName(String domainName) {
		return StringUtils.isEmpty(domainName)
				|| (StringUtils.isAlphanumeric(domainName.replace(SEPARATOR, "0")) && !domainName.startsWith(SEPARATOR)
						&& !domainName.endsWith(SEPARATOR) && domainName.indexOf(SEPARATOR + SEPARATOR) < 0);
	}

	public static boolean isEmptyDomain(String domainName) {
		return StringUtils.isEmpty(domainName);
	}
}
