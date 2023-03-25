package me.niloybiswas.spfile.controller;

import me.niloybiswas.spfile.dto.FileResponseDTO;
import me.niloybiswas.spfile.service.FileService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<FileResponseDTO> fileUpload(
            @RequestParam("image") MultipartFile image
    ) {
        String fileName = null;
        try {
            fileName = fileService.uploadImage(path, image);
        } catch (IOException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            return new ResponseEntity<>(new FileResponseDTO(null, "Failed to upload!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new FileResponseDTO(fileName, "Successfully uploaded!"), HttpStatus.OK);
    }


    ///* Method to serve files
    @GetMapping(value = "/images/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException {

        InputStream resource = fileService.getResource(path, imageName);
        /*byte[] sourceByte = IOUtils.toByteArray(resource);
        String base64EncodedString = Base64.getEncoder().encodeToString(sourceByte);*/
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }

}
