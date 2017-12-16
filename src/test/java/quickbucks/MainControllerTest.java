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
import java.util.List;

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
                return tst.getAJob();
        }
        public int getARequest() {
                return tst.getARequest();
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
        }



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
        }


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
        }

        //requesting and deciding: pass and fail are mixed togeter
        //we will add reviewing here too, and readDecision
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
        public void aatestAddReqest90() throws Exception {
                int toreq = getAJob() + 1;
                int empl = u.findIDByEmail("johnsecret82@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.makeDecision(model,
                        req.getId(), 1);
                assertEquals(view, "notifs");
        }
        //FAIL: read a decision when you decided it
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest90a() throws Exception {
                int toreq = getAJob() + 1;
                int empl = u.findIDByEmail("johnsecret82@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.readDecision(model,
                        req.getId());
                assertEquals(view, "redirect:/403.html");
        }
        //FAIL: read a decision with invalid request id
        @Test
        @WithMockUser(username = "johnsecret82@columbia.edu", roles = { "USER" })
        public void aatestAddReqest90aa() throws Exception {
                int empl = u.findIDByEmail("johnsecret82@columbia.edu");
                String view = this.mainController.readDecision(model,
                        -5);
                assertEquals(view, "redirect:/generic-error.html");
        }
        //FAIL: read a decision when you are uninvolved with it
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void aatestAddReqest90b() throws Exception {
                int toreq = getAJob() + 1;
                int empl = u.findIDByEmail("johnsecret82@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.readDecision(model,
                        req.getId());
                assertEquals(view, "redirect:/403.html");
        }
        //PASS: read a decision
        @Test
        @WithMockUser(username = "johnsecret82@columbia.edu", roles = { "USER" })
        public void aatestAddReqest90c() throws Exception {
                int toreq = getAJob() + 1;
                int empl = u.findIDByEmail("johnsecret82@columbia.edu");
                Request req = r.findRequestByJobAndEmployee(toreq + "", empl);
                String view = this.mainController.readDecision(model,
                        req.getId());
                assertEquals(view, "notifs");
        }
        //PASS: view reviews when you have some pending
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aa() throws Exception {
                int uid = u.findIDByEmail("johnsecret@columbia.edu");
                String view = this.mainController.employeeReviewList(model);
                assertEquals(view, "awaitingReview");
                //no reviews have occurred, so it should be all accepted request
                //below does not work; will not toString properly
                List<Request> reqs = r.findRequestsByEmployer(""+uid);
                assertEquals(((List<Request>)(model.get("results"))).size(),
                        reqs.size());
        }
        //PASS: view reviews when you have some pending as employer
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aa1() throws Exception {
                int uid = u.findIDByEmail("johnsecret@columbia.edu");
                String view = this.mainController.employerReviewList(model);
                assertEquals(view, "awaitingReview");
                List<Request> reqs = r.findRequestsByEmployee(""+uid);
                assertEquals(((List<Request>)(model.get("results"))).size(),
                        reqs.size());
        }
        //PASS: start the review process as an employer
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aaa() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
        }
        //PASS: call doReview() as employer
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aaaa() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.doReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                assertEquals(model.get("errmsg"), "");
        }
        //FAIL: call doReview() as unauthorized
        @Test
        @WithMockUser(username = "johnsecretzz@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aaab() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.doReview(model,
                        toreq + "");
                assertEquals(view, "redirect:/403.html");
                assertEquals(model.get("errmsg"), "");
        }
        //FAIL: call doReview() as invalid
        @Test
        @WithMockUser(username = "johnsecretzz@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aaac() throws Exception {
                int toreq = getAJob() - 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.doReview(model,
                        toreq + "");
                assertEquals(view, "reviewJobError");
                assertEquals(model.get("errmsg"), "");
        }
        //FAIL: start the review process with ID not in db
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aab() throws Exception {
                int toreq = getAJob() - 1;
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "reviewJobError");
                assertEquals(model.get("jobID"), toreq + "");
        }
        //FAIL: do review process with ID not in db
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aaba() throws Exception {
                int toreq = getAJob() - 1;
                String view = this.mainController.addNewReview(model,
                        toreq + "", "adsfj", "this is the reviewBody");
                assertEquals(view, "reviewJobError");
                assertEquals(model.get("jobID"), toreq + "");
        }
        //FAIL: start the review process with nonnumeric ID
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aac() throws Exception {
                String view = this.mainController.createReview(model,
                        "abcdefg");
                assertEquals(view, "reviewJobError");
                assertEquals(model.get("jobID"), "abcdefg");
        }
        //FAIL: do review process with nonnumeric ID
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aaca() throws Exception {
                String view = this.mainController.addNewReview(model,
                        "abcdefg", "adsfj", "this is the reviewBody");
                assertEquals(view, "reviewJobError");
                assertEquals(model.get("jobID"), "abcdefg");
        }
        //FAIL: start the review process with noninteger ID
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aad() throws Exception {
                String view = this.mainController.createReview(model,
                        "3.35");
                assertEquals(view, "reviewJobError");
                assertEquals(model.get("jobID"), "3.35");
        }
        //FAIL: do review process with nonint ID
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aada() throws Exception {
                String view = this.mainController.addNewReview(model,
                        "3.35", "adsfj", "this is the reviewBody");
                assertEquals(view, "reviewJobError");
                assertEquals(model.get("jobID"), "3.35");
        }
        //FAIL: start the review process with negative ID
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aae() throws Exception {
                String view = this.mainController.createReview(model,
                        "-5");
                assertEquals(view, "reviewJobError");
                assertEquals(model.get("jobID"), "-5");
        }
        //FAIL: do review process with negative ID
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aaea() throws Exception {
                String view = this.mainController.addNewReview(model,
                        "-5", "adsfj", "this is the reviewBody");
                assertEquals(view, "reviewJobError");
                assertEquals(model.get("jobID"), "-5");
        }
        //FAIL: start the review process with empty id
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aaf() throws Exception {
                String view = this.mainController.createReview(model,
                        "");
                assertEquals(view, "reviewJobError");
                assertEquals(model.get("jobID"), "");
        }
        //FAIL: review with empty id
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aafa() throws Exception {
                String view = this.mainController.addNewReview(model,
                        "", "adsfj", "this is the reviewBody");
                assertEquals(view, "reviewJobError");
                assertEquals(model.get("jobID"), "");
        }
        //FAIL: start the review process with brand new job no one's requested
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aag() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "job", "a job", "jobs");
                assertEquals(view,"viewJob");
                String jid = model.get("jobID") + "";
                view = this.mainController.createReview(model,
                        jid);
                assertEquals(view, "redirect:/403.html");
        }
        //FAIL: review with brand new job
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aaga() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "job", "a job", "jobs");
                assertEquals(view,"viewJob");
                String jid = model.get("jobID") + "";
                view = this.mainController.addNewReview(model,
                        jid, "adsfj", "this is the reviewBody");
                assertEquals(view, "redirect:/403.html");
        }
        //FAIL: start review process with job you are not employer/employee
        @Test
        @WithMockUser(username = "johnsecretzz@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aah() throws Exception {
                int toreq = getAJob() + 1;
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "redirect:/403.html");
        }
        //FAIL: review with job you are not employer/employee
        @Test
        @WithMockUser(username = "johnsecretzz@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aaha() throws Exception {
                int toreq = getAJob() + 1;
                String view = this.mainController.addNewReview(model,
                        toreq + "", "adsfj", "this is the reviewBody");
                assertEquals(view, "redirect:/403.html");
        }
        //pass: upcoming
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aahb() throws Exception {
                int uid = u.findIDByEmail("johnsecret@columbia.edu");
                String view = this.mainController.employeeAccepted(model);
                assertEquals(view, "employeeJobs");
                List<Request> reqs = r.findRequestsByEmployee(""+uid);
                assertEquals(((List<Request>)(model.get("jobs"))).size(),
                        reqs.size());
        }
        //pass: pending
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aahc() throws Exception {
                int uid = u.findIDByEmail("johnsecret@columbia.edu");
                String view = this.mainController.employeePending(model);
                assertEquals(view, "employeeJobs");
                List<Request> reqs = r.findRequestsByEmployeeUndecided(""+uid);
                assertEquals(((List<Request>)(model.get("jobs"))).size(),
                        reqs.size());
        }
        //FAIL: invalid rating
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91ab() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "adsfj", "this is the reviewBody");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                assertEquals(model.get("errmsg"), "rating must be numerical out of five");
        }
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91ac() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "-9.2", "this is the reviewBody");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                assertEquals(model.get("errmsg"), "rating must be numerical between 0 and five");
        }
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91acna() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "5.0001", "this is the reviewBody");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                assertEquals(model.get("errmsg"), "rating must be numerical between 0 and five");
        }
        //PASS: 0
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aca() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "0", "this is the reviewBody");
                assertEquals(view, "redirect:/reviewCreated.html");
        }
        //PASS: view reviews when you have none pending
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91acaa() throws Exception {
                int uid = u.findIDByEmail("johnsecret@columbia.edu");
                String view = this.mainController.employeeReviewList(model);
                assertEquals(view, "awaitingReview");
                //no reviews have occurred, so it should be all accepted request
                assertEquals(((List<Request>)(model.get("results"))).size(), 0);
        }
        //pass: 5
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91acb() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "5", "this is the reviewBody");
                assertEquals(view, "redirect:/reviewCreated.html");
        }
        //pass: 3.4
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91acc() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "3.4", "this is the reviewBody");
                assertEquals(view, "redirect:/reviewCreated.html");
        }
        //FAIL: invalid review body length
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91ad() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "0", "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                assertEquals(model.get("errmsg"), "review must be between 10 and 100 characters");
        }
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91ae() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "0", "thisisthe");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                assertEquals(model.get("errmsg"), "review must be between 10 and 100 characters");
        }
        //PASS: 10
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91af() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "0", "thisisther");
                assertEquals(view, "redirect:/reviewCreated.html");
        }
        //PASS: 11
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91ag() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "0", "thisisthe r");
                assertEquals(view, "redirect:/reviewCreated.html");
        }
        //PASS: 1499
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91ah() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "0", "thisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthsistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisisther");
                assertEquals(view, "redirect:/reviewCreated.html");
        }
        //PASS: 1500
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91ai() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "0", "thisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisisther");
                assertEquals(view, "redirect:/reviewCreated.html");
        }
        //FAIL: 1501
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91aj() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "0", "thisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthissistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisisther");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                assertEquals(model.get("errmsg"), "review must be between 10 and 100 characters");
        }
        //PASS: review an employer
        @Test
        @WithMockUser(username = "johnsecret82@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91ajj() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
        }
        //PASS: review an employer, make the actual review
        @Test
        @WithMockUser(username = "johnsecret82@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91ajja() throws Exception {
                int toreq = getAJob() + 1;
                Job job = j.findJobByID(toreq + "");
                String view = this.mainController.createReview(model,
                        toreq + "");
                assertEquals(view, "createReview");
                assertEquals(model.get("jobID"), job.getId());
                assertEquals(model.get("title"), job.getJobtitle());
                assertEquals(model.get("desc"), job.getJobdesc());
                assertEquals(model.get("tags"), job.getCategory());
                view = this.mainController.addNewReview(model,
                        toreq + "", "0", "thisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisistherthisisther");
                assertEquals(view, "redirect:/reviewCreated.html");
        }


        @Test
        @WithMockUser(username = "johnsecretzz@columbia.edu", roles = { "USER" })
        public void aatestAddReqest91ak() throws Exception {
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

        //forgot password email!
        //test to pass: user in db
        @Test
        public void testSendForgot1() throws Exception {
                String view = this.mainController.sendForgotEmail("john@columbia.edu");
                assertEquals(view, "redirect:/inputToken.html");
        }
        //fail: user not in db
        @Test
        public void testSendForgot2() throws Exception {
                String view = this.mainController.sendForgotEmail("joh@columbia.edu");
                assertEquals(view, "redirect:/notregistered.html");
        }
        //fail: user who already has a token
        @Test
        public void testSendForgot3() throws Exception {
                String view = this.mainController.sendForgotEmail("john@columbia.edu");
                assertEquals(view, "redirect:/generic-error.html");
        }
        //checkReset:
        //fail: incorrect token
        @Test
        public void testSreset1() throws Exception {
                String view = this.mainController.checkReset(model,
                        "john@columbia.edu", "");
                assertEquals(view, "redirect:/generic-error.html");
        }
        //pass: correct token
        @Test
        public void testSreset2() throws Exception {
                String rtoken = tst.getTokenByEmail("john@columbia.edu");
                String view = this.mainController.checkReset(model,
                        "john@columbia.edu", rtoken);
                assertEquals(view, "doReset");
                assertEquals(model.get("email"), "john@columbia.edu");
                assertEquals(model.get("token"), rtoken);
        }
        //fail: no token for this user
        @Test
        public void testSreset3() throws Exception {
                String view = this.mainController.checkReset(model,
                        "johns@columbia.edu", "");
                assertEquals(view, "redirect:/generic-error.html");
        }

        //doReset()
        //pass: good password, good token
        @Test
        public void testSreset4() throws Exception {
                String rtoken = tst.getTokenByEmail("john@columbia.edu");
                String view = this.mainController.checkReset(model,
                        "john@columbia.edu", rtoken);
                assertEquals(view, "doReset");
                assertEquals(model.get("email"), "john@columbia.edu");
                assertEquals(model.get("token"), rtoken);
                view =this.mainController.doReset("john@columbia.edu", rtoken, "password");
                assertEquals(view, "redirect:/savedreset.html");
        }
        //fail: correct token, gets deleted after
        @Test
        public void testSreset5() throws Exception {
                String view = this.mainController.sendForgotEmail("john@columbia.edu");
                String rtoken = tst.getTokenByEmail("john@columbia.edu");
                view = this.mainController.checkReset(model,
                        "john@columbia.edu", rtoken);
                assertEquals(view, "doReset");
                assertEquals(model.get("email"), "john@columbia.edu");
                assertEquals(model.get("token"), rtoken);
                view = this.mainController.checkReset(model,
                        "john@columbia.edu", rtoken);
                assertEquals(view, "doReset");
                view =this.mainController.doReset("john@columbia.edu", rtoken, "password");
                assertEquals(view, "redirect:/savedreset.html");
                view = this.mainController.checkReset(model,
                        "john@columbia.edu", rtoken);
                assertEquals(view, "redirect:/generic-error.html");
        }
        //fail: bad password, good token
        @Test
        public void testSreset6() throws Exception {
                String view = this.mainController.sendForgotEmail("john@columbia.edu");
                String rtoken = tst.getTokenByEmail("john@columbia.edu");
                view = this.mainController.checkReset(model,
                        "john@columbia.edu", rtoken);
                assertEquals(view, "doReset");
                assertEquals(model.get("email"), "john@columbia.edu");
                assertEquals(model.get("token"), rtoken);
                view =this.mainController.doReset("john@columbia.edu", rtoken, "pw");
                assertEquals(view, "redirect:/generic-error.html");
        }
        //fail: good password, bad token
        @Test
        public void testSreset7() throws Exception {
                String view =this.mainController.doReset("john@columbia.edu", "", "password");
                assertEquals(view, "redirect:/generic-error.html");
        }
        //fail: email and token don't match
        @Test
        public void testSreset8() throws Exception {
                String view = this.mainController.sendForgotEmail("johns@columbia.edu");
                String rtoken = tst.getTokenByEmail("john@columbia.edu");
                view = this.mainController.checkReset(model,
                        "johns@columbia.edu", rtoken);
                assertEquals(view, "redirect:/generic-error.html");
        }
        //fail: email and token don't match
        @Test
        public void testSreset9() throws Exception {
                String rtoken = tst.getTokenByEmail("john@columbia.edu");
                String view =this.mainController.doReset("johns@columbia.edu", rtoken, "password");
                assertEquals(view, "redirect:/generic-error.html");
        }

        //contact!
        //step one: test to pass
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void testContact1() throws Exception {
                String view =this.mainController.contactStepOne(model, "asdf");
                assertEquals(view, "composeMessage");
                assertEquals(model.get("id"), "asdf");
        }
        //contactPoster: test to pass
        //valid job
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void testContact2() throws Exception {
                int toreq = getAJob();
                String view =this.mainController.contactPoster(
                        toreq + "", "hi i am contacting you");
                assertEquals(view, "redirect:/sent.html");
        }
        //PASS: 10, 11, 1499, 1500
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void testContact8() throws Exception {
                int toreq = getAJob();
                String view =this.mainController.contactPoster(
                        toreq + "","asdfghhjkk");
                assertEquals(view, "redirect:/sent.html");
        }
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void testContact9() throws Exception {
                int toreq = getAJob();
                String view =this.mainController.contactPoster(
                        toreq + "","asdfghhjkkk");
                assertEquals(view, "redirect:/sent.html");
        }
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void testContact10() throws Exception {
                int toreq = getAJob();
                String view =this.mainController.contactPoster(
                        toreq + "",
                        "RE9s0Q9SsIU62Y75OnkztWjsOP0kyukpwIYPoFLSlpuvvrtgghQ8GO5NB6pXKv2pNAES7xLLkEjvmHWXcxlmwkIMaXoB2Ui17vLfm7z72u6beRmthEz609UFJ8mP9uhcyFo5NnKo0ml7bSqjSc8YQK34lryOOfSRKnULY3eoTOlylv9twV4PnJRRazD1EBgDDyJJD75pYtmWPjfJPyKJRQ9nBbIRFBuyqpsg2L9eCX0pNjVNNI4S4PRPkYYU9lWrJGDbgUKV23f3exeFhubU2eiVlIGI7gwPn14m96HwkIcnwnq1TmZwWlYOsCq8nkN3I3iEP2RxYc0xqHiAqXjUtzgEsbFT4pUrPFL9nSTQiM64Jk78voJpjbuFSapIhGbiGsSU78Fz12nP1G20mYvXFr7Yy4uz7VEEvnlL27VKq0IWs8siA1oiePHUP19qRyIKzkGTEbHD9MOypaloZNGzP3zYOaxJNykfW1JHt9c1rv5pcNLqL7Ah9bHUwa3MrPDBSH6F33zKt4AOQRKEqXxiCWn7wk0Ib9QenVVWZ8g3uQLx3pSzyQoTo402QIUk2Pt2LxMq6iqaW9pIIn0zANJWqKxe1Nh8gOkC5gr1fzl8GI2DibQhZ8jwAanCAIgDG7UegF1IEThSVnP9rr0iHEcApuPoprVcqcRAD5nnkFNlAY9MWVtglA9HHSZbnK5qwYZaYt1x9b6oVx64SENJ85gZ8T819cqQOqcsSiYuiqQ0sB4sbJOTniLB7CxqLe6BNGa19BpwEEqn8VPBXkw6a2ROxa9mUoWByBaxDcYbw9MYaYIg3A1pG3Pnf21cSQor6RgFGlmw1zLAGhlYN8YIiEtG7cMbcm6XLDDTyMpfIXmcU89NQg0PNVvHkF7fYRo5RKIei3V8iWGpJhSk5F0geZ7oBykvVjhroV9jFzXSkV7bhCNUO6qUMICAB3uyuWY4jwXxl4ZpLh8hO2PI8ZrQpf7Yq3EmEHEiHJ1aezj2FQjkCnx1a6pFWEoRiGGxeY9slUIP1uEXK2XWY9kicAfX6iFq2ilke9xPDBnuVCWrQ2Yv1J3mPPtLrehMLPB1t3oslhFG6LZXkON5gvEalqztQ9S9TOZpiBcX7O4O2c0cgH3ffueADtxJeUkM42jI34aY3bxLcnJIm1cxbeSu5XxZbLagZmVggzVeAopG6DwQsl1r7GhSzGyhfbe8ER05XUaKgrqvgbDuZgzw3Dr1OUUUgY2UJyKqCOxc4Se5hkF3BkwLBVOq5pS4ZEwiRyQNwWpBUBmX1gIWlfD6t3fDR9qKV6DtP7fJiiULQjLUQFrVQwEDo3ZovXaAtDPjqyqxU7Qhl5a74GW4ruoCgUalmgQkjDeC5cvCB65ZFyTUl6SbcfRktBsyD0LwDM5gGWpVtpjnghYm3j31GqawTjXvQ4MkJLWDlNj08ytp9x1VB3t5Qt80asqSmZ2RuPe8HxCFUckepP42stqQo7t5aUsiixJ4QhRLvUbBKPgd"
                        );
                assertEquals(view, "redirect:/sent.html");
        }
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void testContact11() throws Exception {
                int toreq = getAJob();
                String view =this.mainController.contactPoster(
                        toreq + "",
                        "R9s0Q9SsIU62Y75OnkztWjsOP0kyukpwIYPoFLSlpuvvrtgghQ8GO5NB6pXKv2pNAES7xLLkEjvmHWXcxlmwkIMaXoB2Ui17vLfm7z72u6beRmthEz609UFJ8mP9uhcyFo5NnKo0ml7bSqjSc8YQK34lryOOfSRKnULY3eoTOlylv9twV4PnJRRazD1EBgDDyJJD75pYtmWPjfJPyKJRQ9nBbIRFBuyqpsg2L9eCX0pNjVNNI4S4PRPkYYU9lWrJGDbgUKV23f3exeFhubU2eiVlIGI7gwPn14m96HwkIcnwnq1TmZwWlYOsCq8nkN3I3iEP2RxYc0xqHiAqXjUtzgEsbFT4pUrPFL9nSTQiM64Jk78voJpjbuFSapIhGbiGsSU78Fz12nP1G20mYvXFr7Yy4uz7VEEvnlL27VKq0IWs8siA1oiePHUP19qRyIKzkGTEbHD9MOypaloZNGzP3zYOaxJNykfW1JHt9c1rv5pcNLqL7Ah9bHUwa3MrPDBSH6F33zKt4AOQRKEqXxiCWn7wk0Ib9QenVVWZ8g3uQLx3pSzyQoTo402QIUk2Pt2LxMq6iqaW9pIIn0zANJWqKxe1Nh8gOkC5gr1fzl8GI2DibQhZ8jwAanCAIgDG7UegF1IEThSVnP9rr0iHEcApuPoprVcqcRAD5nnkFNlAY9MWVtglA9HHSZbnK5qwYZaYt1x9b6oVx64SENJ85gZ8T819cqQOqcsSiYuiqQ0sB4sbJOTniLB7CxqLe6BNGa19BpwEEqn8VPBXkw6a2ROxa9mUoWByBaxDcYbw9MYaYIg3A1pG3Pnf21cSQor6RgFGlmw1zLAGhlYN8YIiEtG7cMbcm6XLDDTyMpfIXmcU89NQg0PNVvHkF7fYRo5RKIei3V8iWGpJhSk5F0geZ7oBykvVjhroV9jFzXSkV7bhCNUO6qUMICAB3uyuWY4jwXxl4ZpLh8hO2PI8ZrQpf7Yq3EmEHEiHJ1aezj2FQjkCnx1a6pFWEoRiGGxeY9slUIP1uEXK2XWY9kicAfX6iFq2ilke9xPDBnuVCWrQ2Yv1J3mPPtLrehMLPB1t3oslhFG6LZXkON5gvEalqztQ9S9TOZpiBcX7O4O2c0cgH3ffueADtxJeUkM42jI34aY3bxLcnJIm1cxbeSu5XxZbLagZmVggzVeAopG6DwQsl1r7GhSzGyhfbe8ER05XUaKgrqvgbDuZgzw3Dr1OUUUgY2UJyKqCOxc4Se5hkF3BkwLBVOq5pS4ZEwiRyQNwWpBUBmX1gIWlfD6t3fDR9qKV6DtP7fJiiULQjLUQFrVQwEDo3ZovXaAtDPjqyqxU7Qhl5a74GW4ruoCgUalmgQkjDeC5cvCB65ZFyTUl6SbcfRktBsyD0LwDM5gGWpVtpjnghYm3j31GqawTjXvQ4MkJLWDlNj08ytp9x1VB3t5Qt80asqSmZ2RuPe8HxCFUckepP42stqQo7t5aUsiixJ4QhRLvUbBKPgd"
                        );
                assertEquals(view, "redirect:/sent.html");
        }
        //FAIL: empty string, 9, 1501
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void testContact5() throws Exception {
                int toreq = getAJob();
                String view =this.mainController.contactPoster(
                        toreq + "", "");
                assertEquals(view, "redirect:/generic-error.html");
        }
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void testContact6() throws Exception {
                int toreq = getAJob();
                String view =this.mainController.contactPoster(
                        toreq + "","asdfghhjk");
                assertEquals(view, "redirect:/generic-error.html");
        }
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void testContact7() throws Exception {
                int toreq = getAJob();
                String view =this.mainController.contactPoster(
                        toreq + "",
                        "REO9s0Q9SsIU62Y75OnkztWjsOP0kyukpwIYPoFLSlpuvvrtgghQ8GO5NB6pXKv2pNAES7xLLkEjvmHWXcxlmwkIMaXoB2Ui17vLfm7z72u6beRmthEz609UFJ8mP9uhcyFo5NnKo0ml7bSqjSc8YQK34lryOOfSRKnULY3eoTOlylv9twV4PnJRRazD1EBgDDyJJD75pYtmWPjfJPyKJRQ9nBbIRFBuyqpsg2L9eCX0pNjVNNI4S4PRPkYYU9lWrJGDbgUKV23f3exeFhubU2eiVlIGI7gwPn14m96HwkIcnwnq1TmZwWlYOsCq8nkN3I3iEP2RxYc0xqHiAqXjUtzgEsbFT4pUrPFL9nSTQiM64Jk78voJpjbuFSapIhGbiGsSU78Fz12nP1G20mYvXFr7Yy4uz7VEEvnlL27VKq0IWs8siA1oiePHUP19qRyIKzkGTEbHD9MOypaloZNGzP3zYOaxJNykfW1JHt9c1rv5pcNLqL7Ah9bHUwa3MrPDBSH6F33zKt4AOQRKEqXxiCWn7wk0Ib9QenVVWZ8g3uQLx3pSzyQoTo402QIUk2Pt2LxMq6iqaW9pIIn0zANJWqKxe1Nh8gOkC5gr1fzl8GI2DibQhZ8jwAanCAIgDG7UegF1IEThSVnP9rr0iHEcApuPoprVcqcRAD5nnkFNlAY9MWVtglA9HHSZbnK5qwYZaYt1x9b6oVx64SENJ85gZ8T819cqQOqcsSiYuiqQ0sB4sbJOTniLB7CxqLe6BNGa19BpwEEqn8VPBXkw6a2ROxa9mUoWByBaxDcYbw9MYaYIg3A1pG3Pnf21cSQor6RgFGlmw1zLAGhlYN8YIiEtG7cMbcm6XLDDTyMpfIXmcU89NQg0PNVvHkF7fYRo5RKIei3V8iWGpJhSk5F0geZ7oBykvVjhroV9jFzXSkV7bhCNUO6qUMICAB3uyuWY4jwXxl4ZpLh8hO2PI8ZrQpf7Yq3EmEHEiHJ1aezj2FQjkCnx1a6pFWEoRiGGxeY9slUIP1uEXK2XWY9kicAfX6iFq2ilke9xPDBnuVCWrQ2Yv1J3mPPtLrehMLPB1t3oslhFG6LZXkON5gvEalqztQ9S9TOZpiBcX7O4O2c0cgH3ffueADtxJeUkM42jI34aY3bxLcnJIm1cxbeSu5XxZbLagZmVggzVeAopG6DwQsl1r7GhSzGyhfbe8ER05XUaKgrqvgbDuZgzw3Dr1OUUUgY2UJyKqCOxc4Se5hkF3BkwLBVOq5pS4ZEwiRyQNwWpBUBmX1gIWlfD6t3fDR9qKV6DtP7fJiiULQjLUQFrVQwEDo3ZovXaAtDPjqyqxU7Qhl5a74GW4ruoCgUalmgQkjDeC5cvCB65ZFyTUl6SbcfRktBsyD0LwDM5gGWpVtpjnghYm3j31GqawTjXvQ4MkJLWDlNj08ytp9x1VB3t5Qt80asqSmZ2RuPe8HxCFUckepP42stqQo7t5aUsiixJ4QhRLvUbBKPgd");
                assertEquals(view, "redirect:/generic-error.html");
        }
        //to fail: invalid job
        @Test
        @WithMockUser(username = "johns@columbia.edu", roles = { "USER" })
        public void testContact3() throws Exception {
                String view =this.mainController.contactPoster(
                        "blah", "hi i am contacting you");
                assertEquals(view, "redirect:/generic-error.html");
        }
        //FAIL: self contact
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testContact4() throws Exception {
                int toreq = getAJob() + 1;
                String view = this.mainController.contactPoster(
                        toreq + "", "hi i am contacting you");
                assertEquals(view, "redirect:/generic-error.html");
        }

        //displayNotifs(): test to pass only
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testNotif() throws Exception {
                int uid = u.findIDByEmail("johnsecret@columbia.edu");
                List<Request> notifs = r.getNotifsByUserID(uid);
                String view = this.mainController.displayNotifications(model);
                assertEquals(view, "notifs");
                assertEquals(((List<Request>)(model.get("notifs"))).size(),
                        notifs.size());
                assertEquals(model.get("userr"), uid);
        }

        //viewUser(): user in DB (pass), user not in DB (fail)
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testView1() throws Exception {
                int uid = u.findIDByEmail("johnsecret@columbia.edu");
                User user = u.findByID(uid + "");
                String view = this.mainController.viewUser(model, uid + "");
                assertEquals(view, "viewUser");
                assertEquals(model.get("userID"), uid);
                assertEquals(model.get("name"), user.getUserFirstName()+" " +user.getUserLastName());
                assertEquals(model.get("school"), user.getUserSchool());
                assertEquals(model.get("degree"), user.getUserDegree());
                assertEquals(model.get("location"), user.getUserLocation());
        }
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testView2() throws Exception {
                String view = this.mainController.viewUser(model, "-1");
                assertEquals(view, "viewJobError");
        }

        //self: test to pass
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testViewSelf1() throws Exception {
                int uid = u.findIDByEmail("johnsecret@columbia.edu");
                User user = u.findByID(uid + "");
                String view = this.mainController.viewUser(model);
                assertEquals(view, "viewUser");
                assertEquals(model.get("userID"), uid);
                assertEquals(model.get("name"), user.getUserFirstName()+" " +user.getUserLastName());
                assertEquals(model.get("school"), user.getUserSchool());
                assertEquals(model.get("degree"), user.getUserDegree());
                assertEquals(model.get("location"), user.getUserLocation());
        }
        //test to fail: invalid uid
        @Test
        @WithMockUser(username = "jcret@columbia.edu", roles = { "USER" })
        public void testViewSelf2() throws Exception {
                String view = this.mainController.viewUser(model);
                assertEquals(view, "viewJobError");
                assertEquals(model.get("jobID"), "-1");
        }

        //OpenJobs: test to pass
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testOpenJobs1() throws Exception {
                int uid = u.findIDByEmail("johnsecret@columbia.edu");
                User user = u.findByID(uid + "");
                String view = this.mainController.employerJobs(model);
                assertEquals(view, "openjobs");
                /*assertEquals(model.get("userID"), uid);
                assertEquals(model.get("name"), user.getUserFirstName()+" " +user.getUserLastName());
                assertEquals(model.get("school"), user.getUserSchool());
                assertEquals(model.get("degree"), user.getUserDegree());
                assertEquals(model.get("location"), user.getUserLocation());*/
        }

        //cancelJob: test to pass, valid job
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCancel1() throws Exception {
                String view = this.mainController.addNewJob(model,
                        "job", "a job", "jobs");
                assertEquals(view,"viewJob");
                String jid = model.get("jobID") + "";
                view = this.mainController.cancelJob(model, jid + "");
                assertEquals(view, "cancel");
        }
        //fail: invalid job id
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCancel2() throws Exception {
                String view = this.mainController.cancelJob(model, "-1");
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
