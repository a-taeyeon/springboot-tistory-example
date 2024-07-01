package com.tistory.project_api.controller;

import com.tistory.framework.core.response.BaseResponse;
import com.tistory.framework.service.FileUploadMultipartService;
import com.tistory.project_api.domain.entity.FilesEntity;
import com.tistory.project_api.service.FilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@RestController
@RequestMapping("/files/upload")
public class FileUploadController {
    private final String EP_ADD_FILES_MULTIPART = "/multipart";
    private final String EP_ADD_FILES_MULTIPART_ASYNC = "/multipart-async";
    private final String EP_ADD_FILES_MULTIPART_PARALLEL = "/multipart-parallel";

    @Autowired
    private FileUploadMultipartService fileUploadMultipartService;
    @Autowired
    FilesService filesService;

    /**
     * 단순 Multipart
     */
    @PostMapping(value = EP_ADD_FILES_MULTIPART, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse uploadFile(@RequestParam("files") ArrayList<MultipartFile> files) {
        BaseResponse response = new BaseResponse();
        try {
            List<FilesEntity> uploadedFiles = filesService.saveFiles(files);

            response.setResult(uploadedFiles);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = EP_ADD_FILES_MULTIPART + "/all")
    public BaseResponse getAllFiles() {
        BaseResponse response = new BaseResponse();
        List<FilesEntity> uploadedFiles = filesService.getAllFiles();
        response.setResult(uploadedFiles);
        return response;
    }

    @GetMapping(value = EP_ADD_FILES_MULTIPART)
    public BaseResponse getFiles(@RequestParam("id") Long id) {
        BaseResponse response = new BaseResponse();
        Optional<FilesEntity> fileInfo = filesService.getFileById(id);
        response.setResult(fileInfo);
        return response;
    }

    @DeleteMapping(value = EP_ADD_FILES_MULTIPART)
    public BaseResponse deleteFiles(@RequestParam("ids") List<Long> id) {
        BaseResponse response = new BaseResponse();
        filesService.deleteFile(id);
        return response;
    }

    /**
     * 비동기 Async
     */
    @PostMapping(value = EP_ADD_FILES_MULTIPART_ASYNC, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<List<String>> uploadFileAsync(@RequestParam("files") ArrayList<MultipartFile> files) {
        BaseResponse response = new BaseResponse();
        for (MultipartFile file : files) {
            try {
                fileUploadMultipartService.uploadFileAsync(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return response;
    }

    /**
     * 병렬 Parallel
     */
    @PostMapping(value = EP_ADD_FILES_MULTIPART_PARALLEL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<List<String>> uploadFileParallel(@RequestParam("files") ArrayList<MultipartFile> files) {
        BaseResponse response = new BaseResponse();

        List<Future<String>> futures = fileUploadMultipartService.uploadFileParallel(files);
        List<String> uploadedFiles = new ArrayList<>();
        for(Future<String> future : futures){
            try {
                uploadedFiles.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        response.setResult(uploadedFiles);
        return response;
    }
}
