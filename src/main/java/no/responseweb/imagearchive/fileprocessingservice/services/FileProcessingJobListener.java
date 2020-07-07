package no.responseweb.imagearchive.fileprocessingservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.responseweb.imagearchive.fileprocessingservice.config.JmsConfig;
import no.responseweb.imagearchive.model.*;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileProcessingJobListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.FILE_PROCESSING_JOB_QUEUE)
    public void listen(FileProcessingJobDto fileProcessingJobDto) {
        if (fileProcessingJobDto!=null) {
            FileProcessingJobForwarderConfig config = fileProcessingJobDto.getConfig();
            FileItemDto fileItemDto = fileProcessingJobDto.getFileItemDto();
            if (fileItemDto!=null && fileItemDto.getId()!=null && config != null) {
                if (config.isMetadataExtractor()) {
                    forwardMessage(JmsConfig.IMAGE_METADATA_EXTRACTOR_JOB_QUEUE, fileItemDto);
                }
                if (config.isDuplicateDetection()) {
                    forwardMessage(JmsConfig.IMAGE_DUPLICATE_DETECTION_JOB_QUEUE, ImageDuplicateDetectionJobDto.builder()
                            .fileItemIdQuery(fileItemDto.getId())
                            .build());
                }
                if (config.isFaceDetection()) {
                    forwardMessage(JmsConfig.IMAGE_FACE_DETECTION_JOB_QUEUE, ImageFaceDetectionJobDto.builder()
                            .fileItemId(fileItemDto.getId())
                            .build());
                }
            }
        }
    }

    private void forwardMessage(String destinationName, Object message) {
        if (destinationName != null && message != null) {
            jmsTemplate.convertAndSend(destinationName,message);
        }
    }
}
