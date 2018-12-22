package pl.lso.kazimierz.pastoralvisitmanager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lso.kazimierz.pastoralvisitmanager.model.dto.pastoralvisit.PastoralVisitStatus;

@RunWith(SpringRunner.class)
public class MyTest {

	@Test
	public void test() {
		System.out.println("testing...");

		System.out.println(PastoralVisitStatus.getByName("completed"));
		System.out.println(PastoralVisitStatus.getByName("empty"));
	}

}
