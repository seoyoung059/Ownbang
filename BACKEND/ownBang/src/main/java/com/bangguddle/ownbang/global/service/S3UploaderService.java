package com.bangguddle.ownbang.global.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface S3UploaderService {

    String uploadToS3(File uploadFile, String dirName);

    String uploadMultipartFileToS3(String fileName, MultipartFile multipartFile, String dirName);
}
