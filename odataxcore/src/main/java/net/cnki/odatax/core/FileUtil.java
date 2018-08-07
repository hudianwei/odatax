package net.cnki.odatax.core;

import net.cnki.odatax.exception.SystemException;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.UUID;

/**
 * @author hudianwei
 * @date 2018/8/2 15:26
 */
public class FileUtil {
    /**
     * @Description: 删除文件
     * @Param: [file]
     * @return: void
     * @Author: HU
     * @Date: 2018/8/5
     */

    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File subFile : files) {
                delete(subFile);
            }
            file.delete();
        } else {
            file.delete();
        }
    }

    /**
     * @Description: 读取文本文件
     * @Param: [in, charset]
     * @return: java.lang.String
     * @Author: HU
     * @Date: 2018/8/6
     */

    public static String read(InputStream in, String charset) {
        StringBuilder textBuilder = new StringBuilder();
        BufferedReader buf = null;
        String s = null;
        try {
            buf = new BufferedReader(new InputStreamReader(in, charset));
            while ((s = buf.readLine()) != null) {
                textBuilder.append(s);
            }
            return textBuilder.toString();
        } catch (Throwable e) {
            throw new SystemException(e);
        } finally {
            if (buf != null) {
                try {
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @Description: 文件拷贝
     * @Param: [source, out, bufferSize] 文件输入目录
     * @return: void
     * @Author: HU
     * @Date: 2018/8/6
     */

    public static void copy(String source, OutputStream out, int bufferSize) {
        InputStream is = null;
        try {
            is = new FileInputStream(new File(source));
            copy(is, out, bufferSize);
        } catch (FileNotFoundException e) {
            throw new SystemException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Logging.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * @Description: 文件拷贝
     * @Param: [in, out, bufferSize]
     * @return: void
     * @Author: HU
     * @Date: 2018/8/6
     */

    public static void copy(InputStream in, OutputStream out, int bufferSize) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(in);
            bos = new BufferedOutputStream(out);
            byte[] b = new byte[bufferSize];
            int len;
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
        } catch (IOException e) {
            throw new SystemException(e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    Logging.error(e.getMessage(), e);
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    Logging.error(e.getMessage(), e);
                }
            }
        }
    }

    public static void copy(InputStream in, String target, int bufferSize) {
        new File(target).getParentFile().mkdirs();
        try {
            copy(in, new FileOutputStream(target), bufferSize);
        } catch (FileNotFoundException e) {
            throw new SystemException(e);
        }
    }

    public static String zipTempDir(File tempDirFile) {
        String zipFilePath = tempDirFile.getAbsolutePath() + File.separator + UUID.randomUUID().toString() + ".zip";
        BufferedOutputStream bos = null;
        try {
            File[] files = tempDirFile.listFiles();
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath));
            zos.setEncoding("GBK");
            bos = new BufferedOutputStream(zos);
            BufferedInputStream bis = null;
            for (File file : files) {
                zos.putNextEntry(new ZipEntry(file.getName()));
                bis = new BufferedInputStream(new FileInputStream(file));
                try {
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = bis.read(b)) != -1) {
                        bos.write(b, 0, len);
                    }
                } catch (Exception e) {
                    throw new Exception(e);
                } finally {
                    bis.close();
                }
                bos.flush();
            }
        } catch (Throwable e) {
            throw new SystemException(e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    Logging.error(e.getMessage(), e);
                }
            }
        }
        return zipFilePath;
    }

}
