package com.post_show_blues.vine.file;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class FileStore {

    @Value("${org.zerock.upload.path}")
    private String uploadFolder;

    public String getFullPath(String folderPath, String storeFileName){

        return uploadFolder + File.separator + folderPath + File.separator + storeFileName;

    }


    public List<ResultFileStore> storeFiles(List<MultipartFile> multipartFiles) throws IOException {

        List<ResultFileStore> storeFileResult = new ArrayList<>();

        if (!multipartFiles.isEmpty()) {
            for (MultipartFile multipartFile : multipartFiles) {

                storeFileResult.add(storeFile(multipartFile));
            }
        }

        return storeFileResult;
    }



    public ResultFileStore storeFile(MultipartFile multipartFile) throws IOException {

        if(multipartFile.isEmpty()){
            return null;
        }

        //파일 이름
        String originalFilename = multipartFile.getOriginalFilename();
        log.info("originalFilename: " + originalFilename);

        //파일 저장 이름
        String storeFileName = createStoreFileName(originalFilename);
        log.info("storeFileName: " + storeFileName);

        //폴더 생성
        String folderPath = makeFolder();

        //이미지 저장
        multipartFile.transferTo(new File(getFullPath(folderPath, storeFileName)));

        return new ResultFileStore(folderPath, storeFileName);

    }

    private String createStoreFileName(String originalFileName) {

        String uuid = UUID.randomUUID().toString();

        return uuid + "_" + originalFileName;


    }

    private String makeFolder() {

        String folderPath = "vine" + File.separator;

        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        folderPath += str.replace("//", File.separator);

        log.info("folderPath: " + folderPath);

        File uploadPathFolder = new File(uploadFolder, folderPath);

        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }

        return folderPath;

    }


}
