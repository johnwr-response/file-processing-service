package no.responseweb.imagearchive.fileprocessingservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileProcessingServiceJobProducer {

    private final JmsTemplate jmsTemplate;

//    @Scheduled(fixedDelayString = "${response.file.processing.scheduling.fixed-delay-time}", initialDelayString = "${response.file.processing.scheduling.initial-delay-time}")
    public void pushJobsToWalkers() {
    }

}
