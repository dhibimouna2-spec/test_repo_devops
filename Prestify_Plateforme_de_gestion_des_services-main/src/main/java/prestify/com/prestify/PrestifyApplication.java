package prestify.com.prestify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;




@SpringBootApplication(scanBasePackages = "prestify.com.prestify")
public class PrestifyApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrestifyApplication.class, args);
    }

}

