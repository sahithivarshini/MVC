package demo195;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	private static final Logger logger = LogManager.getLogger(HomeController.class);

	@RequestMapping("/")
	public String home(Model model) {
		logger.info("Home method accessed");
		model.addAttribute("message", "Hello, Log4j 2!");
		return "home";
	}

	@RequestMapping("/error")
	public String error() {
		logger.error("Error method accessed");
		throw new RuntimeException("Test exception");
	}
}
