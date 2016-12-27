package com.leo618.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


/**
 * function : 存储及文件工具类.
 *
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
 *
 * app目录：("/mnt/storage0/Android/data/packagename") if the phone has SD card,else return path("data/data/packagename/cache/")
 */
@SuppressWarnings({"WeakerAccess", "unused", "ResultOfMethodCallIgnored", "JavaDoc"})
public class FileStorageUtil {
    private static final String TAG = FileStorageUtil.class.getSimpleName();

    /** 日志记录目录 */
    public static final String PATH_BASE_LOG = "/.crashLog/";
    /** 临时文件目录 */
    public static final String PATH_BASE_TEMP = "/temp/";
    /** 公共图片文件目录 */
    public static final String PATH_BASE_PICTURE = "/picture/";

    /**
     * 初始化App的文件目录，创建好所需要的目录文件
     */
    public static void initAppDir() {
        getAppCacheDirPath();
        getLogDirPath();
        getTempDirPath();
        getPictureDirPath();
        clearTempDir();//清空临时文件夹
    }

    /** 清除临时文件夹中的所有文件 */
    public static void clearTempDir() {
        File[] files = new File(getTempDirPath()).listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }

    /** 获取默认的外部存储目录 */
    public static File getExternalStorageDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }

    /**
     * 获取APP根目录
     *
     * @return File("/mnt/storage0/Android/data/packagename") if the phone has SD card,else return File("data/data/packagename/cache")
     */
    public static File getAppRootDir() {
        File externalCacheDir = AndroidUtilsCore.getContext().getExternalCacheDir();
        if (null == getExternalStorageDirectory() || externalCacheDir == null) {
            return AndroidUtilsCore.getContext().getCacheDir();
        } else {
            File externalAppRootDir = externalCacheDir.getParentFile();
            if (!externalAppRootDir.exists()) {
                externalAppRootDir.mkdirs();
            }
            return externalAppRootDir;
        }
    }

    /**
     * 获取APP根目录路径
     *
     * @return path("/mnt/storage0/Android/data/packagename/") if the phone has SD card,else return path("data/data/packagename/")
     */
    public static String getAppRootDirPath() {
        String path = getAppRootDir().getAbsolutePath();
        if (!TextUtils.isEmpty(path) && !path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        return path;
    }

    /**
     * 获取APP缓存目录
     *
     * @return File("/mnt/storage0/Android/data/packagename/cache") if the phone has SD card,else return File("data/data/packagename/cache")
     */
    public static File getAppCacheDir() {
        File externalCacheDir = AndroidUtilsCore.getContext().getExternalCacheDir();
        if (null == getExternalStorageDirectory() || externalCacheDir == null) {
            return AndroidUtilsCore.getContext().getCacheDir();
        } else {
            if (!externalCacheDir.exists()) {
                externalCacheDir.mkdirs();
            }
            return externalCacheDir;
        }
    }

    /**
     * 获取APP缓存目录路径
     *
     * @return path("/mnt/storage0/Android/data/packagename/cache/") if the phone has SD card,else return path("data/data/packagename/cache/")
     */
    public static String getAppCacheDirPath() {
        String path = getAppCacheDir().getAbsolutePath();
        if (!TextUtils.isEmpty(path) && !path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        LogUtil.d(TAG, "getAppCacheDirPath = " + path);
        return path;
    }


    /**
     * 获取日志记录目录文件
     *
     * @return File("/mnt/storage0/packagename/.crashLog") if the phone has SD card,else return File("data/data/packagename/cache/.crashLog")
     */
    public static File getLogDir() {
        File dir = new File(getAppRootDir(), PATH_BASE_LOG);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        LogUtil.d(TAG, "getLogDir = " + dir.getAbsolutePath());
        return dir;
    }

    /**
     * 获取日志记录目录文件
     *
     * @return File("/mnt/storage0/packagename/.crashLog/") if the phone has SD card,else return File("data/data/packagename/cache/.crashLog/")
     */
    public static String getLogDirPath() {
        String path = getLogDir().getAbsolutePath();
        if (!TextUtils.isEmpty(path) && !path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        LogUtil.d(TAG, "getLogDirPath = " + path);
        return path;
    }

    /**
     * 获取临时目录文件
     *
     * @return File("/mnt/storage0/packagename/temp") if the phone has SD card,else return File("data/data/packagename/cache/temp")
     */
    public static File getTempDir() {
        File dir = new File(getAppRootDir(), PATH_BASE_TEMP);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        LogUtil.d(TAG, "getTempDir = " + dir.getAbsolutePath());
        return dir;
    }

    /**
     * 获取临时文件夹路径
     *
     * @return path("/mnt/storage0/packagename/temp/") if the phone has SD card,else return path("data/data/packagename/cache/temp/")
     */
    public static String getTempDirPath() {
        String path = getTempDir().getAbsolutePath();
        if (!TextUtils.isEmpty(path) && !path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        LogUtil.d(TAG, "getTempDirPath = " + path);
        return path;
    }


    /**
     * 获取图片目录
     *
     * @return File("/mnt/storage0/packagename/picture") if the phone has SD card,else return File("data/data/packagename/cache/picture")
     */
    public static File getPictureDir() {
        File dir = new File(getAppRootDir(), PATH_BASE_PICTURE);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        LogUtil.d(TAG, "getPictureDir = " + dir.getAbsolutePath());
        return dir;
    }

    /**
     * 获取 图片目录路径
     *
     * @return path("/mnt/storage0/packagename/picture/") if the phone has SD card,else return path("data/data/packagename/cache/picture/")
     */
    public static String getPictureDirPath() {
        String path = getPictureDir().getAbsolutePath();
        if (!TextUtils.isEmpty(path) && !path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        LogUtil.d(TAG, "getPictureDirPath = " + path);
        return path;
    }


    /**
     * 获取应用根目录下的文件(eg : "/mnt/storage0/packagename/abc.txt)
     *
     * @param fileName 需要获取的文件名称 eg:abc.txt
     * @return 存在则返回该文件，否则返回null
     */
    public static File getFileInRootDir(final String fileName) {
        String filePath = getAppRootDirPath() + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * 获取临时目录下的文件(eg : "/mnt/storage0/packagename/temp/abc.txt)
     *
     * @param fileName 需要获取的文件名称 eg:abc.txt
     * @return 存在则返回该文件，否则返回null
     */
    public static File getFileInTempDir(final String fileName) {
        String filePath = getTempDirPath() + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * 获取图片目录下的文件(eg : "/mnt/storage0/packagename/picture/abc.txt)
     *
     * @param fileName 需要获取的文件名称 eg:abc.txt
     * @return 存在则返回该文件，否则返回null
     */
    public static File getFileInPictureDir(final String fileName) {
        String filePath = getTempDirPath() + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    /////////////////////////////////////////////File 操作/////////////////////////////////////////

    /**
     * 获取以/分割的路径文件名
     *
     * @param path eg: content://storage/data/abc.txt
     * @return 文件名 eg: abc.txt
     */
    public static String getFileNameOnUrl(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 将asset资产目录下的文件拷贝到内存中
     *
     * @param targetDir     拷贝目标目录
     * @param assetFileName 拷贝文件名
     * @param force         是否强制拷贝,true : 忽略文件是否存在，false : 存在则不拷贝
     */
    public static File copyFileFromAsset(String targetDir, String assetFileName, boolean force) {
        File file = new File(targetDir, assetFileName);
        if (file.exists() && file.length() > 0) {
            if (force) {
                file.delete();
            } else {
                return file;
            }
        }
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            InputStream is = AndroidUtilsCore.getContext().getAssets().open(assetFileName);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 10];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将文件拷贝到指定目录
     *
     * @param targetDir  拷贝目标目录
     * @param sourceFile 拷贝文件名
     * @param force      是否强制拷贝,true : 忽略文件是否存在，false : 存在则不拷贝
     */
    public static boolean copyFile(String targetDir, File sourceFile, boolean force) {
        if (targetDir == null || targetDir.length() == 0) {
            return false;
        }
        if (sourceFile == null || !sourceFile.isFile()) {
            return false;
        }

        File targetFile = new File(targetDir, sourceFile.getName());
        if (targetFile.exists() && targetFile.length() > 0) {
            if (force) {
                targetFile.delete();
            } else {
                return true;
            }
        }
        try {
            InputStream is = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024 * 10];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除指定路径的文件
     *
     * @param filePath 文件路径
     */
    public static boolean deleteFile(String filePath) {
        boolean result = false;
        File file = new File(filePath);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    /**
     * 删除目录下所有文件包括子目录
     */
    public static boolean deleteDirsAndFile(File dir) {
        if (dir == null) return true;
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return true;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDirsAndFile(file);
            }
            file.delete();
        }
        return true;
    }

    /**
     * 计算文件夹目录下所有文件的大小
     */
    public static long sizeFiles(File dir) {
        long result = 0;
        if (dir == null) return result;
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return result;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                result += sizeFiles(file);
            }
            result += file.length();
        }
        return result;
    }

    /**
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param filePath 文件路径
     * @param encoding 写文件编码
     */
    public static String readFileByLines(String filePath, String encoding) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 读取指定目录文件的文件内容
     *
     * @param filePath 包含文件名称的文件路径
     * @return 文件内容 or null
     */
    public static String readFile(String filePath) {
        byte[] data = null;
        FileInputStream inStream = null;
        ByteArrayOutputStream outStream = null;
        try {
            inStream = new FileInputStream(filePath);
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 10];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != outStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (null != data) {
            return new String(data);
        } else {
            return null;
        }
    }

    /**
     * 读取assets目录的文件内容
     *
     * @param fileName 文件名称，包含扩展名
     */
    public static String readValueInAssets(String fileName) {
        String result = null;
        InputStream inputStream = null;
        try {
            inputStream = AndroidUtilsCore.getContext().getResources().getAssets().open(fileName);
            int len = inputStream.available();
            byte[] buffer = new byte[len];
            inputStream.read(buffer);
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 读取raw目录的文件内容
     *
     * @param rawFileResId raw文件名资源id
     */
    public static String readValueInRaw(int rawFileResId) {
        String result = null;
        try {
            InputStream is = AndroidUtilsCore.getContext().getResources().openRawResource(rawFileResId);
            int len = is.available();
            byte[] buffer = new byte[len];
            is.read(buffer);
            result = new String(buffer, "UTF-8");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 指定编码保存内容
     *
     * @param filePath 文件路径
     * @param content  保存的内容
     */
    public static void writeStringToFile(String filePath, String content) {
        writeStringToFile(filePath, content, "UTF-8");
    }

    /**
     * 指定编码保存内容
     *
     * @param filePath 文件路径
     * @param content  保存的内容
     * @param encoding 写文件编码
     */
    public static void writeStringToFile(String filePath, String content, String encoding) {
        BufferedWriter writer = null;
        File file = new File(filePath);
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), encoding));
                writer.write(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void appendStringToFile(String content, File file) {
        appendStringToFile(content, file, "UTF-8");
    }

    /**
     * 向文件中追加文本
     *
     * @param content  需要追加的内容
     * @param file     待追加文件源
     * @param encoding 文件编码
     */
    public static void appendStringToFile(String content, File file, String encoding) {
        BufferedWriter writer = null;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), encoding));
                writer.write(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 规范file地址为Uri格式(追加file://)
     *
     * @param path 文件路径 eg: /sdcard/0/aa.jpg;
     * @return uri文件路径 eg；file:///sdcard/0/aa.jpg.
     */
    public static String formatFileUri(String path) {
        if (!TextUtils.isEmpty(path) && !path.startsWith("file://")) {
            path = "file://" + path;
        }
        return path;
    }

    /**
     * 规范file地址为Uri格式(去掉file://)
     *
     * @return uri文件路径 eg；file:///sdcard/0/aa.jpg.
     */
    public static String formatFileUri2Normal(String uri) {
        if (!TextUtils.isEmpty(uri) && uri.startsWith("file://")) {
            uri.replace("file://", "");
        }
        return uri;
    }

    /**
     * 将输入流写入指定路径的文件
     *
     * @param inputStream 输入流
     * @param filePath    文件路径
     * @return 写好的文件
     */
    public static File writeInputStream(InputStream inputStream, String filePath) {
        OutputStream outputStream = null;
        // 在指定目录创建一个空文件并获取文件对象
        File mFile = new File(filePath);
        if (!mFile.getParentFile().exists())
            mFile.getParentFile().mkdirs();
        try {
            outputStream = new FileOutputStream(mFile);
            byte buffer[] = new byte[4 * 1024];
            int lenght;
            while ((lenght = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, lenght);
            }
            outputStream.flush();
        } catch (IOException e) {
            LogUtil.e(TAG, "writeInputStram fail, cause ：" + e.getMessage());
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException e) {
                LogUtil.e(TAG, "IOException ：" + e.getMessage());
            }
        }
        return mFile;
    }
}
