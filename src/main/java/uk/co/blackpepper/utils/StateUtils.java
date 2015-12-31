package uk.co.blackpepper.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import static uk.co.blackpepper.utils.Functions.map;

public final class StateUtils {

	private static Logger LOG = LoggerFactory.getLogger(StateUtils.class);

	/* Values prefixed with "__" will be made available to the JavaScript
	 * render function. All other values will be passed in the client's state object.
	 */
	public static void populateStateIntoModel(Model model, HttpServletRequest request) {
		model.addAttribute("__requestPath", getRequestPath(request));
		model.addAttribute("auth", getAuthState());
	}

	private static String getRequestPath(HttpServletRequest request) {
		return request.getRequestURI() +
			(request.getQueryString() == null ? "" : "?" + request.getQueryString());
	}

	public static Map<String, Object> getAuthState() {
		List<String> roles = getRoles();

		Map<String, Object> authState = new HashMap<>();
		authState.put("signedIn", !roles.contains("ROLE_ANONYMOUS"));
		authState.put("roles", roles);

		return authState;
	}

	private static List<String> getRoles() {
		return map(
			SecurityContextHolder.getContext().getAuthentication().getAuthorities(),
			GrantedAuthority::getAuthority);
	}
}