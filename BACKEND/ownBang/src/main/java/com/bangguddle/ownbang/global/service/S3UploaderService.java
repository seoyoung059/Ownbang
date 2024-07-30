package com.bangguddle.ownbang.global.service;

import java.io.File;

public interface S3UploaderService {

    String uploadToS3(File uploadFile, String dirName);
}
