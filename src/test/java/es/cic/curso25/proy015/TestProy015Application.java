package es.cic.curso25.proy015;

import org.springframework.boot.SpringApplication;

public class TestProy015Application {

	public static void main(String[] args) {
		SpringApplication.from(Proy015Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
