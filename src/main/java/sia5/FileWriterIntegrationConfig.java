package sia5;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.MessageChannel;

import java.io.File;

@Configuration
public class FileWriterIntegrationConfig {

    //is used only if integration flow configured in xml
//  @Profile("xmlconfig")
//  @Configuration
//  @ImportResource("classpath:/filewriter-config.xml")
//  public static class XmlConfiguration {}

    @Profile("javaconfig")
    @Bean
    @Transformer(inputChannel = "textInChannel",
            outputChannel = "fileWriterChannel")
    public GenericTransformer<String, String> upperCaseTransformer() {
        return text -> text.toUpperCase();
    }

    @Profile("javaconfig")
    @Bean
    @ServiceActivator(inputChannel = "fileWriterChannel")
    public FileWritingMessageHandler fileWriter() {
        FileWritingMessageHandler handler =
                new FileWritingMessageHandler(new File("/tmp/sia5/files"));
        handler.setExpectReply(false);
        handler.setFileExistsMode(FileExistsMode.APPEND);
        handler.setAppendNewLine(true);
        return handler;
    }

//   этот бин будет создан автоматически, а такое его определенние нужно, если мы захотим переопределить его конфиги
//    @Bean
//    public MessageChannel textInChannel() {
//        DirectChannel channel = new DirectChannel();
//        return channel;
//    }


    //
    // DSL Configuration
    //
//  @Profile("javadsl")
//  @Bean
//  public IntegrationFlow fileWriterFlow() {
//    return IntegrationFlows
//        .from(MessageChannels.direct("textInChannel"))
//        .<String, String>transform(t -> t.toUpperCase())
//        .handle(Files
//            .outboundAdapter(new File("/tmp/sia5/files"))
//            .fileExistsMode(FileExistsMode.APPEND)
//            .appendNewLine(true))
//        .get();
//  }

}
