package com.example.repo.location;

import com.example.model.location.Cubicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CubicleRepository extends JpaRepository<Cubicle, Long> {
    List<Cubicle> findByRoomId(Long roomId);
}