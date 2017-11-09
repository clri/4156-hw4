/*package quickbucks;

import quickbucks.AppConfig;
import quickbucks.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MvcConfig.class, WebSecurityConfig.class,
        AuthenticationProviderConfig.class})
public class JobTest {

	//DI
    @Autowired
    @Qualifier("jt")
    Job jt;

    @Test
    public void test_job_() {

        //assert correct type/impl
        assertThat(jt, instanceOf(MachineLearningService.class));

        //assert true
        assertThat(ml.isValid(""), is(true));

    }
}*/
