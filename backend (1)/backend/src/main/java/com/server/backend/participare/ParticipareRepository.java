package com.server.backend.participare;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipareRepository extends JpaRepository<Participare,Integer> {
    List<Participare> findByUserId(Integer userId);
    List<Participare> findByFestivalId(Integer festivalId);

}
