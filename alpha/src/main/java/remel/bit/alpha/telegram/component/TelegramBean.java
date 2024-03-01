package remel.bit.alpha.telegram.component;

import it.tdlight.Init;
import it.tdlight.Log;
import it.tdlight.Slf4JLogMessageHandler;
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import it.tdlight.jni.TdApi.RequestQrCodeAuthentication;
import it.tdlight.util.UnsupportedNativeLibraryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TelegramBean {

    @Bean
    public void register() throws Exception {
        long adminId = Integer.getInteger("it.tdlight.example.adminid", 667900586);
        log.info("ADMIN ID: " + adminId);
        // Initialize TDLight native libraries
        Init.init();

        // Set the log level
        Log.setLogMessageHandler(1, new Slf4JLogMessageHandler());

        // Create the client factory (You can create more than one client,
        // BUT only a single instance of ClientFactory is allowed globally!
        // You must reuse it if you want to create more than one client!)
        try (SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory()) {
            // Obtain the API token
            //
            // var apiToken = new APIToken(your-api-id-here, "your-api-hash-here");
            //
            TdApi.InternalLinkTypeUserToken client = new TdApi.InternalLinkTypeUserToken("scripss");
//            APIToken apiToken = APIToken.example();
            var apiToken = new APIToken(23078775, "d16cd9eb5cff89c95578fe0579dc7d97");

            log.info("API token: " + apiToken);
            log.info("Client info: "+ client);

            // Configure the client
            TDLibSettings settings = TDLibSettings.create(apiToken);

            log.info("Client settings: " + settings);

            // Configure the session directory.
            // After you authenticate into a session, the authentication will be skipped from the next restart!
            // If you want to ensure to match the authentication supplier user/bot with your session user/bot,
            //   you can name your session directory after your user id, for example: "tdlib-session-id12345"
            Path sessionPath = Paths.get("example-tdlight-session");
            settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
            settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));

            // Prepare a new client builder
            SimpleTelegramClientBuilder clientBuilder = clientFactory.builder(settings);

            // Configure the authentication info
            // Replace with AuthenticationSupplier.consoleLogin(), or .user(xxx), or .bot(xxx);

//            SimpleAuthenticationSupplier<?> authenticationData = AuthenticationSupplier.testUser(7381);
            RequestQrCodeAuthentication neco = new RequestQrCodeAuthentication();

            SimpleAuthenticationSupplier<?> authenticationData = AuthenticationSupplier.user("");
            // This is an example, remove this line to use the real telegram datacenters!
//            settings.setUseTestDatacenter(true);

            // Create and start the client
            try (var app = new ExampleApp(clientBuilder, authenticationData, adminId)) {
                // Get me
                TdApi.User me = app.getClient().getMeAsync().get(1, TimeUnit.MINUTES);

                log.info("GET ME: " + me);
                // Send a test message
                var req = new TdApi.SendMessage();
                req.chatId = me.id;
                var txt = new TdApi.InputMessageText();
                txt.text = new TdApi.FormattedText("TDLight test", new TdApi.TextEntity[0]);
                req.inputMessageContent = txt;
                TdApi.Message result = app.getClient().sendMessage(req, true).get(1, TimeUnit.MINUTES);
                System.out.println("Sent message:" + result);
            }
        }
    }
}
