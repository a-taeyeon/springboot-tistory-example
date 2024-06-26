package com.tistory.project_api.controller;

import com.tistory.framework.core.response.BaseResponse;
import com.tistory.framework.service.FileUploadMultipartService;
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

@Slf4j
@RestController
@RequestMapping("/files/upload")
public class FileUploadController {
    private final String EP_ADD_FILES_MULTIPART = "/multipart";

    @Autowired
    private FileUploadMultipartService fileUploadMultipartService;

    @PostMapping(value = EP_ADD_FILES_MULTIPART, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<List<String>> uploadFile(@RequestParam("files") ArrayList<MultipartFile> files) {
        BaseResponse response = new BaseResponse();
        try {
            List<String> fileNames = fileUploadMultipartService.uploadFiles(files);
            response.setResult(fileNames);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
