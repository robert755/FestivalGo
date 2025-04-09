package com.server.backend.map;

import com.server.backend.festival.Festival;
import com.server.backend.festival.FestivalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MapPointService {

    private final MapPointRepository mapPointRepository;
    private final FestivalRepository festivalRepository;

    public MapPointService(MapPointRepository mapPointRepository, FestivalRepository festivalRepository) {
        this.mapPointRepository = mapPointRepository;
        this.festivalRepository = festivalRepository;
    }

    @Transactional
    public MapPoint addPoint(Integer festivalId, MapPoint point) {
        Optional<Festival> festivalOpt = festivalRepository.findById(festivalId);
        if (festivalOpt.isPresent()) {
            point.setFestival(festivalOpt.get());
            return mapPointRepository.save(point);
        }
        return null;
    }

    public List<MapPoint> getPointsByFestival(Integer festivalId) {
        return mapPointRepository.findByFestivalId(festivalId);
    }
    @Transactional
    public void updatePointsForFestival(Integer festivalId, List<MapPoint> newPoints) {
        // Stergem toate punctele existente pentru acel festival
        mapPointRepository.deleteAllByFestivalId(festivalId);

        // Asignăm festivalul pentru fiecare punct și le salvăm
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new RuntimeException("Festivalul nu există"));

        for (MapPoint point : newPoints) {
            point.setFestival(festival); // ⚠️ legătura e obligatorie
            mapPointRepository.save(point);
        }
    }
}
