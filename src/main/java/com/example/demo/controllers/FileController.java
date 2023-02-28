package com.example.demo.controllers;

import com.example.demo.dtos.*;
import com.example.demo.entities.FileEntity;
import com.example.demo.exceptions.FileException;
import com.example.demo.services.FileService;
import com.example.demo.utilities.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.example.demo.Constants.*;
import static com.example.demo.controllers.AbstractController.FILE_API;
import static com.example.demo.utilities.FileUtility.*;
import static com.example.demo.utilities.NumberUtility.getRandomInt;

@RestController
@RequestMapping(FILE_API)
public class FileController extends AbstractController<AbstractDTO>{

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/upload")
    public ResponseEntity<ResponseDTO<AbstractDTO>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            validateFile(file,MAX_FILE_SIZE, List.of(TEXT_EXTENSION));
            FileEntity savedFile = fileService.save(file);
            if(savedFile == null){
                throw new FileException("Unable to save file");
            }
            MostOccurringLetterDTO dto = getDTO(savedFile, MostOccurringLetterDTO.class,
                    StringUtility::findMostFrequentCharacter, MostOccurringLetterDTO::setMostOccurringLetter);
            String infoMessage = "File "+savedFile.getName()+" uploaded successfully.";
            return getResponseDTO(dto, infoMessage,logger,HttpStatus.OK,LogStatus.INFO);

        } catch (IOException | FileException e) {
            String errorMessage = "Error uploading file: " + e.getMessage();
            return getResponseDTO(null, errorMessage,logger,HttpStatus.BAD_REQUEST,LogStatus.ERROR);
        }
    }

    @GetMapping("/all/lines/backward")
    public ResponseEntity<ResponseDTO<AbstractDTO>> getReverseRandomLineFromAllFiles() {
        try {
            List<FileEntity> files = fileService.getAllFiles();
            BackwardLinesListDTO output = new BackwardLinesListDTO();
            for(FileEntity file : files){
                output.addItem(getDTO(file, BackwardLineDTO.class,StringUtility::reverseString,
                        BackwardLineDTO::setBackwardLine));
            }
            String infoMessage = "Getting List of backward lines is done successfully.";
            return getResponseDTO(output, infoMessage,logger,HttpStatus.OK,LogStatus.INFO);
        }catch (IOException | FileException e){
            String errorMessage = "Error getting reversed random line: " + e.getMessage();
            return getResponseDTO(null, errorMessage,logger,HttpStatus.BAD_REQUEST,LogStatus.ERROR);
        }
    }

    @GetMapping("/all/lines/longest")
    public ResponseEntity<ResponseDTO<AbstractDTO>> getLongestLinesFromAllFiles() {
        try {
            List<FileEntity> files = fileService.getAllFiles();
            LongestLinesDTO output = new LongestLinesDTO();
            output.addAll(getLongestLinesFromFilesList(files,ALL_FILES_LINES_COUNT));
            String infoMessage = "Getting Longest 100 lines of all files is done successfully.";
            return getResponseDTO(output, infoMessage,logger,HttpStatus.OK,LogStatus.INFO);
        }catch (IOException e){
            String errorMessage = "Error gLongest 100 lines of all files: " + e.getMessage();
            return getResponseDTO(null, errorMessage,logger,HttpStatus.BAD_REQUEST,LogStatus.ERROR);
        }
    }

    @GetMapping("/random/longest")
    public ResponseEntity<ResponseDTO<AbstractDTO>> getLongestLinesFromRandomFile() {
        try {
            FileEntity fileEntity = fileService.getRandomFile();
            LongestLinesDTO output = new LongestLinesDTO();
            List<String> longestLines = new ArrayList<>();
            getLongestLinesFromFile(fileEntity,RANDOM_FILE_LINES_COUNT,longestLines);
            output.addAll(longestLines);
            String infoMessage = "Getting Longest 20 lines from "+fileEntity.getName()+" is done successfully.";
            return getResponseDTO(output, infoMessage,logger,HttpStatus.OK,LogStatus.INFO);
        }catch (IOException e){
            String errorMessage = "Error getting Longest 20 lines : " + e.getMessage();
            return getResponseDTO(null, errorMessage,logger,HttpStatus.BAD_REQUEST,LogStatus.ERROR);
        }
    }

    private <T extends FileInfoDTO> T getDTO(FileEntity file, Class<T> dtoClass, Function<String, ?> lineProcessor,
                                             BiConsumer<T, String> setter) throws IOException, FileException {
        try {
            T dto = dtoClass.getDeclaredConstructor().newInstance();
            dto.setFileName(file.getName());
            int randomLineIndex = getRandomInt(getLinesCount(file));
            dto.setLineNumber(randomLineIndex);
            String line = getRandomLine(file, randomLineIndex);
            var output = lineProcessor.apply(line);
            setter.accept(dto, String.valueOf(output));
            return dto;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            logger.error("Unable to create dto class: {} cause: {}",dtoClass,e);
            throw new FileException("An internal error occurred while creating a response");
        }
    }




}
