package meeter.app.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(EventRepository repository) {

    return args -> {
      log.info("Preloading " + repository.save(new Event("Катаемся на лыжах", "спорт")));
      log.info("Preloading " + repository.save(new Event("Играем в футбол", "спорт")));
    };
  }
}