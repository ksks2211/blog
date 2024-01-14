package org.iptime.yoon.blog.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.image.exception.ImageEntityNotFoundException;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.UUID;

import static org.iptime.yoon.blog.common.ErrorResponse.createErrorResponse;

/**
 * @author rival
 * @since 2023-08-13
 */

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {


    private final ImageService imageService;

    private boolean isImageFile(MultipartFile uploadFile){
        return uploadFile !=null && uploadFile.getContentType() != null && uploadFile.getContentType().startsWith("image");
    }

    private String generateRandomFilename(final String username) {
        LocalDate currentDate = LocalDate.now();
        String year = String.valueOf(currentDate.getYear());
        String month = String.format("%02d", currentDate.getMonthValue());
        String day = String.format("%02d", currentDate.getDayOfMonth());
        String uuid = UUID.randomUUID().toString();

        return String.format("%s/%s/%s/%s/%s",username, year, month, day, uuid);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@AuthenticationPrincipal JwtUser jwtUser, MultipartFile uploadFile) throws Exception {
        if(!isImageFile(uploadFile)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String filename = generateRandomFilename(jwtUser.getUsername());
        imageService.uploadImage(uploadFile,filename, jwtUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> downloadImage(
        @PathVariable(name="id") Long id,
        @RequestParam(value = "thumbnail", defaultValue = "false") boolean isThumbnail
    ) throws Exception {
        ImageDto imageDto;

        if (isThumbnail) {
            imageDto = imageService.downloadImageThumbnail(id);
        } else {
            imageDto = imageService.downloadImage(id);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, imageDto.getContentType());


        return new ResponseEntity<>(imageDto.getBytes(), headers, HttpStatus.OK);
    }


    @GetMapping("/url/{id}")
    public RedirectView getImageUrl(@PathVariable(name="id") Long id){
        return new RedirectView(imageService.getImageUrl(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable(name="id")Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }



    @ExceptionHandler(value= ImageEntityNotFoundException.class)
    public ResponseEntity<?> imageNotFoundExceptionHandler(ImageEntityNotFoundException e){
        log.info(e.getClass().getName());
        log.info(e.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
