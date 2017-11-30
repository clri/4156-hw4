package quickbucks;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import quickbucks.User;
import quickbucks.TestRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration //(classes = {WebSecurityConfig.class, MvcConfig.class})
@SpringBootTest(classes = Application.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
//@WebAppConfiguration
public class MainControllerTest {
        //@Autowired

        private MainController mainController;

        @Autowired
        private UserRepository u;

        @Autowired
        private JobRepository j;

        @Autowired
        private RequestRepository r;

        @Autowired
        private ReviewRepository re;

        @Autowired
        private ResetTokenRepository rt;

        @Autowired
        private TestRepository tst;

        private ModelMap model;

        public int getAJob() {
                int i = 1;
                Job job = null;

                while (job == null)
                        job = j.findJobByID(i++ + "");
                return i - 1;
        }
        public int getARequest() {
                int i = 1;
                Request req = null;

                while (req == null)
                        req = r.findRequestByID(i++);
                return i - 1;
        }

        @Before
        public void setup() {
                mainController = new MainController();
                mainController.setReposotories(u, j, r, re, rt);
                model = new ModelMap();
        }
        @After
        public void verify() {
                //Mockito.reset();
        }

        /*run this first; clean up the DB*/
        @Test
        public void cleanDB() throws Exception {
                tst.deleteRequests();
                tst.deleteTokens();
                tst.deleteReviews();
                tst.deleteUsers();
                tst.deleteJobs();
                this.mainController.addNewUser(
                        "john", "secret", "johnsecretzz@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                this.mainController.addNewUser(
                        "john", "secret", "johnsecret82@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
        }

        //sanitizing strings: test to pass
        @Test
        public void testSanitize1() throws Exception {
                String ans = mainController.sanitizeString("abcd");
                assertEquals(ans,"abcd");
        }
        @Test
        public void testSanitize2() throws Exception {
                String ans = mainController.sanitizeString("abc d");
                assertEquals(ans,"abc d");
        }
        @Test
        public void testSanitize3() throws Exception {
                String ans = mainController.sanitizeString("abc-d");
                assertEquals(ans,"abc-d");
        }
        @Test
        public void testSanitize4() throws Exception {
                String ans = mainController.sanitizeString("abc--d");
                assertEquals(ans,"abcd");
        }
        @Test
        public void testSanitize5() throws Exception {
                String ans = mainController.sanitizeString("abc;d;;;");
                assertEquals(ans,"abcd");
        }

        //REGISTRATION
        //test to pass
        //first name capitalized

        @Test
        public void testRegister1() throws Exception {
                String view = this.mainController.addNewUser(
                        "John", "Secret", "john@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //first name all lowercase
        @Test
        public void testRegisterLowerF() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "Secret", "johns@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //boundary: first name 255 characters
        @Test
        public void testRegisterMaxLenF() throws Exception {
                String view = this.mainController.addNewUser(
                        "sec etlksdjflsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosl",
                        "Secret", "johns1@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }

        //last name capitalized
        @Test
        public void testRegister1L() throws Exception {
                String view = this.mainController.addNewUser(
                        "John", "Secret", "johnsecret@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //last name all lowercase
        @Test
        public void testRegisterLowerL() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "johnsec@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //boundary: last name 255 characters
        @Test
        public void testRegisterMaxLenL() throws Exception {
                String view = this.mainController.addNewUser(
                        "Secret",
                        "sec etlksdjflsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosl",
                        "johns0@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }

        //@columbia.edu email
        @Test
        public void testRegisterEmail1() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "johnsec4444@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //@barnard.edu email
        @Test
        public void testRegisterEmail2() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "johnsec4444@barnard.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //@cs.columbia.edu email
        @Test
        public void testRegisterEmail3() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "johnsec4444@cs.columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //255-char email
        @Test
        public void testRegisterEmailLong1() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "senetlkfsdjflsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdi@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //email with dash
        @Test
        public void testRegisterEmail4() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "johnsec-4444@cs.columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }

        //ON campus
        @Test
        public void testLocation1() throws Exception {
                String view = this.mainController.addNewUser(
                        "jo-hn", "sec et", "john343432@columbia.edu",
                        "abcde", "MS", "OFF", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //OFF campus
        @Test
        public void testLocation2() throws Exception {
                String view = this.mainController.addNewUser(
                        "jo-hn", "sec et", "johnsec43et2@columbia.edu",
                        "abcde", "MS", "OFF", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }

        //=4 password
        @Test
        public void testRegisterPasswordShort1() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "john15@columbia.edu",
                        "1234", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //255 password
        @Test
        public void testRegisterPasswordLong1() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "john15@cs.columbia.edu",
                        "secnetlkfsdjfsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdi@columbia.edu",
                        "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }



        //test to FAIL
        //blank first name
        @Test
        public void testRegisterEmptyF() throws Exception {
                String view = this.mainController.addNewUser(
                        "", "Secret", "johsaasdfasdfsns@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }
        //first name 256 characters
        @Test
        public void testRegisterTooLongF() throws Exception {
                String view = this.mainController.addNewUser(
                        "sec etlkfsdjflsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosl",
                        "Secret", "johns11@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }

        //blank last name
        @Test
        public void testRegisterEmpty() throws Exception {
                String view = this.mainController.addNewUser(
                        "F", "", "johns11@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }
        //last name 256 characters
        @Test
        public void testRegisterTooLongL() throws Exception {
                String view = this.mainController.addNewUser(
                        "Secret",
                        "sec etlkfsdjflsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosl",
                        "johns11@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }

        //duplicate @columbia.edu email
        @Test
        public void testRegisterEmail1Duplicate() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "johnsec4444@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }
        //blank @columbia.edu email
        @Test
        public void testRegisterEmail1Blank() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }
        //blank email
        @Test
        public void testRegisterEmail2Blank() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }
        //wrong domain email
        @Test
        public void testRegisterEmail2Wrong() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "john@gmail.com",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }
        //256-char email
        @Test
        public void testRegisterEmailLong() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "secnetlkfsdjflsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdi@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }
        //invalid character in email
        @Test
        public void testRegisterEmailSpec() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "johns$ec4444@cs.columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }

        //not ON/OFF in on/off campus
        @Test
        public void testRegisterOnOffFail() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "sec et", "johns11@columbia.edu",
                        "abcde", "MS", "fff", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }

        //blank password
        @Test
        public void testRegisterPasswordBlank() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "john11@columbia.edu",
                        "", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }
        //<4 password
        @Test
        public void testRegisterPasswordShort() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "john11@columbia.edu",
                        "abc", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }
        //256 password
        @Test
        public void testRegisterPasswordLong() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "john11@columbia.edu",
                        "secnetlkfsdjflsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdi@columbia.edu",
                        "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html");
        }


        //JOB CREATION
        //TEST TO PASS

        //average job
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJob1() throws Exception {
                String view = this.mainController.addNewJob(
                        "job", "a job", "jobs");
                assertEquals(view,"redirect:/homepageloggedin.html");
        }
        //name = 255
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJob2() throws Exception {
                String view = this.mainController.addNewJob(
                        "sec etlksdjflsdkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaos",
                        "a job", "jobs");
                assertEquals(view,"redirect:/homepageloggedin.html");
        }
        //descrition = 255
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJob3() throws Exception {
                String view = this.mainController.addNewJob(
                        "job",
                        "REO9s0Q9SsIU62Y75OnkztWjsOP0kyukpwIYPoFLSlpuvvrtgghQ8GO5NB6pXKv2pNAES7xLLkEjvmHWXcxlmwkIMaXoB2Ui17vLfm7z72u6beRmthEz609UFJ8mP9uhcyFo5NnKo0ml7bSqjSc8YQK34lryOOfSRKnULY3eoTOlylv9twV4PnJRRazD1EBgDDyJJD75pYtmWPjfJPyKJRQ9nBbIRFBuyqpsg2L9eCX0pNjVNNI4S4PRPkYYU9lWrJGDbgUKV23f3exeFhubU2eiVlIGI7gwPn14m96HwkIcnwnq1TmZwWlYOsCq8nkN3I3iEP2RxYc0xqHiAqXjUtzgEsbFT4pUrPFL9nSTQiM64Jk78voJpjbuFSapIhGbiGsSU78Fz12nP1G20mYvXFr7Yy4uz7VEEvnlL27VKq0IWs8siA1oiePHUP19qRyIKzkGTEbHD9MOypaloZNGzP3zYOaxJNykfW1JHt9c1rv5pcNLqL7Ah9bHUwa3MrPDBSH6F33zKt4AOQRKEqXxiCWn7wk0Ib9QenVVWZ8g3uQLx3pSzyQoTo402QIUk2Pt2LxMq6iqaW9pIIn0zANJWqKxe1Nh8gOkC5gr1fzl8GI2DibQhZ8jwAanCAIgDG7UegF1IEThSVnP9rr0iHEcApuPoprVcqcRAD5nnkFNlAY9MWVtglA9HHSZbnK5qwYZaYt1x9b6oVx64SENJ85gZ8T819cqQOqcsSiYuiqQ0sB4sbJOTniLB7CxqLe6BNGa19BpwEEqn8VPBXkw6a2ROxa9mUoWByBaxDcYbw9MYaYIg3A1pG3Pnf21cSQor6RgFGlmw1zLAGhlYN8YIiEtG7cMbcm6XLDDTyMpfIXmcU89NQg0PNVvHkF7fYRo5RKIei3V8iWGpJhSk5F0geZ7oBykvVjhroV9jFzXSkV7bhCNUO6qUMICAB3uyuWY4jwXxl4ZpLh8hO2PI8ZrQpf7Yq3EmEHEiHJ1aezj2FQjkCnx1a6pFWEoRiGGxeY9slUIP1uEXK2XWY9kicAfX6iFq2ilke9xPDBnuVCWrQ2Yv1J3mPPtLrehMLPB1t3oslhFG6LZXkON5gvEalqztQ9S9TOZpiBcX7O4O2c0cgH3ffueADtxJeUkM42jI34aY3bxLcnJIm1cxbeSu5XxZbLagZmVggzVeAopG6DwQsl1r7GhSzGyhfbe8ER05XUaKgrqvgbDuZgzw3Dr1OUUUgY2UJyKqCOxc4Se5hkF3BkwLBVOq5pS4ZEwiRyQNwWpBUBmX1gIWlfD6t3fDR9qKV6DtP7fJiiULQjLUQFrVQwEDo3ZovXaAtDPjqyqxU7Qhl5a74GW4ruoCgUalmgQkjDeC5cvCB65ZFyTUl6SbcfRktBsyD0LwDM5gGWpVtpjnghYm3j31GqawTjXvQ4MkJLWDlNj08ytp9x1VB3t5Qt80asqSmZ2RuPe8HxCFUckepP42stqQo7t5aUsiixJ4QhRLvUbBKPg",
                        "jobs");
                assertEquals(view,"redirect:/homepageloggedin.html");
        }
        //category = 255
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJob4() throws Exception {
                String view = this.mainController.addNewJob(
                        "job", "a job",
                        "sec etlksdjflsdkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaos");
                assertEquals(view,"redirect:/homepageloggedin.html");
        }


        //TEST TO FAIL
        //name = 256
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJobF1() throws Exception {
                String view = this.mainController.addNewJob(
                        "sec etlksdjflsddkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaos",
                        "a job", "jobs");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //description = 256
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJobF2() throws Exception {
                String view = this.mainController.addNewJob(
                        "job",
                        "REO9s0Q9SsIU62Y75OnkztWjsOP0kyukpwIYPoFLSlpuvvrtgghQ8GO5NB6pXKv2pNAES7xLLkEjvmHWXcxlmwkIMaXoB2Ui17vLfm7z72u6beRmthEz609UFJ8mP9uhcyFo5NnKo0ml7bSqjSc8YQK34lryOOfSRKnULY3eoTOlylv9twV4PnJRRazD1EBgDDyJJD75pYtmWPjfJPyKJRQ9nBbIRFBuyqpsg2L9eCX0pNjVNNI4S4PRPkYYU9lWrJGDbgUKV23f3exeFhubU2eiVlIGI7gwPn14m96HwkIcnwnq1TmZwWlYOsCq8nkN3I3iEP2RxYc0xqHiAqXjUtzgEsbFT4pUrPFL9nSTQiM64Jk78voJpjbuFSapIhGbiGsSU78Fz12nP1G20mYvXFr7Yy4uz7VEEvnlL27VKq0IWs8siA1oiePHUP19qRyIKzkGTEbHD9MOypaloZNGzP3zYOaxJNykfW1JHt9c1rv5pcNLqL7Ah9bHUwa3MrPDBSH6F33zKt4AOQRKEqXxiCWn7wk0Ib9QenVVWZ8g3uQLx3pSzyQoTo402QIUk2Pt2LxMq6iqaW9pIIn0zANJWqKxe1Nh8gOkC5gr1fzl8GI2DibQhZ8jwAanCAIgDG7UegF1IEThSVnP9rr0iHEcApuPoprVcqcRAD5nnkFNlAY9MWVtglA9HHSZbnK5qwYZaYt1x9b6oVx64SENJ85gZ8T819cqQOqcsSiYuiqQ0sB4sbJOTniLB7CxqLe6BNGa19BpwEEqn8VPBXkw6a2ROxa9mUoWByBaxDcYbw9MYaYIg3A1pG3Pnf21cSQor6RgFGlmw1zLAGhlYN8YIiEtG7cMbcm6XLDDTyMpfIXmcU89NQg0PNVvHkF7fYRo5RKIei3V8iWGpJhSk5F0geZ7oBykvVjhroV9jFzXSkV7bhCNUO6qUMICAB3uyuWY4jwXxl4ZpLh8hO2PI8ZrQpf7Yq3EmEHEiHJ1aezj2FQjkCnx1a6pFWEoRiGGxeY9slUIP1uEXK2XWY9kicAfX6iFq2ilke9xPDBnuVCWrQ2Yv1J3mPPtLrehMLPB1t3oslhFG6LZXkON5gvEalqztQ9S9TOZpiBcX7O4O2c0cgH3ffueADtxJeUkM42jI34aY3bxLcnJIm1cxbeSu5XxZbLagZmVggzVeAopG6DwQsl1r7GhSzGyhfbe8ER05XUaKgrqvgbDuZgzw3Dr1OUUUgY2UJyKqCOxc4Se5hkF3BkwLBVOq5pS4ZEwiRyQNwWpBUBmX1gIWlfD6t3fDR9qKV6DtP7fJiiULQjLUQFrVQwEDo3ZovXaAtDPjqyqxU7Qhl5a74GW4ruoCgUalmgQkjDeC5cvCB65ZFyTUl6SbcfRktBsyD0LwDM5gGWpVtpjnghYm3j31GqawTjXvQ4MkJLWDlNj08ytp9x1VB3t5Qt80asqSmZ2RuPe8HxCFUckepP42stqQo7t5aUsiixJ4QhRLvUbBKPgk",
                        "jobs");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //category = 256
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJobF3() throws Exception {
                String view = this.mainController.addNewJob(
                        "job", "a job",
                        "sec etlkfsdjflsdkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaos");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //name = blank
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJobF4() throws Exception {
                String view = this.mainController.addNewJob(
                        "",
                        "a job", "jobs");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //category = blank
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJobF5() throws Exception {
                String view = this.mainController.addNewJob(
                        "job",
                        "",
                        "jobs");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //description = blank
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJobF6() throws Exception {
                String view = this.mainController.addNewJob(
                        "job", "a job",
                        "");
                assertEquals(view,"redirect:/generic-error.html");
        }

        /*search*/
        //test to pass: category and keywords
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testSearch1() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "keywords", "category");
                assertEquals(view,"searchResults");
        }
        //keyword blank
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testSearch2() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "", "category");
                assertEquals(view,"searchResults");
        }
        //category blank
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testSearch3() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "keywords", "");
                assertEquals(view,"searchResults");
        }
        //both blank
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testSearch4() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "", "");
                assertEquals(view,"searchResults");
        }

        //viewjob: test to pass
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testViewJob1() throws Exception {
                String vieww = this.mainController.addNewJob(
                        "job", "a job", "jobs");
                assertEquals(vieww, "redirect:/homepageloggedin.html");
                int jid = getAJob();
                Job job = j.findJobByID(jid + "");
                String view = this.mainController.viewJob(model, jid + "");
                assertEquals(view, "viewJob");
                assertEquals(model.get("jobID"),jid);
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
        }
        //test to fail: job id not in the db
        @Test
        @WithMockUser(username = "johnsecretzz@columbia.edu", roles = { "USER" })
        public void testViewJob2() throws Exception {
                String vieww = this.mainController.addNewJob(
                        "job", "a job", "jobs");
                assertEquals(vieww, "redirect:/homepageloggedin.html");
                int jid = getAJob() - 1;
                Job job = j.findJobByID(jid + "");
                String view = this.mainController.viewJob(model, jid + "");
                assertEquals(view, "viewJobError");
                assertEquals(model.get("jobID"),jid + "");
        }

        //requesting and deciding: pass and fail are mixed togeter
        //adding requests: test to pass
        //PASS: request job that has not been accepted and not by me
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testAddReqest1() throws Exception {
                //getARequest() + 1 =
        }
        //PASS: request a job that someone else has requested, but not chosen

        //FAIL: request job I posted
        //FAIL: request job I requested
        //FAIL: request a job that doesn't exist
        //PASS: reject a request
        //PASS: accept a job where someone has been rejected from
        //FAI:: request a job someone has been accepted by
        //FAIL: accept/reject a request that doesn't exist
        //FAIL: accept a job that you didn't create
        //FAIL: accept a job that's already been decided


        @Configuration
        static class MainControllerTestConfiguration {
                //@Bean
                public MainController mainController() {
                    return new MainController();
                }
        }



}
