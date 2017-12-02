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
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.util.UUID;
import java.lang.NullPointerException;

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
		if (s.length() > 255 && in != 6)
			return false;
		if (!(sanitizeString(s).equals(s)))
			return false;
		boolean ans = true;
		/*in = 1: email; in = 2: password, 3: name, 4: location
		 *5: non-empty*/
		switch(in) {
			case 1: ans = (!s.equals("") && (
				s.matches("[a-zA-z0-9\\.\\-]+@.*columbia.edu")
				||
				s.matches("[a-zA-z0-9\\.\\-]+@.*barnard.edu")));
				break;
			case 2: ans = (!s.equals("") && s.length() >= 4);
				break;
			case 3: ans = (s.matches("[a-zA-z\\s\\-]+"));
				break;
			case 4: ans = (s.equals("ON") || s.equals("OFF"));
				break;
			case 5: ans = (!s.equals(""));
				break;
			default: ans = true;
		}
		return ans;
	}

	/* sanitizeString(): remove semicolons and dashes from the given
	 * string */
	public String sanitizeString(String input)
	{
		String ans = input.replaceAll(";","");
		ans = ans.replaceAll("--","");
		return ans;
	}

	/* setRepositories(): for testing
	 */
	public void setReposotories(UserRepository u, JobRepository j,
		RequestRepository r, ReviewRepository re,
		ResetTokenRepository rt)
	{
			userRepository = u;
			jobRepository = j;
			requestRepository = r;
			reviewRepository = re;
			resetTokenRepository = rt;
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
		if (!(validateInputStrings(3,firstName) &&
			validateInputStrings(3,lastName) &&
			validateInputStrings(1,email) &&
			validateInputStrings(2,password) &&
			validateInputStrings(0,degree) &&
			validateInputStrings(4,location) &&
			validateInputStrings(0,school)))
			return "redirect:/index3.html";

		firstName = sanitizeString(firstName);
		lastName = sanitizeString(lastName);
		email = sanitizeString(email);
		password = sanitizeString(password);
		degree = sanitizeString(degree);
		location = sanitizeString(location);
		school = sanitizeString(school);

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
				return genericError();
			}
			return "redirect:/index2.html";
		}

		return "redirect:/index3.html";
	}

	@Autowired
	private JobRepository jobRepository;

	@GetMapping(path="/demo/postJob")
	public String addNewJob (@RequestParam String jobtitle
			, @RequestParam String jobdesc, String category)
	{
		if (!validateInputStrings(5, jobtitle) ||
			!validateInputStrings(6, jobdesc) ||
			!validateInputStrings(5, category))
			return genericError();

		jobtitle = sanitizeString(jobtitle);
		jobdesc = sanitizeString(jobdesc);
		if (jobdesc.length() > 1500 || jobdesc.equals(""))
			return genericError();
		category = sanitizeString(category);

		int uid = -1;
		Job j = new Job();
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			uid = userRepository.findIDByEmail(user.getUsername());
		} catch(Exception ee) {
			return genericError();
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
		try {
			jobRepository.save(j);
		} catch(Exception ee) {
			return genericError();
		}
		return "redirect:/homepageloggedin.html";
	}

	@GetMapping(path="/demo/search")
	public String searchJobs(ModelMap model, @RequestParam String keywords
			,@RequestParam String category)
	{
		keywords = sanitizeString(keywords);
		category = sanitizeString(category);

		List results = new ArrayList();
		if (keywords.equals("") && category.equals(""))
			results = jobRepository.findAllJobs();
		else if (keywords.equals(""))
			results = jobRepository.findJobByCat(category);
		else if (category.equals(""))
			results = jobRepository.findJobByTitle(keywords);
		else
			results = jobRepository.findJobByBoth(keywords, category);

		model.addAttribute("test", "hello");
		model.addAttribute("results", results);
		return "searchResults";
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

	@RequestMapping(value = "/employeeReview", method = RequestMethod.GET)
	public String lookupJobByID()
	{
		//org.springframework.security.core.userdetails.User user
			//= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();



		//and then from there getUsername() should get you the email



		return "jobByID";

   	}








   	@RequestMapping(value = "/finalPage", method = RequestMethod.GET)
   	public String viewJob(ModelMap model, @RequestParam String id)
	{
		id = sanitizeString(id);

	 	Job j = jobRepository.findJobByID(id);
	 	if (j == null) {
			model.addAttribute("jobID", id);
			return "viewJobError";
	 	}
	 	else{
			model.addAttribute("jobID", j.getId());
			model.addAttribute("title", j.getJobtitle());
			model.addAttribute("desc", j. getJobdesc());
			model.addAttribute("tags", j.getCategory());
	   	}

		return "viewJob";
	}


	@Autowired
	private RequestRepository requestRepository;

	//TODO make sure user does not request their own job
	@GetMapping(path="/demo/request") // Map ONLY GET Requests
	public String addNewJob (@RequestParam String id)
	{
		id = sanitizeString(id);

		Job j = jobRepository.findJobByID(id);
		if (j == null){
			System.out.println("job null");
			return "redirect:/searchJobsRF.html";
		}
		int emp = -1;
		try {
			emp = requestRepository.findAcceptedEmployee(id);
		} catch (Exception fae) {}
		if (emp != -1)
			return "redirect:/searchJobsRF.html"; //already requested

		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());

		/*FAIL CASES:
		requesting your own job. Should bounce you back to the search
		page with a message. also you cannot request something you
		have already requested.*/
		if (j.getUser() == uid)
			return "redirect:/searchJobsRF.html";
		Request rr;
		try {
			rr = requestRepository.findRequestByJobAndEmployee(j.getId() + "", uid);
		} catch (Exception rex) {
			return genericError();
		}
		if (rr != null){
			System.out.println("request already exists");
			return "redirect:/searchJobsRF.html";
		}

		Request r = new Request();
		r.setEmployee(uid);
		r.setTitle(j.getJobtitle());
		r.setEmployer(j.getUser());
		r.setJob(j.getId());
		r.setMsg("");

		try {
			requestRepository.save(r);
		} catch (Exception ee) {
			return genericError();
		}
		return "redirect:/requestSuccess.html";
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

	public String createReview(ModelMap model, @RequestParam String id)
	{
		id = sanitizeString(id);

	 	Job j = jobRepository.findJobByID(id);
	 	if (j ==null) {
			model.addAttribute("jobID", id);
			return "reviewJobError";
	 	}
		model.addAttribute("jobID", j.getId());
		model.addAttribute("title", j.getJobtitle());
		model.addAttribute("desc", j. getJobdesc());
		model.addAttribute("tags", j.getCategory());

		return "createReview";
	}

	@RequestMapping(value = "/createAReview", method = RequestMethod.GET)
	public String doReview(ModelMap model, @RequestParam String id)
	{
		model.addAttribute("errmsg",""); //clean page, no error message
		return createReview(model, id);
	}

	@Autowired
        public JavaMailSender emailSender;

	@GetMapping(path="/demo/review") // Map ONLY GET Requests
	public String addNewReview (ModelMap model, @RequestParam String id,
		@RequestParam String rating, @RequestParam String reviewBody)
	{
		id = sanitizeString(id);
	 	rating = sanitizeString(rating);
		reviewBody = sanitizeString(reviewBody);
		double rat = 0.0;

		if (reviewBody.length() > 1500 || reviewBody.length() < 10) {
			model.addAttribute("errmsg", "review must be between 10 and 100 characters");
			return createReview(model, id); //invalid input
		}

		try {
			rat = Double.parseDouble(rating);
		} catch (Exception eee) {
			model.addAttribute("errmsg", "rating must be numerical out of five");
			return createReview(model, id); //invalid input
		}
		model.addAttribute("errmsg","");

		Job j = new Job();
		try {
			j = jobRepository.findJobByID(id);
		} catch (Exception e) {
			return genericError();
		}
		Review r = new Review();
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());

		int accid;
		try {
			accid = requestRepository.findAcceptedEmployee(id);
		} catch (Exception ne) {
			/*no one has accepted this job; access denied*/
			return "redirect:/403.html";
		}
		if (accid != uid)
			return "redirect:/403.html"; /* someone else accepted */

		/*FAIL CASES:
		Review for this job has already been posted
		Cannot parse double for rating.
		reviewBody is too long
		you were not the person who was accepted for the job
		*/

		r.setEmployee(uid);
		r.setEmployer(j.getUser());
		r.setJob(j.getId());
		r.setReviewBody(reviewBody);
		r.setRating(rat);

		try {
			reviewRepository.save(r);
		} catch(Exception ee) {
			return genericError();
		}

		/*mark request as read*/
		Request req;
		try{
			req = requestRepository.findRequestByJobAndEmployee(id, uid);
		} catch (Exception rex) {
			return genericError();
		}
		if (req == null)
			return genericError(); /*something happened in the db to delete our record*/

		req.setEmployeeRead(true);
		try {
			requestRepository.save(req);
		} catch(Exception ree) {
			return genericError();
		}

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(userRepository.findEmailById(j.getUser()));
			message.setSubject("Review posted for job " +
				j.getId().toString() + " on Quickbucks");
			message.setText("Rating: " + rating + "\n\n\n"
				+ reviewBody);
			try {
				emailSender.send(message);
			} catch (NullPointerException ne) {}
		} catch (MailException exception) {
			exception.printStackTrace();
		}

		return "redirect:/reviewCreated.html";
	}

	@Autowired
	private ResetTokenRepository resetTokenRepository;

	@GetMapping(path="/sendForgotEmail")
	public String sendForgotEmail (@RequestParam String email)
	{
		email = sanitizeString(email);

		try {
			int uid = userRepository.findIDByEmail(email);
		} catch (Exception e) {
			//fail case: not in the respository
			return "redirect:/notregistered.html";
		}

		String token = UUID.randomUUID().toString();
		token = sanitizeString(token);
		ResetToken rt = new ResetToken();
		rt.setToken(token);
		rt.setUserEmail(email);
		try {
			resetTokenRepository.save(rt);
		} catch (Exception ee) {
			return genericError();
		}

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(email);
			message.setSubject("Reset password code from Quickbucks");
			message.setText(token);
			try {
				emailSender.send(message);
			} catch (NullPointerException ne) {}
		} catch (MailException exception) {
			exception.printStackTrace();
		}

		return "redirect:/inputToken.html";

	}

	@GetMapping(path="/checkReset")
	public String checkReset (ModelMap model, @RequestParam String email,
		@RequestParam String token)
	{
		email = sanitizeString(email);
	 	token = sanitizeString(token);

		ResetToken rt = resetTokenRepository.lookupRTByBoth(email, token);
		if (rt == null)
			return genericError();

		model.addAttribute("email", email);
		model.addAttribute("token", token);

		return "doReset";
	}

	@GetMapping(path="/demo/reset")
	public String doReset (@RequestParam String email, @RequestParam String
		token, @RequestParam String password)
	{
		email = sanitizeString(email);
	 	token = sanitizeString(token);
		password = sanitizeString(password);

		if (!(validateInputStrings(2,password))) {
			return genericError(); //need a better error...
			//probably something like doReset but with a
			//"your password must be xyz"
		}
		ResetToken rt = resetTokenRepository.lookupRTByBoth(email, token);
		if (rt == null)
			return genericError();

		resetTokenRepository.delete(rt);

		User u = new User();
		try {
			u = userRepository.findUserByEmail(email);
		} catch (Exception ee) {
			return genericError();
		}
		if (u == null)
			return genericError();

		BCryptPasswordEncoder passwordEncoder = new
			BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		u.setUserPassword(hashedPassword);

		try {
			userRepository.save(u);
		} catch(Exception eee) {
			return genericError();
		}
		return "redirect:/savedreset.html";
	}

	private void sendEmailToUser(String e, String subj, String body)
		throws MailException
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(e);
		message.setSubject(subj);
		message.setText(body);
		try {
			emailSender.send(message);
		} catch (NullPointerException ne) {}
	}

	@GetMapping(path="/demo/contact")
	public String contactStepOne (ModelMap model, @RequestParam String id)
	{
		id = sanitizeString(id);

		model.addAttribute("id",id);
		return "composeMessage";
	}

	@GetMapping(path="/demo/sendcontact")
	public String contactPoster (@RequestParam String id,
		@RequestParam String msg)
	{
	 	id = sanitizeString(id);
		msg = sanitizeString(msg);

		Job j = new Job();
		try {
			j = jobRepository.findJobByID(id);
		} catch(Exception e) {
			return genericError();
		}
	 	if (j ==null)
			return genericError();

		String empl = "";
		try {
			empl = userRepository.findEmailById(j.getUser());
		} catch(Exception ee) {
			return genericError(); //null job user
		}

		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();

		String msgbody = "Hi from Quickbucks!\n\nYou've received a new inquiry about your job " +
			id + " from a potential employee (" + username +
			"). Their message:\n" + msg + "\n\nLove,\nQuickbucks";

		try {
			sendEmailToUser(empl,
				"Contact from potential employee on Quickbucks",
				msgbody);
		} catch (MailException exception) {
			exception.printStackTrace();
			return genericError();
		}

		return "redirect:/sent.html";
	}

	@GetMapping(path="/notifications")
	public String displayNotifications(ModelMap model) {
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		int uid = userRepository.findIDByEmail(username);

		List<Request> notifs = requestRepository.getNotifsByUserID(uid);

		model.addAttribute("notifs", notifs);
		model.addAttribute("userr", uid);

		return "notifs";
	}

	@GetMapping(path="/read")
	public String readDecision(ModelMap model, @RequestParam Integer id)
	{
		Request r = requestRepository.findRequestByID(id);
		if (r == null)
			return genericError();
		int jid = r.getJob();

		Job j = jobRepository.findJobByID(jid + "");
		if (j == null)
			return genericError();

		org.springframework.security.core.userdetails.User meUser
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = meUser.getUsername();
		int uid = userRepository.findIDByEmail(username);

		if (r.getEmployee() != uid)
			return "redirect:/403.html"; //unauthorized
		r.setEmployeeRead(true);
		try {
			requestRepository.save(r);
		} catch(Exception eee) {}
		return "displayNotifications(model)";
	}

	@GetMapping(path="/decide")
	public String makeDecision(ModelMap model, @RequestParam Integer id,
		@RequestParam Integer decision)
	{
		/*error cases: decision not 1 or 2, job already decided, job
		 *not yours to decide (you are not employer), request does not
		 *exist */
		if (decision != 1 && decision != 2)
			return genericError();

		Request r = requestRepository.findRequestByID(id);
		if (r == null)
			return genericError();
		int jid = r.getJob();

		Job j = new Job();
		try {
			j = jobRepository.findJobByID(jid + "");
		} catch(Exception ee) {
			return genericError();
		}
		if (j == null)
			return genericError();

		org.springframework.security.core.userdetails.User meUser
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = meUser.getUsername();
		int uid = userRepository.findIDByEmail(username);

		if (r.getEmployer() != uid)
			return "redirect:/403.html"; /*unauthorized*/
		if (r.getDecision() != 0)
			return genericError();

		r.decide(decision);
		try {
			requestRepository.save(r);
		} catch (Exception eee) {
			return genericError();
		}
		if (decision == 1) {
			String empl = userRepository.findEmailById(r.getEmployee());
			String msgbody = "Hello!\nUser " + uid + " has accepted"
				+ " your request for job " + jid + ": "
				+ j.getJobtitle() + "\n\nCongrats!\n\nLove,\n"
				+ "Quickbucks";
			/*send an email to the user who has been accepted.*/
			try {
				sendEmailToUser(empl,
					"Quickbucks: Your Job Request",
					msgbody);
			} catch (MailException exception) {
				exception.printStackTrace();
			}
			/*then formally reject everyone else*/
			requestRepository.blanketReject(jid, id);
		}

		List<Request> notifs = requestRepository.getNotifsByUserID(uid);
		model.addAttribute("notifs", notifs);
		model.addAttribute("userr", uid);
		return "notifs";
	}

	public String genericError() {
		return "redirect:/generic-error.html";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
   	public String viewUser(ModelMap model, @RequestParam String id)
	{
	 	User u = userRepository.findByID(id);
	 	if(u == null){
			//TODO - change to user error
			model.addAttribute("jobID", id);
			return "viewJobError";
	 	}
	 	else{
			model.addAttribute("userID", u.getId());
			model.addAttribute("name", u.getUserFirstName()+u.getUserLastName());
			model.addAttribute("school", u.getUserSchool());
			model.addAttribute("degree", u.getUserDegree());
			model.addAttribute("location", u.getUserLocation());
	   	}
		//model.addAttribute("title", j.getJobTitle());

		return "viewUser";
	}
	
		@GetMapping(path="/demo/employeeReview")
	public String employeeReviewList(ModelMap model)
	{
		
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());

		List<Request> requests = new ArrayList();
		requests = requestRepository.findRequestsByEmployer(""+uid);
		
		for(int i = 0; i< requests.size(); i++){
			//check if review exists
			Request temp = requests.get(i);
			Integer jobID = temp.getJob();
			Review test = reviewRepository.lookupReviewByJobID(jobID);
			
			//jobID.toString());
			//if it does, remove from list
			if(test != null)
				requests.remove(i);
			
		}
		
		

		model.addAttribute("type", "Employees");
		model.addAttribute("results", requests);
		return "awaitingReview";
	}

}
