package com.ngochung.uploadimage.controller;

import com.ngochung.uploadimage.entity.Image;
import com.ngochung.uploadimage.service.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ImageController {
  private final ImageService imageService;

  @Autowired
  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @GetMapping(value = {"/", "/home"})
  public String homePage() {
    return "index";
  }

  @PostMapping("/image/saveImageDetails")
  public @ResponseBody ResponseEntity<?> createProduct(
      @RequestParam("name") String name,
      @RequestParam("price") double price,
      @RequestParam("description") String description,
      final @RequestParam("image") MultipartFile file) {
    try {
      byte[] imageData = file.getBytes();
      Image image = new Image();
      image.setName(name);
      image.setImage(imageData);
      image.setPrice(price);
      image.setDescription(description);
      image.setCreateDate(new Date());
      imageService.saveImage(image);
      return new ResponseEntity<>(
          "Product Saved With File - " + file.getOriginalFilename(), HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/image/display/{id}")
  @ResponseBody
  void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Image> image)
      throws IOException {
    image = imageService.getImageById(id);
    response.setContentType("image/png");
    response.getOutputStream().write(image.get().getImage());
    response.getOutputStream().close();
  }

  @GetMapping("/image/imageDetails")
  String showProductDetails(
      @RequestParam("id") Long id, Optional<Image> image, Model model) {
    try {
      if (id != 0) {
        image = imageService.getImageById(id);

        if (image.isPresent()) {
          model.addAttribute("id", image.get().getId());
          model.addAttribute("description", image.get().getDescription());
          model.addAttribute("name", image.get().getName());
          model.addAttribute("price", image.get().getPrice());
          return "imagedetails";
        }
        return "redirect:/home";
      }
      return "redirect:/home";
    } catch (Exception e) {
      e.printStackTrace();
      return "redirect:/home";
    }
  }

  @GetMapping("/image/show")
  String show(Model map) {
    List<Image> images = imageService.getAllActiveImages();
    map.addAttribute("images", images);
    return "images";
  }
}
