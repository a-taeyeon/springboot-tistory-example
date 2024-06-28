package com.tistory.framework.service;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * MultipartFile을 이용한 파일 업로드 구현
 */
@Service
@Slf4j
public class FileUploadMultipartService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    // 썸네일 기준 방향
    public static final String FILE_THUMB_DIRECTION_WIDTH = "width";
    public static final String FILE_THUMB_DIRECTION_HEIGHT = "height";

    public List<String> uploadFiles(ArrayList<MultipartFile> files) throws IOException {

        List<String> uploadedFileNames = new ArrayList<>();

        // 업로드 디렉터리가 없으면 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {
            // 고유한 파일 이름 생성
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // 파일 저장
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            if(isImageFile(fileExtension)) {
                // 파일이 이미지인 경우 썸네일 생성
                File savedFile = filePath.toFile();
                String thumbnailFilename = generateThumbnail(savedFile, uploadPath.toString(), uniqueFilename, 500, FILE_THUMB_DIRECTION_HEIGHT);
            }

            uploadedFileNames.add(uniqueFilename);
        }

        return uploadedFileNames;
    }

    private boolean isImageFile(String fileExtension) {
        String lowerCaseExtension = fileExtension.toLowerCase();
        return lowerCaseExtension.equals(".jpg") || lowerCaseExtension.equals(".jpeg") || lowerCaseExtension.equals(".png") || lowerCaseExtension.equals(".gif");
    }

    /**
     *
     * @param file 썸네일 만들 대상 이비지
     * @param filePath 파일 저장 경로
     * @param fileName 파일명
     * @param thumbBaseSize - 썸네일은 만들때 가로/세로 기준의 사이즈
     * @param thumbDirection - 썸네일 만들때 사이즈 기준 방향 (width, height) FILE_THUMB_DIRECTION_WIDTH, FILE_THUMB_DIRECTION_HEIGHT
     * @return
     * @throws IOException
     */
    public String generateThumbnail(File file, String filePath, String fileName, int thumbBaseSize, String thumbDirection) throws IOException {
        // 확장자
        String ext = FilenameUtils.getExtension(fileName);
        fileName = Paths.get(fileName).getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            fileName = fileName.substring(0, lastDotIndex);
        }

        String thumbnailFileName =  fileName + "_thumb" + "." + ext;


        String saveFilePath = String.format("%s/%s", filePath , thumbnailFileName);
        log.error("thumb file name : {}", thumbnailFileName);
        log.error("thumb file name : {}", file.getName());
        log.error("thumb file name : {}", file.getAbsolutePath());

        // origin size
        BufferedImage imageFile = ImageIO.read(file);

        if (imageFile == null) {
            log.error("############ Image is Null");
        }
        int width = 0;
        int height = 0;

        log.error("이미지 원본 사이즈 : {} x {}", imageFile.getWidth(), imageFile.getHeight());

        // 사이즈  = (기준 사이즈 * 변경되어야 하는 사이즈 (원본 - 가로 / 세로)) / (원본의 기준 너비 / 또는 높이)
        // 세로를 기준으로 하는경우 가로를 변경해야함
        // 가로사이즈 = (기준사이즈 * 원본 가로 사이즈) / 원본 세로 사이즈

        // 가로를 기준으로 하는 경우 세로를 변경함
        // 세로 사이즈 = (기준 사이즈 * 원본 세로 사이즈) / 원본 가로 사이

        switch (thumbDirection) {
            // 가로기준
            case FILE_THUMB_DIRECTION_HEIGHT:
                if (imageFile.getHeight() > thumbBaseSize) {
                    width = (thumbBaseSize * imageFile.getWidth()) / imageFile.getHeight();
                    height = thumbBaseSize;

                } else {
                    width = imageFile.getWidth();
                    height = imageFile.getHeight();
                }
                break;
            case FILE_THUMB_DIRECTION_WIDTH:
                if (imageFile.getWidth() > thumbBaseSize) {
                    width = thumbBaseSize;
                    height = (thumbBaseSize * imageFile.getHeight()) / imageFile.getWidth();

                } else {
                    width = imageFile.getWidth();
                    height = imageFile.getHeight();
                }
                break;
            default:
                width = imageFile.getWidth() / 10; // 10배 줄이기
                height = imageFile.getHeight() / 10;
                break;
        }

        Thumbnails.of(file)
                .size(width,height)
                .toFile(saveFilePath);


        return thumbnailFileName;
    }

    @Async
    public void uploadFileAsync(MultipartFile file) throws IOException {
        // 고유한 파일 이름 생성
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        File destinationFile = new File(uploadDir + File.separator + uniqueFilename);
        file.transferTo(destinationFile);
        log.info("Async File uploaded : {}", file.getOriginalFilename());
    }
}
