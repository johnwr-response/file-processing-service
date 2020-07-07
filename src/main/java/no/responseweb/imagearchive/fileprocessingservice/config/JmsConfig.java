package no.responseweb.imagearchive.fileprocessingservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {
    public static final String FILE_PROCESSING_JOB_QUEUE = "file-processing-job";
    public static final String IMAGE_METADATA_EXTRACTOR_JOB_QUEUE = "image-metadata-extractor-job";
    public static final String IMAGE_DUPLICATE_DETECTION_JOB_QUEUE = "image-duplicate-detection-job";
    public static final String IMAGE_FACE_DETECTION_JOB_QUEUE = "image-face-detection-job";

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);
        return converter;
    }

}
