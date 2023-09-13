package org.iptime.yoon.blog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.dto.ImageFileDto;
import org.iptime.yoon.blog.dto.res.ErrorResDto;
import org.iptime.yoon.blog.dto.res.ImageMetaResDto;
import org.iptime.yoon.blog.exception.ImageEntityNotFoundException;
import org.iptime.yoon.blog.security.dto.User;
import org.iptime.yoon.blog.service.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

/**
 * @author rival
 * @since 2023-08-13
 */

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {


    private final ImageService imageService;

    private boolean isImageFile(MultipartFile uploadFile){
        return uploadFile !=null && uploadFile.getContentType() != null && uploadFile.getContentType().startsWith("image");
    }

    public String generateRandomFilename(final String username) {
        LocalDate currentDate = LocalDate.now();
        String year = String.valueOf(currentDate.getYear());
        String month = String.format("%02d", currentDate.getMonthValue());
        String day = String.format("%02d", currentDate.getDayOfMonth());
        String uuid = UUID.randomUUID().toString();

        return String.format("%s/%s/%s/%s/%s",username, year, month, day, uuid);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@AuthenticationPrincipal User user, MultipartFile uploadFile) throws Exception {
        if(!isImageFile(uploadFile)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String filename = generateRandomFilename(user.getUsername());
        ImageMetaResDto imageMetaResDto = imageService.uploadImageFile(uploadFile,filename,user.getId());
        return new ResponseEntity<>(imageMetaResDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> downloadImage(
        @PathVariable(name="id") Long id,
        @RequestParam(value = "thumbnail", defaultValue = "false") boolean isThumbnail
    ) throws Exception {
        ImageFileDto imageFileDto;

        if (isThumbnail) {
            imageFileDto = imageService.downloadImageThumbnailFile(id);
        } else {
            imageFileDto = imageService.downloadImageFile(id);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, imageFileDto.getContentType());
        return new ResponseEntity<>(imageFileDto.getBytes(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable(name="id")Long id) {
        imageService.deleteImageFile(id);
        return ResponseEntity.noContent().build();
    }



    @ExceptionHandler(value= ImageEntityNotFoundException.class)
    public ResponseEntity<?> imageNotFoundExceptionHandler(ImageEntityNotFoundException e){
        log.info(e.getClass().getName());
        log.info(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResDto("Image Not Found", e.getMessage()));
    }
}
