package com.example.demo.services;

import com.example.demo.entities.FileEntity;
import com.example.demo.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.demo.utilities.NumberUtility.getRandomInt;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Transactional
    public FileEntity save(MultipartFile file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setContent(file.getBytes());
        return fileRepository.save(fileEntity);
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    public FileEntity getRandomFile() {
        List<FileEntity> files = getAllFiles();
        if(files.isEmpty()){
            throw new NoSuchElementException("No files are found");
        }
        int randomLineIndex = getRandomInt(files.size());
        return files.get(randomLineIndex);
    }
}
