package com.server.backend.map;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapPointRepository extends JpaRepository<MapPoint, Integer> {
    List<MapPoint> findByFestivalId(Integer festivalId);
    @Modifying
    @Transactional
    @Query("DELETE FROM MapPoint p WHERE p.festival.id = :festivalId")
    void deleteAllByFestivalId(@Param("festivalId") Integer festivalId);

}
