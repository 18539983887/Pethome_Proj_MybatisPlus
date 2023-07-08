package com.qpf.pethome_server;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
public class FastDFSTest {

    /**
     * 测试FastDFS的文件上传
     */

    //用于文件上传或者下载
    @Autowired
    private FastFileStorageClient client;

    @Autowired
    private FdfsWebServer webServer;

    @Test
    public void testUpload() throws FileNotFoundException {
        //1、指定文件
        File file = new File("C:\\Users\\17320\\Pictures\\Saved Pictures\\TOUXIANG.jpg");
        //2.获取文件后缀名
        String extName = FileNameUtil.extName(file);
        //2、文件上传
        StorePath path = client.uploadFile(new FileInputStream(file), file.length(), extName, null);
        //3、拼接请求路径
        String fullPath = path.getFullPath();
        System.out.println(fullPath);  //a/b/abc.jpg;
        String url = webServer.getWebServerUrl() + fullPath;
        System.out.println(url);
    }

    @Test
    public void testDownload() throws FileNotFoundException {

        String fullPath = "http://192.168.136.133:8888/group1/M00/00/00/wKiIhWSgDWGAbRhJAALZM0Vww_w766.jpg";

        String group = StorePath.parseFromUrl(fullPath).getGroup();
        System.out.println(group);
        String path = StorePath.parseFromUrl(fullPath).getPath();
        System.out.println(path);

        byte[] bytes = client.downloadFile(group, path, new DownloadByteArray());
    }

    @Test
    public void testDelete() throws FileNotFoundException {
        String fullPath = "http://192.168.136.133:8888/group1/M00/00/00/wKiIhWSgDWGAbRhJAALZM0Vww_w766.jpg";
        client.deleteFile(fullPath);
    }
}
