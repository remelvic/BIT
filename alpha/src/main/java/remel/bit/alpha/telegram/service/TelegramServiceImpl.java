package remel.bit.alpha.telegram.service;

//import org.telegram.td.TdApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class TelegramServiceImpl {

    public static native String execute(String query);

    public boolean isUserOnline(String userId){


        log.info("isUserOnline triggered");
        String response = execute("{\"@type\":\"getUser\",\"user_id\":" + userId + "}");
        return parseUserStatus(response);
    }

    private boolean parseUserStatus(String response){
        return !Objects.equals(response, "");
    }
}
