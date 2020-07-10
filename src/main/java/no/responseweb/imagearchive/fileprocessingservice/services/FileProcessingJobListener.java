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
            log.info("{} operation for file id: {}. Config : {}", fileProcessingJobDto.getFileStoreRequestType(), fileItemDto.getId(), config);
            if (fileItemDto.getId()!=null && config != null) {
                if (config.isMetadataExtractor()) {
                    log.info("Sending {} to metadata extraction. ", fileItemDto.getFilename());
                    forwardMessage(JmsConfig.IMAGE_METADATA_EXTRACTOR_JOB_QUEUE, fileItemDto);
                }
                if (config.isDuplicateDetection()) {
                    log.info("Sending {} to duplicate detection. ", fileItemDto.getFilename());
                    forwardMessage(JmsConfig.IMAGE_DUPLICATE_DETECTION_JOB_QUEUE, ImageDuplicateDetectionJobDto.builder()
                            .fileItemDto(fileItemDto)
                            .build());
                }
                if (config.isFaceDetection()) {
                    log.info("Sending {} to face recognition. ", fileItemDto.getFilename());
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
