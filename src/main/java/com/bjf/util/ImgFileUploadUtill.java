package com.bjf.util;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

@Component
public  class ImgFileUploadUtill {

    @Resource
    private  FastFileStorageClient fastFileStorageClient;



    private static String GROUP_NAME="group1";

    public  String imgUpload(MultipartFile multipartFile) throws IOException {
        InputStream inputStream=multipartFile.getInputStream();
        String filetype=multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1);
        StorePath storePath= fastFileStorageClient.uploadFile(inputStream,multipartFile.getSize(),filetype,null);
        String   imgUrl="http://192.168.0.200/"+storePath.getFullPath();
        return imgUrl;
    }

    public void deleteimg(String fullPath){
            try {
                fastFileStorageClient.deleteFile(fullPath);

            }catch (Exception e){
                e.printStackTrace();
                System.err.println("删除失败");
            }
            System.err.println("删除成功");
        }
}