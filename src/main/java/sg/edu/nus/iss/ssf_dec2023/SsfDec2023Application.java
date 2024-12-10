package sg.edu.nus.iss.ssf_dec2023;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sg.edu.nus.iss.ssf_dec2023.model.Event;

@SpringBootApplication
public class SsfDec2023Application implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(SsfDec2023Application.class, args);
	}

	@Override
	public void run(String... args)
	{
		String fileName = "events.json";
		List<Event> events = databaseService.
	}

}
