package com.pm.patientservice.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import java.util.UUID;
import com.pm.patientservice.exception.PatientNotFoundException;

@Service
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // Add methods to interact with the patientRepository

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        // Map the patients to patientResponseDTOs and return
        return patients.stream()
                .map(PatientMapper::toDTO)
                .toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with email " + patientRequestDTO.getEmail() + " already exists.");
        }

        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with id " + id + " not found."));

        // Update fields
        //if(patientRepository.existsByEmail(patientRequestDTO.getEmail()) && !existingPatient.getEmail().equals(patientRequestDTO.getEmail())) {
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A different patient with email " + patientRequestDTO.getEmail() + " already exists.");
        }

        existingPatient.setName(patientRequestDTO.getName());
        existingPatient.setEmail(patientRequestDTO.getEmail());
        existingPatient.setAddress(patientRequestDTO.getAddress()); // Assuming address is optional and can be set to null
        existingPatient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(existingPatient);
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
        if(!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient with id " + id + " not found.");
        }
        patientRepository.deleteById(id);
    }
}
