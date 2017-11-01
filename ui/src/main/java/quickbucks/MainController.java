package quickbucks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.exception.ConstraintViolationException;

import quickbucks.User;
import quickbucks.UserRepository;
import quickbucks.MvcConfig;

@Controller    // This means that this class is a Controller
//@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
	
	
	@Autowired 
	private UserRepository userRepository;

	@GetMapping(path="/demo/adduser")
	public String addNewUser (@RequestParam String firstName
			, @RequestParam String email
			, @RequestParam String password) {
		//@TODO: add all params
		//@TODO: sanitize input. 
		if (! (email.matches(".+@columbia.edu") || 
			email.matches(".+@barnard.edu")))
			return "redirect:/index3.html";
			
		
		try {
			int uid = userRepository.findIDByEmail(email);
		} catch (Exception e) {
			User n = new User();
			n.setUserFirstName(firstName);
			n.setUserEmail(email);
			BCryptPasswordEncoder passwordEncoder = new 
				BCryptPasswordEncoder();
			String hashedPassword = 
				passwordEncoder.encode(password);
			n.setUserPassword(hashedPassword);
			try {
				userRepository.save(n);
			} catch (Exception ee) {
				return "redirect:/index3.html";
			}
			return "redirect:/index2.html";
		}
		
		return "redirect:/index3.html";
	}

	@GetMapping(path="/demo/alluser")
	public @ResponseBody Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
		return userRepository.findAll();
	}
	
	
	@Autowired
	private JobRepository jobRepository;
	
	@GetMapping(path="/demo/postJob") // Map ONLY GET Requests
	public String addNewJob (@RequestParam String jobtitle
			, @RequestParam String jobdesc, String category, String pay) {
		// @RequestParam means it is a parameter from the GET or POST request
		//@TODO: add tags (how are they input in form) and date
		//@TODO: add authentication and get current user so they
		//can be attached to the form. 
		
		Job j = new Job();
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());
		
		j.setUser(uid); 
		j.setJobTitle(jobtitle);
		j.setJobDescription(jobdesc);
		//j.setCategory(category);
/*		try{
			double payrate = Double.parseDouble(pay);
			j.setPay(payrate);
		}
		catch(NumberFormatException e){}
		*/
		
		//j.setStatus(0); //jobs are created as 'open'
		
		//set tags, date
		jobRepository.save(j);
		return "redirect:/homepageloggedin.html";
	}
	
	@GetMapping(path="/demo/alljob")
	public @ResponseBody Iterable<Job> getAllJobs() {
		// This returns a JSON or XML with the jobs
		return jobRepository.findAll();
	}
	
	@Autowired
	private RequestRepository requestRepository;
	
	
}





