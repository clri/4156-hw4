package quickbucks;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.After;
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

        @Before
        public void setup() {
                mainController = new MainController();
                mainController.setReposotories(u,j,r);
        }

        @After
        public void verify() {
                //Mockito.reset();
        }

        @Test
        public void testRegister() throws Exception {

                /*MockHttpSession mockHttpSession = new MockHttpSession();
                mockHttpSession.setAttribute(MainController.REQUESTED_URL, "someUrl");*/

                /*first, last, email, pass, degree, on/off, school*/
                String view = this.mainController.addNewUser(
                        "john", "secret", "john@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html"); //why will this fail

                view = this.mainController.addNewUser(
                        "john", "secret", "john@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html"); //should fail, dup

                view = this.mainController.addNewUser(
                        "john", "secret", "john1@email.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html"); //should fail, email

                view = this.mainController.addNewUser(
                        "john123", "secret", "john1@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html"); //should fail, name

                view = this.mainController.addNewUser(
                        "john", "secret", "john1@columbia.edu",
                        "abc", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html"); //password too short

                view = this.mainController.addNewUser(
                        "john", "sec et", "john1@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html"); //should pass, space

                view = this.mainController.addNewUser(
                        "jo-hn", "sec et", "john2@columbia.edu",
                        "abcde", "MS", "OFF", "SEAS");
                assertEquals(view,"redirect:/index2.html"); //should pass, space

                view = this.mainController.addNewUser(
                        "john", "sec et", "john3@columbia.edu",
                        "abcde", "MS", "fff", "SEAS");
                assertEquals(view,"redirect:/index3.html"); //should fail, onoff

                view = this.mainController.addNewUser(
                        "john",
                        "sec etlksdjflsdkfjsldksdlkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosldijfalsdkfjalskdfjalskdjfalks",
                        "john3@columbia.edu",
                        "abced", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html"); //should fail, length

                view = this.mainController.addNewUser(
                        "john",
                        "sece tlksdjflsdkfjsldkssdlkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosl",
                        "john3@columbia.edu",
                        "abcde", "MS", "OFF", "SEAS");
                assertEquals(view,"redirect:/index2.html"); //should pass, length

                view = this.mainController.addNewUser(
                        "john",
                        "sece tlksdjflsdkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosl",
                        "john4@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html"); //should fail, length*/

        }

        @Test
        @WithMockUser(username = "mjs@barnard.edu", roles = { "USER" })
        public void testCreateJob() throws Exception {

                String view = this.mainController.addNewJob(
                        "job", "a job", "jobs");
                assertEquals(view,"redirect:/homepageloggedin.html"); //success

                view = this.mainController.addNewJob(
                        "job", "a job",
                        "sec etlksdjflsdkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosl"
                        );
                assertEquals(view,"redirect:/index3.html"); //job too long

                view = this.mainController.addNewJob(
                        "job", "a job",
                        "sec etlksdjflsdkfjsldksdfflkfjsdlfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosl"
                        );
                assertEquals(view,"redirect:/homepageloggedin.html"); //just right

                view = this.mainController.addNewJob(
                        "job", "a job",
                        ""
                        );
                assertEquals(view,"redirect:/index3.html"); //just right
        }

        @Test
        public void testIndex() throws Exception {

                String view = this.mainController.lookupJobByID();
                assertEquals(view,"jobByID"); //success
        }

        @Configuration
        static class MainControllerTestConfiguration {
                //@Bean
                public MainController mainController() {
                    return new MainController();
                }
        }



}
