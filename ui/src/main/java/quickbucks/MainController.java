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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.ui.ModelMap;

import quickbucks.User;
import quickbucks.UserRepository;
import quickbucks.MvcConfig;
import quickbucks.ReviewRepository;
import quickbucks.Review;
import java.lang.Integer;
import java.util.List;
import java.util.ArrayList;

@Controller
public class MainController {

	private boolean validateInputStrings(int in, String s)
	{
		if (s.length() > 255)
			return false;
		boolean ans = true;
		//in = 1: email; in = 2: password, 3: name, 4: location
		//5: non-empty
		switch(in) {
			case 1: ans = (s != "" && (
				s.matches("[a-zA-z0-9\\.\\-]+@.*columbia.edu")
				||
				s.matches("[a-zA-z0-9\\.\\-]+@.*barnard.edu")));
				break;
			case 2: ans = (s != "" && s.length() >= 4);
				break;
			case 3: ans = (s.matches("[a-zA-z\\s\\-]+"));
				break;
			case 4: ans = (s.equals("ON") || s.equals("OFF"));
				break;
			case 5: ans = (s != "");
				break;
			default: ans = true;
		}
		return ans;
	}

	public void setReposotories(UserRepository u, JobRepository j,
		RequestRepository r) {
			userRepository = u;
			jobRepository = j;
			requestRepository = r;
	}


	@Autowired
	private UserRepository userRepository;

	@GetMapping(path="/demo/adduser")
	public String addNewUser (@RequestParam String firstName
			, @RequestParam String lastName
			, @RequestParam String email
			, @RequestParam String password
			, @RequestParam String degree
			, @RequestParam String location
			, @RequestParam String school)
	{
		//@TODO: which are required to be not blank?

		if (!(validateInputStrings(3,firstName) &&
			validateInputStrings(3,lastName) &&
			validateInputStrings(1,email) &&
			validateInputStrings(2,password) &&
			validateInputStrings(0,degree) &&
			validateInputStrings(4,location) &&
			validateInputStrings(0,school)))
			return "redirect:/index3.html";

		try {
			int uid = userRepository.findIDByEmail(email);
		} catch (Exception e) {
			User n = new User();
			n.setUserFirstName(firstName);
			n.setUserLastName(lastName);
			n.setUserEmail(email);
			n.setUserDegree(degree);
			n.setUserLocation(location);
			n.setUserSchool(school);
			BCryptPasswordEncoder passwordEncoder = new
				BCryptPasswordEncoder();
			String hashedPassword =
				passwordEncoder.encode(password);
			n.setUserPassword(hashedPassword);
			try {
				userRepository.save(n);
			} catch (Exception ee) {
				return "could_not_save";
				//return "redirect:/index3.html";
			}
			return "redirect:/index2.html";
		}

		return "redirect:/index3.html";
	}

	@GetMapping(path="/demo/alluser")
	public @ResponseBody Iterable<User> getAllUsers()
	{
		// This returns a JSON or XML with the users
		return userRepository.findAll();
	}


	@Autowired
	private JobRepository jobRepository;

	@GetMapping(path="/demo/postJob") // Map ONLY GET Requests
	public String addNewJob (@RequestParam String jobtitle
			, @RequestParam String jobdesc, String category)
	{
		// @RequestParam means it is a parameter from the GET or POST request
		//@TODO: add tags (how are they input in form) and date
		if (!validateInputStrings(5, jobtitle) ||
			!validateInputStrings(5, jobdesc) ||
			!validateInputStrings(5, category))
			return "redirect:/index3.html"; //change to better error page

		int uid = -1;
		Job j = new Job();
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			uid = userRepository.findIDByEmail(user.getUsername());
		} catch(Exception ee) {
			return "wrong_user";
		}

		j.setUser(uid);
		j.setJobTitle(jobtitle);
		j.setJobDescription(jobdesc);
		j.setCategory(category);
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


/*	@GetMapping(path="/demo/search") // Map ONLY GET Requests
	public @ResponseBody Iterable<Job> searchJobs (@RequestParam String keywords
			,@RequestParam String category)
	{
		//TODO add findAll
		if(keywords.equals(""))
			jobRepository.findJobByCat(category);
		if(category.equals(""))
			return jobRepository.findJobByTitle(keywords);
		return jobRepository.findJobByBoth(keywords, category);
	}
*/

	@GetMapping(path="/demo/search") // Map ONLY GET Requests
	public String searchJobs(ModelMap model, @RequestParam String keywords
			,@RequestParam String category)
	{
		List results = new ArrayList();

		if(keywords.equals("") && category.equals("")){
			results = jobRepository.findAllJobs();
		}
		else if(keywords.equals("")){
			results = jobRepository.findJobByCat(category);

		}
		else if(category.equals("")){
			results = jobRepository.findJobByTitle(keywords);
		}
		else
			results = jobRepository.findJobByBoth(keywords, category);

		model.addAttribute("test", "hello");
		model.addAttribute("results", results);

		return "searchResults";


	}


