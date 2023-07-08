package com.qpf.basic.service.impl;

import cn.hutool.core.io.file.FileNameUtil;
import com.qpf.basic.service.IFastDfsService;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class FastDfsServiceImpl implements IFastDfsService {

    //用于文件上传或者下载
    @Autowired
    private FastFileStorageClient client;

    //用来获取缩略图路径
    @Autowired
    private ThumbImageConfig thumbImageConfig;

    //用户获取服务器地址
    @Autowired
    private FdfsWebServer webServer;

    /**
     * 获取FastDfs的服务器地址
     * @return FastDfs服务器地址
     */
    @Override
    public String getWebServerUrl() {
        return webServer.getWebServerUrl();
    }

    /**
     * 文件上传
     * @param filePath 被上传文件的路径
     * @return 上传后的路径(不携带服务器路径)
     * @throws FileNotFoundException
     */
    @Override
    public String uploadFile(String filePath) throws FileNotFoundException {
        //0.把路径转换为File对象
        File file = new File(filePath);
        //1.获取文件后缀名
        String extName = FileNameUtil.extName(file);
        //2.文件上传
        StorePath path = client.uploadFile(new FileInputStream(file), file.length(), extName, null);
        //3.获取文件上传路径(组名/a/b/abc.jpg)
        String fullPath = path.getFullPath();
        //4 .返回完整路径
        return fullPath;
    }

    /**
     * 文件上传
     * @param file 被上传文件(MultipartFile对象)
     * @return 上传后的完整路径
     * @throws FileNotFoundException
     */
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        //1.获取文件后缀名
        String extName = FileNameUtil.extName(file.getOriginalFilename());
        //2.文件上传
        StorePath path = client.uploadFile(file.getInputStream(), file.getSize(), extName, null);
        //3.获取文件上传路径(组名/a/b/abc.jpg)
        String fullPath = path.getFullPath();
        //4.返回完整路径
        return fullPath;
    }

    /**
     * 文件上传
     * @param file 被上传文件(MultipartFile对象)
     * @return 上传后的完整路径
     * @throws FileNotFoundException
     */
    @Override
    public String uploadFileAndThumb(MultipartFile file) throws IOException {
        //1.获取文件后缀名
        String extName = FileNameUtil.extName(file.getOriginalFilename());
        //2.文件上传
        StorePath storePath = client.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), extName, null);
        //3.获取文件上传路径(组名/a/b/abc.jpg)
        String fullPath = storePath.getFullPath();
        //4.返回完整路径
        return fullPath;
    }

    /**
     * 路径转换: 正常路径 -> 缩略图路径
     * 注意事项: 必须先使用uploadFileAndThumb上传的文件才可以转换路径.
     * @param filePath 文件路径(全部路径和短路径均可)
     * @return 缩略图路径
     */
    @Override
    public String fullPathToThumb(String filePath) {
        //1.转换为缩略图路径
        String thumbImagePath = thumbImageConfig.getThumbImagePath(filePath);
        //2.返回缩略图路径
        return thumbImagePath;
    }


    /**
     * 下载文件
     * @param filePath 文件路径(全部路径和短路径均可)
     * @return 文件的字节数组
     * @throws IOException
     */
    @Override
    public byte[] downloadFile(String filePath) {
        //1.转换为StorePath对象
        StorePath sp = StorePath.parseFromUrl(filePath);
        //2.获取组名
        String group = sp.getGroup();
        //3.获取路径名
        String path = sp.getPath();
        //4.下载
        byte[] bytes = client.downloadFile(group, path, new DownloadByteArray());
        //5.返回字节数组
        return bytes;
    }

    /**
     * 删除文件
     * @param filePath 文件路径(全部路径和短路径均可)
     * @return 文件的字节数组
     * @throws IOException
     */
    @Override
    public void deleteFile(String filePath) {
        client.deleteFile(filePath);
    }
}