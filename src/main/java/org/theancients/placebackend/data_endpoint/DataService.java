package org.theancients.placebackend.data_endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.theancients.placebackend.highlight.HighlightRepository;
import org.theancients.placebackend.highlight.HighlightService;
import org.theancients.placebackend.job.JobRepository;
import org.theancients.placebackend.job.JobService;

@Service
public class DataService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private HighlightRepository highlightRepository;

    public CurrentStateDto getCurrentState() {
        CurrentStateDto currentStateDto = new CurrentStateDto();
        currentStateDto.setJobs(jobRepository.findAll());
        currentStateDto.setHighlights(highlightRepository.findAll());

        return currentStateDto;
    }

}