	@GetMapping(path="/demo/alljob")
	public @ResponseBody Iterable<Job> getAllJobs()
	{
		// This returns a JSON or XML with the jobs
		return jobRepository.findAll();
	}


	/*@GetMapping(path="/demo/viewjob")
	public @ResponseBody ModelAndView viewJob(@RequestParam String id) {
		ModelAndView blep=new ModelAndView();
		blep.setViewName("redirect:viewJob");
		Job j = findOne(1);
		redir.addFlashAttribute("title", j.getJobTitle());


		return jobRepository.findAll();
	}
	*/

	@RequestMapping(value = "/jobByID", method = RequestMethod.GET)
	public String lookupJobByID()
	{
		return "jobByID";
   	}

   	/*@RequestMapping(value = "/redirect", method = RequestMethod.GET)
   	public String redirect()
	{
		return "redirect:finalPage";
	}
*/
   	@RequestMapping(value = "/finalPage", method = RequestMethod.GET)
   	public String viewJob(ModelMap model, @RequestParam String id)
	{
	 	Job j = jobRepository.findJobByID(id);
	 	if(j ==null){
			model.addAttribute("jobID", id);
			return "viewJobError";
	 	}
	 	else{
			model.addAttribute("jobID", j.getId());
			model.addAttribute("title", j.getJobtitle());
			model.addAttribute("desc", j. getJobdesc());
			model.addAttribute("tags", j.getCategory());
	   	}
		//model.addAttribute("title", j.getJobTitle());

		return "viewJob";
	}


	@Autowired
	private RequestRepository requestRepository;

	//TODO make sure user does not request their own job
	@GetMapping(path="/demo/request") // Map ONLY GET Requests
	public String addNewJob (@RequestParam String id)
	{
		Job j = new Job();
		try {
			j = jobRepository.findJobByID(id);
		} catch (Exception e) {
			//reidrect to something else here since it is fail case
		}
		Request r = new Request();
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());

		/*FAIL CASES:
		requesting your own job.
		if (j.getUser() == uid)
			return "redirect:/requestFail.html";
		*/

		r.setEmployee(uid);
		r.setTitle(j.getJobtitle());
		r.setEmployer(j.getUser());
		r.setJob(j.getId());
		r.setMsg("");

		requestRepository.save(r);
		return "redirect:/requestSuccess.html";
	}

	@GetMapping(path="/demo/allrequest")
	public @ResponseBody Iterable<Request> getAllReqs()
	{
		// This returns a JSON or XML with the jobs
		return requestRepository.findAll();
	}

	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request,
		HttpServletResponse response)
	{
		Authentication auth =
			SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request,
				response, auth);
		}
		return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}


	@Autowired
	private ReviewRepository reviewRepository;

	@RequestMapping(value = "/createAReview", method = RequestMethod.GET)
   	public String createReview(ModelMap model, @RequestParam String id)
	{
	 	Job j = jobRepository.findJobByID(id);
	 	if(j ==null){
			model.addAttribute("jobID", id);
			return "viewJobError"; //@TODO: separate error
	 	}
	 	else{
			model.addAttribute("jobID", j.getId());
			model.addAttribute("title", j.getJobtitle());
			model.addAttribute("desc", j. getJobdesc());
			model.addAttribute("tags", j.getCategory());
	   	}
		//model.addAttribute("title", j.getJobTitle());

		return "createReview";
	}


	@GetMapping(path="/demo/review") // Map ONLY GET Requests
	public String addNewReview (@RequestParam String id,
		@RequestParam String rating, @RequestParam String reviewBody)
	{
		//@TODO: make sure given user is authorized to write a review
		//AKA they were the accepted user for a job
		Job j = new Job();
		try {
			j = jobRepository.findJobByID(id);
		} catch (Exception e) {
			//reidrect to something else here since it is fail case
		}
		Review r = new Review();
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());

		/*FAIL CASES:
		Review for this job has already been posted
		Cannot parse double for rating.
		reviewBody is too long
		*/

		r.setEmployee(uid);
		r.setEmployer(j.getUser());
		r.setJob(j.getId());
		r.setReviewBody(reviewBody);
		r.setRating(0.0);

		try {
			reviewRepository.save(r);
		} catch(Exception ee) {
			//redirect to could_not_save
		}
		return "redirect:/requestSuccess.html"; //@TODO: new success page
	}

	@GetMapping(path="/error")
	public String genericError() {
		return "redirect:/generic-error.html";
	}



}
