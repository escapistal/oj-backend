package com.xc.oj.service;

import com.xc.oj.entity.SubmissionBase;
import com.xc.oj.repository.SubmissionBaseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubmissionBaseService {

    final SubmissionBaseRepository submissionBaseRepository;

    public SubmissionBaseService(SubmissionBaseRepository submissionBaseRepository) {
        this.submissionBaseRepository = submissionBaseRepository;
    }

    public Optional<SubmissionBase> findById(Long id){
        return submissionBaseRepository.findById(id);
    }

    public void save(SubmissionBase submissionBase){
        submissionBaseRepository.save(submissionBase);
    }
}
