package org.theancients.placebackend.job;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Integer> {
}
