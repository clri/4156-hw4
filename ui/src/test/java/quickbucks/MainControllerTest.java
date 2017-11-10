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

//import quickbucks.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration 
public class MainControllerTest {
        @Autowired
        private MainController mainController;

        @Test
        public void testRegister() throws Exception {

                /*MockHttpSession mockHttpSession = new MockHttpSession();
                mockHttpSession.setAttribute(MainController.REQUESTED_URL, "someUrl");*/

                /*first, last, email, pass, degree, on/off, school*/
                String view = this.mainController.addNewUser(
                        "john", "secret", "john@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html"); //success

                view = this.mainController.addNewUser(
                        "john", "secret", "john@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index3.html"); //should fail, dup

                view = this.mainController.addNewUser(
                        "john", "secret", "john1@columbia.edu",
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
                        "sec etlksdjflsdkfjsldkssdlkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosl",
                        "john3@columbia.edu",
                        "abcde", "MS", "fff", "SEAS");
                assertEquals(view,"redirect:/index2.html"); //should pass, length

                view = this.mainController.addNewUser(
                        "john",
                        "sec etlksdjflsdkfjsldksdfflkfjsdklfjdklsfjslkdsjflskdfjlsdkfjsdljdsfkjfdjfjfjlskdfjlesirjslidjfsldkfjslkdfjsldkfjsldkfjsleirjlkxdjfalskdfjalsdkfjalskdjflaksdjflkasjdlkfjalsdkfjlaksjdflkajsdfkfjsldkfjsldkfjsdlkfjsldkjdjdjdjdksajdfhlasdifjkalsdifjalsidjfaosl",
                        "john4@columbia.edu",
                        "abcde", "MS", "fff", "SEAS");
                assertEquals(view,"redirect:/index3.html"); //should fail, length

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

                String view = this.mainController.index();
                assertEquals(view,"jobByID"); //success
        }

        @Configuration
        static class MainControllerTestConfiguration {
                @Bean
                public MainController mainController() {
                    return new MainController();
                }
        }



}
