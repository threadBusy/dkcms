package cc.dkcms.cms.common.util;

import cc.dkcms.cms.common.define.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

@Slf4j
public class ImageDownloader {


    public static Result fetch(String imageUrl, String desPath, String desFileName) {

        if (StringUtils.isEmpty(imageUrl) || StringUtils.isEmpty(desPath)) {
            return Result.fail("imageUrl missing or desPath missing");
        }

        int         responseCode;
        InputStream inputStream;
        try {
            URL               url        = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            responseCode = connection.getResponseCode();
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }

        if (responseCode != 200) {
            return Result.fail("responseCode != 200：" + responseCode);
        }

        if (StringUtils.isEmpty(desFileName)) {
            desFileName = FilenameUtils.getName(imageUrl);
            log.info("desFileName empty;auto generate:" + desFileName);
        }
        File desFile = new File(desPath + "/" + desFileName);
        if (desFile.exists()) {
            try {
                String bakFileName = desFile.getAbsoluteFile() + ".bak" + System.currentTimeMillis();
                FileUtils.moveFile(desFile, new File(bakFileName));
            } catch (IOException e) {
                return Result.fail("file exists;cat not move to bak");
            }

        }
        byte[] bytes = new byte[1024];
        try {
            int length;
            while ((length = inputStream.read(bytes)) > 0) {
                FileUtils.writeByteArrayToFile(desFile, bytes, 0, length, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(e.getMessage());
        }
        return Result.success("success");
    }

    public static final String[] MIME_TYPES = ImageIO.getReaderMIMETypes();

    public static void mainIgnore(String[] args) {
        System.out.println(Arrays.asList(MIME_TYPES));
        String url         = "https://mat1.gtimg.com/pingjs/ext2020/qqindex2018/dist/img/qq_logo_2x.png";
        String desFileName = System.getProperty("user.dir") + "/";
        System.out.println(ImageDownloader.fetch(url, desFileName, "a"));
    }
}



