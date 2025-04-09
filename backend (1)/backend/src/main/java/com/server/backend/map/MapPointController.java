package com.server.backend.map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/map-points")
public class MapPointController {

    private final MapPointService mapPointService;

    public MapPointController(MapPointService mapPointService) {
        this.mapPointService = mapPointService;
    }

    @PostMapping("/{festivalId}")
    public ResponseEntity<MapPoint> addPoint(@PathVariable Integer festivalId, @RequestBody MapPoint point) {
        MapPoint created = mapPointService.addPoint(festivalId, point);
        if (created != null) {
            return ResponseEntity.ok(created);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{festivalId}")
    public ResponseEntity<List<MapPoint>> getPointsForFestival(@PathVariable Integer festivalId) {
        return ResponseEntity.ok(mapPointService.getPointsByFestival(festivalId));
    }
    @PutMapping("/update/{festivalId}")
    public ResponseEntity<?> updateMapPoints(@PathVariable Integer festivalId, @RequestBody List<MapPoint> newPoints) {
        mapPointService.updatePointsForFestival(festivalId, newPoints);
        return ResponseEntity.ok().build();
    }

}
