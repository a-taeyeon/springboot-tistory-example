package com.tistory.project_api.service;

import com.tistory.framework.service.FileUploadMultipartService;
import com.tistory.project_api.domain.entity.FilesEntity;
import com.tistory.project_api.repository.FilesRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Service
public class FilesService {
    private final FilesRepository filesRepository;
    private final FileUploadMultipartService fileUploadMultipartService;

    @Autowired
    public FilesService(FilesRepository filesRepository, FileUploadMultipartService fileUploadMultipartService) {
        this.filesRepository = filesRepository;
        this.fileUploadMultipartService = fileUploadMultipartService;
    }

    public List<FilesEntity> saveFiles(ArrayList<MultipartFile> files) throws IOException {
        List<FilesEntity> uploadedFiles = fileUploadMultipartService.uploadFiles(files);
        if(uploadedFiles.size() == files.size()){
            return filesRepository.saveAll(uploadedFiles);
        }
        return null;
    }
}
