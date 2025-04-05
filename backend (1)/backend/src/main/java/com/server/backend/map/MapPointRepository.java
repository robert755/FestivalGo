package com.server.backend.map;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MapPointRepository extends JpaRepository<MapPoint, Integer> {
    List<MapPoint> findByFestivalId(Integer festivalId);
}
