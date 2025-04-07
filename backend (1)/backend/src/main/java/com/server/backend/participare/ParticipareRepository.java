package com.server.backend.participare;


import com.server.backend.festival.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipareRepository extends JpaRepository<Participare,Integer> {
    @Query("SELECT p.festival FROM Participare p WHERE p.user.id = :userId AND p.status = com.server.backend.participare.Status.PARTICIPA")
    List<Festival> findFestivaluriByUserId(@Param("userId") Integer userId);

}
