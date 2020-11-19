package com.cecilleo.core.base.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.widget.Toast;
import java.io.File;
import java.util.Objects;

public class FileUtil {

  /**
   * 最小可用空间(MB)
   */
  private static final int MIN_FREE_SPACE = 100;
  /**
   * 默认下载文件地址.
   */
  private static final String DOWNLOAD_ROOT_DIR = "CGLiveFile";
  /**
   * 默认下载图片文件地址.
   */
  private static final String DOWNLOAD_IMAGE_DIR = "images";
  /**
   * 默认下载文件地址.
   */
  private static final String DOWNLOAD_FILE_DIR = "download";

  /**
   * APP缓存目录.
   */
  private static final String CACHE_DIR = "cache";

  private static String downloadRootDir = null;
  private static String imageDownloadDir = null;
  private static String fileDownloadDir = null;
  private static String cacheDownloadDir = null;

  private FileUtil() {
    throw new UnsupportedOperationException("StrUtil cannot be instantiated");
  }

  /**
   * SD卡是否能用.
   *
   * @return true 可用,false不可用
   */
  public static boolean isSDCardAvailable() {
    try {
      return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 计算手机上的剩余空间,优先选取SD卡.
   *
   * @return the int
   */
  public static int getFreeSpace() {
    double sdFreeMB;
    StatFs stat;
    //if (isSDCardAvailable()) {
    //  stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
    //} else {
    //  stat = new StatFs(Environment.getDataDirectory().getPath());
    //}
    stat = new StatFs(
        Objects.requireNonNull(AppUtil.Companion.getAppContext().getExternalCacheDir()).getPath());

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
      sdFreeMB = stat.getAvailableBytes() / (1024 * 1024);
    } else {
      //noinspection deprecation
      sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / 1024 / 1024;
    }

    return (int) sdFreeMB;
  }

  /**
   * Gets the download root dir.
   *
   * @param context the context
   * @return the download root dir
   */
  public static String getDownloadRootDir(Context context) {
    if (downloadRootDir == null) {
      initFileDir(context);
    }
    return downloadRootDir;
  }

  /**
   * Gets the image download dir.
   *
   * @param context the context
   * @return the image download dir
   */
  public static String getImageDownloadDir(Context context) {
    if (downloadRootDir == null) {
      initFileDir(context);
    }
    return imageDownloadDir;
  }

  /**
   * Gets the file download dir.
   *
   * @param context the context
   * @return the file download dir
   */
  public static String getFileDownloadDir(Context context) {
    if (downloadRootDir == null) {
      initFileDir(context);
    }
    return fileDownloadDir;
  }

  /**
   * Gets the cache download dir.
   *
   * @param context the context
   * @return the cache download dir
   */
  public static String getCacheDownloadDir(Context context) {
    if (downloadRootDir == null) {
      initFileDir(context);
    }
    return cacheDownloadDir;
  }

  /**
   * 删除所有缓存文件.
   *
   * @return true, if successful
   */
  public static boolean clearDownloadFile() {
    try {
      File fileDirectory = new File(downloadRootDir);
      deleteFile(fileDirectory);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static boolean deleteDir(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }
    return dir.delete();
  }

  /**
   * 删除文件.
   *
   * @return true, if successful
   */
  public static boolean deleteFile(File file) {
    try {
      if (file == null) return true;
      if (file.isDirectory()) {
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
          deleteFile(files[i]);
        }
      } else {
        file.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private static void initFileDir(Context context) {

    //默认下载文件根目录.
    String fileRootPath = File.separator + DOWNLOAD_ROOT_DIR + File.separator;

    //默认下载图片文件目录.
    String imageDownloadPath = fileRootPath + DOWNLOAD_IMAGE_DIR + File.separator;

    //默认下载文件目录.
    String fileDownloadPath = fileRootPath + DOWNLOAD_FILE_DIR + File.separator;

    //默认缓存目录.
    String cacheDownloadPath = fileRootPath + CACHE_DIR + File.separator;

    try {
      if (getFreeSpace() < MIN_FREE_SPACE) {
        Toast.makeText(context.getApplicationContext(), "Device Storage Is Not Enough...",
            Toast.LENGTH_SHORT).show();
        return;
      }

      File root;
      //if (isSDCardAvailable()) {
      //  root = Environment.getExternalStorageDirectory();
      //} else {
      //  root = Environment.getDataDirectory();
      //}
      root = AppUtil.Companion.getAppContext().getExternalCacheDir();

      File downloadDir = new File(Objects.requireNonNull(root).getAbsolutePath() + fileRootPath);
      if (!downloadDir.exists()) {
        downloadDir.mkdirs();
      }
      downloadRootDir = downloadDir.getPath();

      File cacheDownloadDirFile = new File(root.getAbsolutePath() + cacheDownloadPath);
      if (!cacheDownloadDirFile.exists()) {
        cacheDownloadDirFile.mkdirs();
      }
      cacheDownloadDir = cacheDownloadDirFile.getPath();

      File imageDownloadDirFile = new File(root.getAbsolutePath() + imageDownloadPath);
      if (!imageDownloadDirFile.exists()) {
        imageDownloadDirFile.mkdirs();
      }
      imageDownloadDir = imageDownloadDirFile.getPath();

      File fileDownloadDirFile = new File(root.getAbsolutePath() + fileDownloadPath);
      if (!fileDownloadDirFile.exists()) {
        fileDownloadDirFile.mkdirs();
      }
      fileDownloadDir = fileDownloadDirFile.getPath();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 本地是否完整存在该文件
   */
  public static boolean isFileFullExist(Context context, String fileName, long downloadSize) {
    File downloadFile = new File(getFileDownloadDir(context), fileName);
    if (!downloadFile.exists()) {
      return false;
    } else {
      return downloadFile.length() == downloadSize;
    }
  }

  public static boolean isCacheExist(String url) {
    String fileName = String.valueOf(url.hashCode());
    File file = new File(FileUtil.getCacheDownloadDir(AppUtil.Companion.getAppContext()), fileName);
    return file.exists();
  }
}
