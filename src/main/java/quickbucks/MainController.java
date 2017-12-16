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
	public String addNewJob (ModelMap model, @RequestParam String jobtitle
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
		model.addAttribute("user", uid);
		model.addAttribute("userEmail", user.getUsername());
		model.addAttribute("jobID", j.getId());
		model.addAttribute("title", j.getJobtitle());
		model.addAttribute("desc", j. getJobdesc());
		model.addAttribute("tags", j.getCategory());

		return "viewJob";
	}

	@GetMapping(path="/demo/search")
	public String searchJobs(ModelMap model, @RequestParam String keywords,
	@RequestParam String category, @RequestParam String pageNum)
	{
		keywords = sanitizeString(keywords);
		category = sanitizeString(category);
		pageNum = sanitizeString(pageNum);

		String keynull = "%"+keywords+"%";
		String catnull = "%"+category+"%";
		int limit = 10;

		Integer count = 1;
		try {
			count = jobRepository.searchCount(keywords, category);
		} catch (Exception e) {
			count = 1;
		}
		int maxPage = count/limit;
		maxPage++; /*limit 1, x; is ok*/

		int page = 1;
		try{
			page = Integer.parseInt(pageNum);
		}catch(NumberFormatException e){
			System.out.println("parse error");
			return genericError();
		}

		if (page < 1)
			page = 1;
		if(page > maxPage)
			page = maxPage;

		int start = (page-1)*limit;

		List results = new ArrayList();
		boolean isAll = false;
		if (keywords.equals("") && category.equals("")){
			results = jobRepository.findAllJobs(start, limit);
			isAll = true;
		}
		else if (keywords.equals(""))
			keynull = "%";
		else if (category.equals(""))
			catnull = "%";

		if(!isAll) {
			results = jobRepository.findJobByBoth(keynull, catnull, start, limit);
		}

		int prev = page-1;
		if(prev <=0)
			prev = 1;
		int next = page+1;
		if(next > maxPage)
			next = maxPage;

		model.addAttribute("test", "hello");
		model.addAttribute("results", results);
		model.addAttribute("currPage", page + "");
		model.addAttribute("key",keywords);
		model.addAttribute("cat",category);

		model.addAttribute("prev",""+prev);
		model.addAttribute("next",""+next);
		model.addAttribute("max",""+maxPage);
		return "searchResults";
	}

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
			String email = userRepository.findEmailById(j.getUser());
			model.addAttribute("user", j.getUser());
			model.addAttribute("userEmail", email);
			model.addAttribute("jobID", j.getId());
			model.addAttribute("title", j.getJobtitle());
			model.addAttribute("desc", j. getJobdesc());
			model.addAttribute("tags", j.getCategory());
	   	}

		return "viewJob";
	}


	@Autowired
	private RequestRepository requestRepository;

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
		return "logoutPage";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
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
		if (accid != uid && uid != j.getUser())
			return "redirect:/403.html"; /* someone else accepted */

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
		if (rat < 0.0 || rat > 5.0) {
			model.addAttribute("errmsg", "rating must be numerical between 0 and five");
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
		if (accid != uid && uid != j.getUser())
			return "redirect:/403.html"; /* someone else accepted */

		/*FAIL CASES:
		Review for this job has already been posted
		Cannot parse double for rating.
		reviewBody is too long
		you were not the person who was accepted for the job or employer
		*/
		r.setEmployee(accid);
		r.setEmployer(j.getUser());
		r.setAuthor(uid);
		r.setJob(j.getId());
		r.setReviewbody(reviewBody);
		r.setRating(rat);

		try {
			reviewRepository.save(r);
		} catch(Exception ee) {
			return genericError();
		}

		/*mark request as read*/
		Request req;
		try {
			req = requestRepository.findRequestByJobAndEmployee(id, accid);
		} catch (Exception rex) {
			return genericError();
		}
		if (req == null)
			return genericError();

		if (uid == accid)
			req.setEmployeeRead(true);
		else
			req.setEmployerRead(true);

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
	public String contactPoster(@RequestParam String id,
		@RequestParam String msg)
	{
	 	id = sanitizeString(id);
		msg = sanitizeString(msg);

		if (msg.length() > 1500 || msg.length() < 10) {
			return genericError();
		}

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
		if (username.equals(empl))
			return genericError(); //can't send email to self

		String msgbody = "Hi from Quickbucks!\n\nYou've received a new inquiry about your job " +
			id + " from a potential employee (" + username +
			"). Their message:\n" + msg + "\n\nLove,\nQuickbucks";

		try {
			sendEmailToUser(empl,
				"Contact from potential employee on Quickbucks",
				msgbody);
		} catch (MailException exception) {
			exception.printStackTrace();
			//return genericError()?
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
		return displayNotifications(model);
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
			model.addAttribute("jobID", id);
			return "viewJobError";
	 	}
	 	else{
			List reviews = new ArrayList();
			reviews = reviewRepository.getUserReviews(u.getId());
			//System.out.println(reviews.size());
			//System.out.println(u.getId());
			model.addAttribute("userID", u.getId());
			model.addAttribute("name", u.getUserFirstName()+" " +u.getUserLastName());
			model.addAttribute("school", u.getUserSchool());
			model.addAttribute("degree", u.getUserDegree());
			model.addAttribute("location", u.getUserLocation());
			model.addAttribute("reviews", reviews);
	   	}
		//model.addAttribute("title", j.getJobTitle());

		return "viewUser";
	}

	@RequestMapping(value = "/myprofile", method = RequestMethod.GET)
   	public String viewUser(ModelMap model)
	{
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid;
		try {
			uid = userRepository.findIDByEmail(user.getUsername());
		} catch (NullPointerException npe) {
			model.addAttribute("jobID", "-1");
			return "viewJobError";
		}
	 	User u = userRepository.findByID(uid+"");
	 	if(u == null){
			String hello = "" + uid + "";
			model.addAttribute("jobID", hello);
			return "viewJobError";
	 	}
	 	else{
			List reviews = new ArrayList();
			reviews = reviewRepository.getUserReviews(u.getId());
			System.out.println(reviews.size());
			System.out.println(u.getId());
			model.addAttribute("userID", u.getId());
			model.addAttribute("name", u.getUserFirstName()+" " +u.getUserLastName());
			model.addAttribute("school", u.getUserSchool());
			model.addAttribute("degree", u.getUserDegree());
			model.addAttribute("location", u.getUserLocation());
			model.addAttribute("reviews", reviews);
	   	}
		//model.addAttribute("title", j.getJobTitle());

		return "viewUser";
	}

	@GetMapping(path="/demo/employeeReview")
	public String employeeReviewList(ModelMap model)
	{
		//System.out.println("herhehreherherh");
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());

		List<Request> requests = requestRepository.findRequestsByEmployer(""+uid);
		System.out.println(requests.size());
		for(int i = 0; i< requests.size(); i++){
			//check if review exists
			Request temp = requests.get(i);
			Integer jobID = temp.getJob();

			Review test;
			try {
				test = reviewRepository.lookupReviewByJobAndAuthor(jobID, uid);
			} catch (Exception e) {
				//multiple matches
				test = new Review();
			}
			//jobID.toString());
			//if it does, remove from list
			if(test != null)
				requests.remove(i);

		}

		model.addAttribute("type", "Employees");
		model.addAttribute("results", requests);
		return "awaitingReview";
	}

	@GetMapping(path="/demo/openjobs")
	public String employerJobs(ModelMap model)
	{
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());

		List<JobDecision> openJobs = new ArrayList<JobDecision>();
		List<Integer> searchedJobs = new ArrayList<Integer>();
		List<Request> requests = requestRepository.findRequestsByEmployer(""+uid);

		for(int i = 0; i< requests.size(); i++){
			//include if decision not made
			Request temp = requests.get(i);
			if(searchedJobs.contains(temp.getJob()))
				continue;

			if(temp.getDecision() == 0)
				openJobs.add(new JobDecision(temp.getJob(), 0, temp.getTitle()));
			else{
				Review test;
				try {
					test = reviewRepository.lookupReviewByJobAndAuthor(temp.getJob(), uid);
				} catch (Exception e) {
					//multiple matches
					test = new Review();
				}
				if(test == null)
					openJobs.add(new JobDecision(temp.getJob(), 1, temp.getTitle()));
			}
			searchedJobs.add(temp.getJob());
		}

		List<Job> allJobs = new ArrayList<Job>();
		allJobs = jobRepository.findAllUsersJobs(uid);
		for(int i = 0; i< allJobs.size(); i++){
			Job temp = allJobs.get(i);
			if(searchedJobs.contains(temp.getId())){
				continue;
			}
			else{
				openJobs.add(new JobDecision(temp.getId(), 0, temp.getJobtitle()));
			}
		}

		model.addAttribute("jobs", openJobs);
		return "openjobs";
	}

	@RequestMapping(value = "/cancelJob", method = RequestMethod.GET)
   	public String cancelJob(ModelMap model, @RequestParam String id)
	{
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());
		Job temp;
		try {
			temp = jobRepository.findJobByID(id);
		} catch(Exception ee) {
			System.out.println("LOOKUP"+id);
			return genericError();

		}
		if(temp == null){
			System.out.println("NULL"+id);
			return genericError();

		}

		else{
			if(temp.getUser() != uid)
				model.addAttribute("msg", "Error: cannot delete someone else's job");
			else{


				try{
					jobRepository.delete(temp);
					List<Request> requests2 = requestRepository.findRequestsByJob(id);
					for(int i = 0; i<requests2.size(); i++){
						requestRepository.delete(requests2.get(i));
					}
				} catch(Exception ee) {
				System.out.println("DELETE"+id);
				return genericError();
				}
				model.addAttribute("msg", "Job successfully deleted.");
			}
		}

		return "cancel";

	}

		@GetMapping(path="/demo/employerReview")
	public String employerReviewList(ModelMap model)
	{

		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());

		List<Request> requests = requestRepository.findRequestsByEmployee(""+uid);
		System.out.println(requests.size());
		for(int i = 0; i< requests.size(); i++){
			//check if review exists
			Request temp = requests.get(i);
			Integer jobID = temp.getJob();

			Review test;
			try {
				test = reviewRepository.lookupReviewByJobAndAuthor(jobID, uid);
			} catch (Exception e) {
				//multiple matches
				test = new Review();
			}

			//jobID.toString());
			//if it does, remove from list
			if(test != null)
				requests.remove(i);

		}

		model.addAttribute("type", "Employeers");
		model.addAttribute("results", requests);
		return "awaitingReview";
	}


		@GetMapping(path="/demo/upcoming")
	public String employeeAccepted(ModelMap model)
	{
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());



		List<Request> requests = requestRepository.findRequestsByEmployee(""+uid);
			for(int i = 0; i<requests.size(); i++){
				Request temp = requests.get(i);
				Integer jobID = temp.getJob();

					Review test;
					try {
						test = reviewRepository.lookupReviewByJobAndAuthor(jobID, uid);
					} catch (Exception e) {
						//multiple matches
						test = new Review();
					}

					if(test != null)
						requests.remove(i);
			}



		model.addAttribute("title", "Hired Jobs");
		model.addAttribute("jobs", requests);
		return "employeeJobs";
	}

			@GetMapping(path="/demo/pending")
	public String employeePending(ModelMap model)
	{
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());



		List<Request> requests = requestRepository.findRequestsByEmployeeUndecided(""+uid);


		model.addAttribute("title", "Pending Requests");
		model.addAttribute("jobs", requests);
		return "employeeJobs";
	}

	@RequestMapping(value = "/cancelReq", method = RequestMethod.GET)
   	public String cancelReq(ModelMap model, @RequestParam String id)
	{
		org.springframework.security.core.userdetails.User user
			= (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int uid = userRepository.findIDByEmail(user.getUsername());
		Request temp;
		try {
			temp = requestRepository.findRequestByIDString(id);
		} catch(Exception ee) {
			System.out.println("LOOKUP"+id);
			return genericError();
		}
		if(temp == null)
			return genericError();

		if(temp.getEmployee()!= uid)
			model.addAttribute("msg", "Error: cannot delete someone else's request");
		else if(temp.getDecision() == 1){
			Integer jobID = temp.getJob();
			Review test;
			try {
				test = reviewRepository.lookupReviewByJobID(jobID);
			} catch (Exception e) {
				//multiple matches
				test = new Review();
			}
			if(test != null)
				model.addAttribute("msg", "Error: cannot delete request for a completed job!");
			else{
				try{
				requestRepository.delete(temp);
				} catch(Exception ee) {
				return genericError();
				}
				model.addAttribute("msg", "Request successfully deleted.");
			}
		}
		else{
			try{
				requestRepository.delete(temp);
			} catch(Exception ee) {
			return genericError();
			}
			model.addAttribute("msg", "Request successfully deleted.");
		}
		return "cancel";

	}


}
