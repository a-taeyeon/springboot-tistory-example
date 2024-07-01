package com.tistory.project_api.controller;

import com.tistory.framework.core.response.BaseResponse;
import com.tistory.framework.service.FileUploadMultipartService;
import com.tistory.project_api.domain.entity.FilesEntity;
import com.tistory.project_api.service.FilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    @PostMapping(value = EP_ADD_FILES_MULTIPART, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<List<String>> uploadFile(@RequestParam("files") ArrayList<MultipartFile> files) {
        BaseResponse response = new BaseResponse();
        try {
//            List<FilesEntity> uploadedFiles = fileUploadMultipartService.uploadFiles(files);
            List<FilesEntity> uploadedFiles = filesService.saveFiles(files);

            response.setResult(uploadedFiles);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
