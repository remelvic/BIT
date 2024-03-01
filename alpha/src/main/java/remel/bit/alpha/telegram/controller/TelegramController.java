package remel.bit.alpha.telegram.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import remel.bit.alpha.telegram.service.TelegramServiceImpl;

@RestController
@Slf4j
class TelegramController {

    @Autowired
    private TelegramServiceImpl telegramService;

    @GetMapping(value = "getStatus")
    public boolean GetStatus(){
        boolean result = telegramService.isUserOnline("skripss");
        log.info("User online: " + result);
        return result;
    }
}
