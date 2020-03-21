package com.xc.oj.service;

import com.xc.oj.entity.ContestSubmission;
import com.xc.oj.repository.ContestSubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContestSubmissionService {

    final ContestSubmissionRepository contestSubmissionRepository;

    public ContestSubmissionService(ContestSubmissionRepository contestSubmissionRepository) {
        this.contestSubmissionRepository = contestSubmissionRepository;
    }

    public Optional<ContestSubmission> findById(Long id){
        return contestSubmissionRepository.findById(id);
    }

    public void save(ContestSubmission contestSubmission){
        contestSubmissionRepository.save(contestSubmission);
    }
}
