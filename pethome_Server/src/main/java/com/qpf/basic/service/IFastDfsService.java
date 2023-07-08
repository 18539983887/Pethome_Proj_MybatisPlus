package com.qpf.basic.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IFastDfsService {


    /**
     * 获取FastDfs的服务器地址
     * @return FastDfs服务器地址
     */
    public String getWebServerUrl();

    /**
     * 文件上传
     *
     * @param filePath 被上传文件的路径
     * @return 上传后的完整路径
     * @throws FileNotFoundException
     */
    public String uploadFile(String filePath) throws FileNotFoundException;

    /**
     * 文件上传
     *
     * @param file 被上传文件(MultipartFile对象)
     * @return 上传后的完整路径
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException;

    /**
     * 文件上传并生成缩略图
     * @param file 被上传文件(MultipartFile对象)
     * @return 上传后的完整路径(非缩略图路径)
     * @throws FileNotFoundException
     */
    public String uploadFileAndThumb(MultipartFile file) throws IOException ;

    /**
     * 路径转换: 正常路径 -> 缩略图路径
     * 注意事项: 必须先使用uploadFileAndThumb上传的文件才可以转换路径.
     * @param filePath 文件路径(全部路径和短路径均可)
     * @return 缩略图路径
     */
    public String fullPathToThumb(String filePath);

    /**
     * 下载文件
     *
     * @param filePath 文件路径(全部路径和短路径均可)
     * @return 文件的字节数组
     * @throws IOException
     */
    public byte[] downloadFile(String filePath);

    /**
     * 删除文件
     *
     * @param filePath 文件路径(全部路径和短路径均可)
     * @return 文件的字节数组
     * @throws IOException
     */
    public void deleteFile(String filePath);
}
