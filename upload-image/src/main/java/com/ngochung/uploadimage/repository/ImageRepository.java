package com.ngochung.uploadimage.repository;

import com.ngochung.uploadimage.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
