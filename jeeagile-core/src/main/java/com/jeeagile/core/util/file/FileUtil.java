package com.jeeagile.core.util.file;

import com.jeeagile.core.util.DateUtil;
import com.jeeagile.core.util.StringUtil;
import com.jeeagile.core.util.system.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
@Slf4j
public class FileUtil {

    /**
     * 获取文件系统分隔符
     *
     * @return
     */
    public static String getFileSeparator() {
        return SystemUtil.get("file.separator");
    }

    /**
     * 获取文件保存的文件夹
     *
     * @return
     */
    public static String getFilePath() {
        String dateStr = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
        return dateStr.replace("-", getFileSeparator()) + getFileSeparator() + getFileUuidName();
    }


    /**
     * 获取随机文件名
     *
     * @return
     */
    public static String getFileUuidName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getFileExtName(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtil.isEmpty(extension))
        {
            extension = FileTypeUtil.getExtension(file.getContentType());
        }
        return extension;
    }

    /**
     * 将文件名解析成文件的上传路径
     */
    public static String upload(MultipartFile multipartFile, String filePath) {
        String fileExtName = getFileExtName(multipartFile);
        String filePathName = getFilePath() + "." + fileExtName;
        String path = filePath + getFileSeparator() + filePathName;
        try {
            File dest = new File(path).getCanonicalFile();
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            // 文件写入
            multipartFile.transferTo(dest);
            return path;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
