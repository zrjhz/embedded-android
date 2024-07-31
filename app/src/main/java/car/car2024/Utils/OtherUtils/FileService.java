package car.car2024.Utils.OtherUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * 文件操作工具类
 * 进行图片的存储或者的文件的存储
 *
 * @author hdy
 */
public class FileService {
    private static final String saveFileDirectory = "2024car";

    /**
     * 保存文件
     *
     * @param fileName 文件名称
     * @param content  文件内容
     * @throws IOException
     */
    public static void saveToSDCard(String fileName, String content)
            throws IOException {
        // 考虑不同版本的sdCard目录不同，采用系统提供的API获取SD卡的目录
        File file = new File(Environment.getExternalStorageDirectory() + "/"
                + saveFileDirectory, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.isDirectory()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(content.getBytes());
        fileOutputStream.close();
    }

    /**
     * 读取文件内容
     *
     * @param fileName 文件名称
     * @return 文件内容
     * @throws IOException
     */
    public static String read(String fileName) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory() + "/"
                + saveFileDirectory, fileName);
        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            // 把每次读取的内容写入到内存中，然后从内存中获取
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            // 只要没读完，不断的读取
            while ((len = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            // 得到内存中写入的所有数据
            byte[] data = outputStream.toByteArray();
            fileInputStream.close();
            return new String(data);
        } else
            return "";
    }

    /**
     * 分类保存图片  每个类一个文件夹
     *
     * @param bitmap
     * @param strFileName
     * @param className
     * @return
     */
    public static File saveClassPhoto(Bitmap bitmap, String strFileName, String className) {
        String path = "/" + saveFileDirectory + "/" + className;
        return savePhotoRealize(bitmap, path, strFileName + ".png");
    }

    /**
     * 保存图片
     *
     * @param bitmap      图片资源
     * @param strFileName 图片名称
     * @throws IOException
     */
    public static File savePhoto(Bitmap bitmap, String strFileName) {
        String path = "/" + saveFileDirectory;
        return savePhotoRealize(bitmap, path, strFileName);
    }

    private static File savePhotoRealize(Bitmap bitmap, String path, String strFileName) {
        if (bitmap == null || strFileName == null) {
            Log.i("小车", "bitmap是空指针!");
            return null;
        }
        try {
            File file = new File(Environment.getExternalStorageDirectory() + path, strFileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.isDirectory()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
                fos.flush();
                fos.close();
            }
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void saveBitmap(Bitmap bitmap, String name) {
        FileService.savePhoto(bitmap, name + ".png");
        System.out.println(name + "  保存成功·····");
    }

    /**
     * 读取图片
     *
     * @param strFileName 图片名称
     * @return 图片内容
     * @throws IOException
     */
    @SuppressWarnings("unused")
    public static Bitmap readPhoto(String strFileName) {
        String path = Environment.getExternalStorageDirectory() + "/"
                + saveFileDirectory + "/" + strFileName;
        if (path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        } else
            return null;

    }
}
