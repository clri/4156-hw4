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
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import org.springframework.mail.javamail.JavaMailSender;

import quickbucks.User;
import quickbucks.TestRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration //(classes = {WebSecurityConfig.class, MvcConfig.class})
@SpringBootTest(classes = Application.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
        public void aaaacleanDB() throws Exception {
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

/*
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
*/
        //REGISTRATION
        //test to pass
        //first name capitalized
        @Test
        public void aaaatestRegister1() throws Exception {
                String view = this.mainController.addNewUser(
                        "John", "Secret", "john@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //first name all lowercase
        @Test
        public void aaaatestRegisterLowerF() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "Secret", "johns@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //boundary: first name 255 characters
        @Test
        public void aaaatestRegisterMaxLenF() throws Exception {
                String view = this.mainController.addNewUser(
                        "sec etlksdjflsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosl",
                        "Secret", "johns1@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }

        //last name capitalized
        @Test
        public void aaaatestRegister1L() throws Exception {
                String view = this.mainController.addNewUser(
                        "John", "Secret", "johnsecret@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //last name all lowercase
        @Test
        public void aaaatestRegisterLowerL() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "johnsec@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //boundary: last name 255 characters
        @Test
        public void aaaatestRegisterMaxLenL() throws Exception {
                String view = this.mainController.addNewUser(
                        "Secret",
                        "sec etlksdjflsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosl",
                        "johns0@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }

        //@columbia.edu email
        @Test
        public void aaaatestRegisterEmail1() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "johnsec4444@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //@barnard.edu email
        @Test
        public void aaaatestRegisterEmail2() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "johnsec4444@barnard.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //@cs.columbia.edu email
        @Test
        public void aaaatestRegisterEmail3() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "johnsec4444@cs.columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //255-char email
        @Test
        public void aaaatestRegisterEmailLong1() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "senetlkfsdjflsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdi@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //email with dash
        @Test
        public void aaaatestRegisterEmail4() throws Exception {
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
        public void aaaatestRegisterPasswordShort1() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "john15@columbia.edu",
                        "1234", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }
        //255 password
        @Test
        public void aaaatestRegisterPasswordLong1() throws Exception {
                String view = this.mainController.addNewUser(
                        "john", "secret", "john15@cs.columbia.edu",
                        "secnetlkfsdjfsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdi@columbia.edu",
                        "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");
        }/*



        //test to FAIL
        //blank first name
        @Test
        public void aaaatestRegisterEmptyF() throws Exception {
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
        public void aaatestRegisterEmail1Duplicate() throws Exception {
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
        }*/


        //JOB CREATION
        //TEST TO PASS

        //average job
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aaatestCreateJob1() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "job", "a job", "jobs");
                assertEquals(view,"viewJob");
        }
        //name = 255
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aaatestCreateJob2() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "sec etlksdjflsdkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaos",
                        "a job", "jobs");
                assertEquals(view,"viewJob");
        }
        //descrition = 1500
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aaatestCreateJob3() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "job",
                        "REO9s0Q9SsIU62Y75OnkztWjsOP0kyukpwIYPoFLSlpuvvrtgghQ8GO5NB6pXKv2pNAES7xLLkEjvmHWXcxlmwkIMaXoB2Ui17vLfm7z72u6beRmthEz609UFJ8mP9uhcyFo5NnKo0ml7bSqjSc8YQK34lryOOfSRKnULY3eoTOlylv9twV4PnJRRazD1EBgDDyJJD75pYtmWPjfJPyKJRQ9nBbIRFBuyqpsg2L9eCX0pNjVNNI4S4PRPkYYU9lWrJGDbgUKV23f3exeFhubU2eiVlIGI7gwPn14m96HwkIcnwnq1TmZwWlYOsCq8nkN3I3iEP2RxYc0xqHiAqXjUtzgEsbFT4pUrPFL9nSTQiM64Jk78voJpjbuFSapIhGbiGsSU78Fz12nP1G20mYvXFr7Yy4uz7VEEvnlL27VKq0IWs8siA1oiePHUP19qRyIKzkGTEbHD9MOypaloZNGzP3zYOaxJNykfW1JHt9c1rv5pcNLqL7Ah9bHUwa3MrPDBSH6F33zKt4AOQRKEqXxiCWn7wk0Ib9QenVVWZ8g3uQLx3pSzyQoTo402QIUk2Pt2LxMq6iqaW9pIIn0zANJWqKxe1Nh8gOkC5gr1fzl8GI2DibQhZ8jwAanCAIgDG7UegF1IEThSVnP9rr0iHEcApuPoprVcqcRAD5nnkFNlAY9MWVtglA9HHSZbnK5qwYZaYt1x9b6oVx64SENJ85gZ8T819cqQOqcsSiYuiqQ0sB4sbJOTniLB7CxqLe6BNGa19BpwEEqn8VPBXkw6a2ROxa9mUoWByBaxDcYbw9MYaYIg3A1pG3Pnf21cSQor6RgFGlmw1zLAGhlYN8YIiEtG7cMbcm6XLDDTyMpfIXmcU89NQg0PNVvHkF7fYRo5RKIei3V8iWGpJhSk5F0geZ7oBykvVjhroV9jFzXSkV7bhCNUO6qUMICAB3uyuWY4jwXxl4ZpLh8hO2PI8ZrQpf7Yq3EmEHEiHJ1aezj2FQjkCnx1a6pFWEoRiGGxeY9slUIP1uEXK2XWY9kicAfX6iFq2ilke9xPDBnuVCWrQ2Yv1J3mPPtLrehMLPB1t3oslhFG6LZXkON5gvEalqztQ9S9TOZpiBcX7O4O2c0cgH3ffueADtxJeUkM42jI34aY3bxLcnJIm1cxbeSu5XxZbLagZmVggzVeAopG6DwQsl1r7GhSzGyhfbe8ER05XUaKgrqvgbDuZgzw3Dr1OUUUgY2UJyKqCOxc4Se5hkF3BkwLBVOq5pS4ZEwiRyQNwWpBUBmX1gIWlfD6t3fDR9qKV6DtP7fJiiULQjLUQFrVQwEDo3ZovXaAtDPjqyqxU7Qhl5a74GW4ruoCgUalmgQkjDeC5cvCB65ZFyTUl6SbcfRktBsyD0LwDM5gGWpVtpjnghYm3j31GqawTjXvQ4MkJLWDlNj08ytp9x1VB3t5Qt80asqSmZ2RuPe8HxCFUckepP42stqQo7t5aUsiixJ4QhRLvUbBKPg",
                        "jobs");
                assertEquals(view,"viewJob");
        }
        //category = 255
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aaatestCreateJob4() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "job", "a job",
                        "sec etlksdjflsdkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaos");
                assertEquals(view,"viewJob");
        }

/*
        //TEST TO FAIL
        //name = 256
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJobF1() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "sec etlksdjflsddkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaos",
                        "a job", "jobs");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //description = 256
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJobF2() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "job",
                        "REO9s0Q9SsIU62Y75OnkztWjsOP0kyukpwIYPoFLSlpuvvrtgghQ8GO5NB6pXKv2pNAES7xLLkEjvmHWXcxlmwkIMaXoB2Ui17vLfm7z72u6beRmthEz609UFJ8mP9uhcyFo5NnKo0ml7bSqjSc8YQK34lryOOfSRKnULY3eoTOlylv9twV4PnJRRazD1EBgDDyJJD75pYtmWPjfJPyKJRQ9nBbIRFBuyqpsg2L9eCX0pNjVNNI4S4PRPkYYU9lWrJGDbgUKV23f3exeFhubU2eiVlIGI7gwPn14m96HwkIcnwnq1TmZwWlYOsCq8nkN3I3iEP2RxYc0xqHiAqXjUtzgEsbFT4pUrPFL9nSTQiM64Jk78voJpjbuFSapIhGbiGsSU78Fz12nP1G20mYvXFr7Yy4uz7VEEvnlL27VKq0IWs8siA1oiePHUP19qRyIKzkGTEbHD9MOypaloZNGzP3zYOaxJNykfW1JHt9c1rv5pcNLqL7Ah9bHUwa3MrPDBSH6F33zKt4AOQRKEqXxiCWn7wk0Ib9QenVVWZ8g3uQLx3pSzyQoTo402QIUk2Pt2LxMq6iqaW9pIIn0zANJWqKxe1Nh8gOkC5gr1fzl8GI2DibQhZ8jwAanCAIgDG7UegF1IEThSVnP9rr0iHEcApuPoprVcqcRAD5nnkFNlAY9MWVtglA9HHSZbnK5qwYZaYt1x9b6oVx64SENJ85gZ8T819cqQOqcsSiYuiqQ0sB4sbJOTniLB7CxqLe6BNGa19BpwEEqn8VPBXkw6a2ROxa9mUoWByBaxDcYbw9MYaYIg3A1pG3Pnf21cSQor6RgFGlmw1zLAGhlYN8YIiEtG7cMbcm6XLDDTyMpfIXmcU89NQg0PNVvHkF7fYRo5RKIei3V8iWGpJhSk5F0geZ7oBykvVjhroV9jFzXSkV7bhCNUO6qUMICAB3uyuWY4jwXxl4ZpLh8hO2PI8ZrQpf7Yq3EmEHEiHJ1aezj2FQjkCnx1a6pFWEoRiGGxeY9slUIP1uEXK2XWY9kicAfX6iFq2ilke9xPDBnuVCWrQ2Yv1J3mPPtLrehMLPB1t3oslhFG6LZXkON5gvEalqztQ9S9TOZpiBcX7O4O2c0cgH3ffueADtxJeUkM42jI34aY3bxLcnJIm1cxbeSu5XxZbLagZmVggzVeAopG6DwQsl1r7GhSzGyhfbe8ER05XUaKgrqvgbDuZgzw3Dr1OUUUgY2UJyKqCOxc4Se5hkF3BkwLBVOq5pS4ZEwiRyQNwWpBUBmX1gIWlfD6t3fDR9qKV6DtP7fJiiULQjLUQFrVQwEDo3ZovXaAtDPjqyqxU7Qhl5a74GW4ruoCgUalmgQkjDeC5cvCB65ZFyTUl6SbcfRktBsyD0LwDM5gGWpVtpjnghYm3j31GqawTjXvQ4MkJLWDlNj08ytp9x1VB3t5Qt80asqSmZ2RuPe8HxCFUckepP42stqQo7t5aUsiixJ4QhRLvUbBKPgk",
                        "jobs");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //category = 256
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJobF3() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "job", "a job",
                        "sec etlkfsdjflsdkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaos");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //name = blank
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJobF4() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "",
                        "a job", "jobs");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //category = blank
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJobF5() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "job",
                        "",
                        "jobs");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //description = blank
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJobF6() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "job", "a job",
                        "");
                assertEquals(view,"redirect:/generic-error.html");
        }

        //
        //test to pass: category and keywords, page
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testSearch1() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "keywords", "category", "1");
                assertEquals(view,"searchResults");
                assertEquals(model.get("test"),"hello");
                assertEquals(model.get("currPage"),"1");
                assertEquals(model.get("key"),"keywords");
                assertEquals(model.get("cat"),"category");
        }
        //keyword blank
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testSearch2() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "", "category", "1");
                assertEquals(view,"searchResults");
                assertEquals(model.get("test"),"hello");
                assertEquals(model.get("currPage"),"1");
                assertEquals(model.get("key"),"");
                assertEquals(model.get("cat"),"category");
        }
        //category blank
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testSearch3() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "keywords", "", "1");
                assertEquals(view,"searchResults");
                assertEquals(model.get("test"),"hello");
                assertEquals(model.get("currPage"),"1");
                assertEquals(model.get("key"),"keywords");
                assertEquals(model.get("cat"),"");
        }
        //both blank
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testSearch4() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "", "", "1");
                assertEquals(view,"searchResults");
                assertEquals(model.get("test"),"hello");
                assertEquals(model.get("currPage"),"1");
                assertEquals(model.get("key"),"");
                assertEquals(model.get("cat"),"");
        }
        //negative page
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testSearch8() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "", "", "-1");
                assertEquals(view,"searchResults");
                assertEquals(model.get("test"),"hello");
                assertEquals(model.get("currPage"),"1");
                assertEquals(model.get("key"),"");
                assertEquals(model.get("cat"),"");
        }
        //zero page
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testSearch9() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "", "", "0");
                assertEquals(view,"searchResults");
                assertEquals(model.get("test"),"hello");
                assertEquals(model.get("currPage"),"1");
                assertEquals(model.get("key"),"");
                assertEquals(model.get("cat"),"");
        }
        //larger than number of pages
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testSearch10() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "678", "939", "1234");
                assertEquals(view,"searchResults");
                assertEquals(model.get("test"),"hello");
                assertEquals(model.get("currPage"),"1");
                assertEquals(model.get("key"),"678");
                assertEquals(model.get("cat"),"939");
        }

        //test to fail!
        //empty string page number
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testSearch5() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "", "", "");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //noninteger page number
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testSearch6() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "", "", "4.4");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //nonnumeric page number
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testSearch7() throws Exception {
                String view = this.mainController.searchJobs(model,
                        "", "", "sdklfjsldkfj");
                assertEquals(view,"redirect:/generic-error.html");
        }


        //viewjob: test to pass
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aaatestViewJob1() throws Exception {
                String vieww = this.mainController.addNewJob(model,
                        "job", "a job", "jobs");
                assertEquals(vieww, "viewJob");
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
        public void aaatestViewJob2() throws Exception {
                String vieww = this.mainController.addNewJob(model,
                        "job", "a job", "jobs");
                assertEquals(vieww, "viewJob");
                int jid = getAJob() - 1;
                Job job = j.findJobByID(jid + "");
                String view = this.mainController.viewJob(model, jid + "");
                assertEquals(view, "viewJobError");
                assertEquals(model.get("jobID"),jid + "");
        }*/

        //requesting and deciding: pass and fail are mixed togeter
        //we will add reviewing here too
        //adding requests: test to pass
        //PASS: request job that has not been accepted and not by me
        @Test
        @WithMockUser(username = "johnsecretzz@columbia.edu", roles = { "USER" })
        public void aatestAddReqest1() throws Exception {
                int toreq = getAJob() + 1;
                String view = this.mainController.addNewJob(toreq + "");
                assertEquals(view, "redirect:/requestSuccess.html");
        }
        //PASS: request a job that someone else has requested, but not chosen
        @Test
        @WithMockUser(username = "johnsecret82@columbia.edu", roles = { "USER" })
        public void aatestAddReqest2() throws Exception {
                int toreq = getAJob() + 1;
                String view = this.mainController.addNewJob(toreq + "");
                assertEquals(view, "redirect:/requestSuccess.html");
        }
        //FAIL: request job I posted
        @Test
        @WithMockUser(username = "johnsecretzz@columbia.edu", roles = { "USER" })
        public void aatestAddReqest3() throws Exception {
                int toreq = getAJob() + 1;
                String view = this.mainController.addNewJob(toreq + "");
                assertEquals(view, "redirect:/searchJobsRF.html");
        }
        //FAIL: request job I requested already
        @Test
        @WithMockUser(username = "johnsecret82@columbia.edu", roles = { "USER" })
        public void aatestAddReqest4() throws Exception {
                int toreq = getAJob() + 1;
                String view = this.mainController.addNewJob(toreq + "");
                assertEquals(view, "redirect:/searchJobsRF.html");
        }
        //FAIL: request a job that doesn't exist
        @Test
        @WithMockUser(username = "johnsecret82@columbia.edu", roles = { "USER" })
        public void aatestAddReqest5() throws Exception {
                int toreq = getAJob() - 1;
                String view = this.mainController.addNewJob(toreq + "");
                assertEquals(view, "redirect:/searchJobsRF.html");
        }
        //PASS: reject a request
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest6() throws Exception {
                int toreq = getAJob() + 1;
                int empl = u.findIDByEmail("johnsecretzz@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.makeDecision(model,
                        req.getId(), 2);
                assertEquals(view, "notifs");
        }
        //FAIL: decision = -1; decision = 3; decision = 0
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest7() throws Exception {
                int toreq = getAJob() + 1;
                int empl = u.findIDByEmail("johnsecret82@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.makeDecision(model,
                        req.getId(), 0);
                assertEquals(view, "redirect:/generic-error.html");
        }
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest8() throws Exception {
                int toreq = getAJob() + 1;
                int empl = u.findIDByEmail("johnsecret82@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.makeDecision(model,
                        req.getId(), -1);
                assertEquals(view, "redirect:/generic-error.html");
        }
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest9() throws Exception {
                int toreq = getAJob() + 1;
                int empl = u.findIDByEmail("johnsecret82@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.makeDecision(model,
                        req.getId(), 3);
                assertEquals(view, "redirect:/generic-error.html");
        }
        //PASS: accept a job where someone has been rejected from
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91() throws Exception {
                int toreq = getAJob() + 1;
                int empl = u.findIDByEmail("johnsecret82@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.makeDecision(model,
                        req.getId(), 1);
                assertEquals(view, "notifs");
        }

        @Test
        @WithMockUser(username = "johnsecretzz@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91a() throws Exception {
                int toreq = getAJob();
                String view = this.mainController.addNewJob(toreq + "");
                assertEquals(view, "redirect:/requestSuccess.html");
        }
        //PASS: request a job that someone else has requested, but not chosen
        @Test
        @WithMockUser(username = "johnsecret82@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91b() throws Exception {
                int toreq = getAJob();
                String view = this.mainController.addNewJob(toreq + "");
                assertEquals(view, "redirect:/requestSuccess.html");
        }
        //FAIL: accept a job that you didn't create
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91c() throws Exception {
                int toreq = getAJob();
                int empl = u.findIDByEmail("johnsecretzz@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.makeDecision(model,
                        req.getId(), 2);
                assertEquals(view, "redirect:/403.html");
        }
        //PASS: accept a job where no one has been rejected from
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest92() throws Exception {
                int toreq = getAJob();
                int empl = u.findIDByEmail("johnsecret82@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.makeDecision(model,
                        req.getId(), 1);
                assertEquals(view, "notifs");
        }
        //FAIL: reject job implicitly rjecetd
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest93() throws Exception {
                int toreq = getAJob();
                int empl = u.findIDByEmail("johnsecretzz@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.makeDecision(model,
                        req.getId(), 2);
                assertEquals(view, "redirect:/generic-error.html");
        }
        //FAI:: request a job someone has been accepted by
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void aatestAddReqest93a() throws Exception {
                int toreq = getAJob();
                String view = this.mainController.addNewJob(toreq + "");
                assertEquals(view, "redirect:/searchJobsRF.html");
        }
        //FAIL: accept/reject a request that doesn't exist
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void aatestAddReqest93b() throws Exception {
                int req = getARequest() - 12;
                String view = this.mainController.makeDecision(model,
                        req, 2);
                assertEquals(view, "redirect:/generic-error.html");
        }
        //FAIL: accept a job that's already been decided
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest93c() throws Exception {
                int toreq = getAJob();
                int empl = u.findIDByEmail("johnsecretzz@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.makeDecision(model,
                        req.getId(), 1);
                assertEquals(view, "redirect:/generic-error.html");
        }


        @Configuration
        static class MainControllerTestConfiguration {
                //@Bean
                public MainController mainController() {
                    return new MainController();
                }
        }



}
