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

import quickbucks.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MainControllerTest {
        @Autowired
        private MainController mainController;


        @Configuration
        static class MainControllerTestConfiguration {

        @Test
        public void testRegister() throws Exception {

                MockHttpSession mockHttpSession = new MockHttpSession();
                mockHttpSession.setAttribute(MainController.REQUESTED_URL, "someUrl");

                /*first, last, email, pass, degree, on/off, school*/
                String view = this.mainController.addNewUser(
                        "john", "secret", "john@columbia.edu",
                        "abcde", "MS", "ON", "SEAS");
                assertEquals(view,"redirect:/index2.html");

                /*Account account = (Account) mockHttpSession.getAttribute(LoginController.ACCOUNT_ATTRIBUTE);

       assertNotNull(account);
       assertEquals("John", account.getFirstName());
       assertEquals("Doe", account.getLastName());
       assertNull(mockHttpSession.getAttribute(LoginController.REQUESTED_URL));
       assertEquals("redirect:someUrl", view);

       // Test the different view selection choices
       mockHttpSession = new MockHttpSession();
       view = this.loginController.handleLogin("john", "secret", mockHttpSession);
       assertEquals("redirect:/index.htm", view);

       mockHttpSession = new MockHttpSession();
       mockHttpSession.setAttribute(LoginController.REQUESTED_URL, "abclogindef");
       view = this.loginController.handleLogin("john", "secret", mockHttpSession);
       assertEquals("redirect:/index.htm", view);*/
}


                /*@Bean
                /*public AccountService accountService() {
                    return Mockito.mock(AccountService.class);
            }*/

                @Bean
                public MainController mainController() {
                    return new MainController();
                }
        }



}
