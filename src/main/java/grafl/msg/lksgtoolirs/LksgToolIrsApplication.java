package grafl.msg.lksgtoolirs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Slf4j
public class LksgToolIrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LksgToolIrsApplication.class, args);
    }

    @GetMapping(value = "/")
    public String root_context() {
        log.info("root_context()");
        return "Ready";
    }

}
