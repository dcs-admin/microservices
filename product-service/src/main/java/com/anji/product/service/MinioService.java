package com.anji.product.service;

// import io.minio.MinioClient;
// import io.minio.errors.MinioException;
// import io.minio.messages.Bucket;


// import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class MinioService {

    // //@Autowired
    // MinioClient minioClient;

    // @Value("${minio.buckek.name}")
    // String defaultBucketName;

    // @Value("${minio.default.folder}")
    // String defaultBaseFolder;

    // public List<Bucket> getAllBuckets() {
    //     try {
    //         return minioClient.listBuckets();
    //     } catch (Exception e) {
    //         throw new RuntimeException(e.getMessage());
    //     }

    // }


    // public void uploadFile(String name, byte[] content) {
    //     File file = new File("/tmp/" + name);
    //     file.canWrite();
    //     file.canRead();
    //     try {
    //         FileOutputStream iofs = new FileOutputStream(file);
    //         iofs.write(content);
    //         minioClient.putObject(defaultBucketName, defaultBaseFolder + name, file.getAbsolutePath());
    //     } catch (Exception e) {
    //        throw new RuntimeException(e.getMessage());
    //     }

    // }

    // public byte[] getFile(String key) {
    //     try {
    //         InputStream obj = minioClient.getObject(defaultBucketName, defaultBaseFolder + "/" + key);

    //         byte[] content = IOUtils.toByteArray(obj);
    //         obj.close();
    //         return content;
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }
}
