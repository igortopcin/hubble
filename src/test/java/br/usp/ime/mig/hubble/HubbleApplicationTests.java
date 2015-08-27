package br.usp.ime.mig.hubble;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.usp.ime.mig.hubble.HubbleApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HubbleApplication.class)
@WebAppConfiguration
public class HubbleApplicationTests {

	@Test
	public void contextLoads() {
	}

}
