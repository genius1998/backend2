package com.example.demo2.service;
import com.example.demo2.entity.Photo;
import com.example.demo2.entity.Polyline;
import com.example.demo2.repository.PhotoRepository;
import com.example.demo2.repository.PolylineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PolylineService {

    private final PolylineRepository polylineRepository;
    private final PhotoRepository photoRepository;

    public PolylineService(PolylineRepository polylineRepository, PhotoRepository photoRepository) {
        this.polylineRepository = polylineRepository;
        this.photoRepository = photoRepository;
    }

    @Transactional
    public void addPhotoToPolyline(Long polylineId, String photoPath, String description) {
        Polyline polyline = polylineRepository.findById(polylineId)
                .orElseThrow(() -> new IllegalArgumentException("Polyline not found"));

        Photo photo = new Photo();
        photo.setPolyline(polyline);
        photo.setPhotoPath(photoPath);
        photo.setDescription(description);

        photoRepository.save(photo);
    }
}

