package me.niloybiswas.spfile.serviceImpl;

import me.niloybiswas.spfile.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        ///* file name
        String fileName = null;
        String fileActualName = null;
        String fileActualExt = null;

        String[] fileFrags = file.getOriginalFilename().split("\\.");

        if (fileFrags.length > 1) {
            fileActualName = fileFrags[0];
            fileActualExt = fileFrags[fileFrags.length - 1];
            fileName = fileActualName + " " + new Date() + "." + fileActualExt;
        } else {
            fileActualName = fileFrags[0];
            fileName = fileActualName + " " + new Date();
        }

        ///* full path
        String filePath = path + File.separator + fileName;

        ///* create folder if not exist
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        ///* file copy
        Files.copy(file.getInputStream(), Paths.get(filePath));

        ///* can call repo and save it to db

        return fileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {

        String fullPath = path + File.separator + fileName;
        InputStream inputStream = new FileInputStream(fullPath);
        ///* db logic to return input stream
        return inputStream;

    }

}
