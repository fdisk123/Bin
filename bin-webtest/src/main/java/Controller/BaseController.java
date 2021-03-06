package Controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/*", "/*/**", "/", "" })
@Scope("prototype")
public class BaseController {

	@RequestMapping("{path}")
	public ModelAndView basePath(HttpServletRequest request, HttpServletResponse response, @PathVariable("path") String path) throws IOException {
		String url = request.getParameter("url");
		if ("proxy".equals(path)) {
			return new ModelAndView("proxy", getParams(request)).addObject("data", JspProxy.getProxy(url));
		}
		return new ModelAndView(path, getParams(request));
	}

	protected final Map<String, Object> getParams(HttpServletRequest request) {
		Enumeration<String> en = request.getParameterNames();
		Map<String, Object> map = new HashMap<String, Object>();
		String name;
		while (en.hasMoreElements()) {
			name = en.nextElement();
			map.put(name, request.getParameter(name));
		}
		return map;
	}

}
