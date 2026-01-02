package com.pm.analyticsservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        // Process the incoming message
        try{
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            // perform any business related to analytics here
            log.info("Received PatientEvent: [PatientId={}, PatientName={}, PatientEmail={}]"
            , patientEvent.getPatientId(), patientEvent.getName(), patientEvent.getEmail());
        } catch (Exception e) {
            log.error("Error deserializing PatientEvent message: {}", e.getMessage());
        }
    }
}
