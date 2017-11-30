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


        @Before
        public void setup() {
                mainController = new MainController();
                mainController.setReposotories(u, j, r, re, rt);
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
        //category = 255
        @Test
        @WithMockUser(username = "johnsecret@columbia.edu", roles = { "USER" })
        public void testCreateJob3() throws Exception {
                String view = this.mainController.addNewJob(
                        "job",
                        "sec etlksdjflsdkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaos",
                        "jobs");
                assertEquals(view,"redirect:/homepageloggedin.html");
        }
        //description = 255
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
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testCreateJobF1() throws Exception {
                String view = this.mainController.addNewJob(
                        "sec etlksdjflsddkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaos",
                        "a job", "jobs");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //category = 256
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testCreateJobF2() throws Exception {
                String view = this.mainController.addNewJob(
                        "job",
                        "sec ertlksdjflsdkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaos",
                        "jobs");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //description = 256 @TODO: 1500
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testCreateJobF3() throws Exception {
                String view = this.mainController.addNewJob(
                        "job", "a job",
                        "sec etlkfsdjflsdkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaos");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //name = blank
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testCreateJobF4() throws Exception {
                String view = this.mainController.addNewJob(
                        "",
                        "a job", "jobs");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //category = blank
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testCreateJobF5() throws Exception {
                String view = this.mainController.addNewJob(
                        "job",
                        "",
                        "jobs");
                assertEquals(view,"redirect:/generic-error.html");
        }
        //description = blank
        @Test
        @WithMockUser(username = "john@columbia.edu", roles = { "USER" })
        public void testCreateJobF6() throws Exception {
                String view = this.mainController.addNewJob(
                        "job", "a job",
                        "");
                assertEquals(view,"redirect:/generic-error.html");
        }


        @Configuration
        static class MainControllerTestConfiguration {
                //@Bean
                public MainController mainController() {
                    return new MainController();
                }
        }



}
