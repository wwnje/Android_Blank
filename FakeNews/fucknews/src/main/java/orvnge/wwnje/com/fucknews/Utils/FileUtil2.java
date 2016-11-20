package orvnge.wwnje.com.fucknews.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by wwnje on 2016/5/22.
 */
public class FileUtil2 {
        /**
         * 如果文件不存在，就创建文件
         *
         * @param path 文件路径
         * @return
         */
        public static String createIfNotExist(String path) {
            File file = new File(path);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            return path;
        }

        /**
         * 向文件中写入数据
         *
         * @param filePath
         *            目标文件全路径
         * @param data
         *            要写入的数据
         * @return 0表示成功写入，1表示没找到文件，2表示IO异常
         */
        public static boolean writeBytes(String filePath, byte[] data) {
            try {
                FileOutputStream fos = new FileOutputStream(filePath);
                fos.write(data);
                fos.close();
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return false;
        }

        /**
         * 从文件中读取数据
         *
         * @param file
         * @return
         */
        public static byte[] readBytes(String file) {
            try {
                FileInputStream fis = new FileInputStream(file);
                int len = fis.available();
                byte[] buffer = new byte[len];
                fis.read(buffer);
                fis.close();
                return buffer;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return null;

        }

        /**
         * 向文件中写入字符串String类型的内容
         *
         * @param file
         *            文件路径
         * @param content
         *            文件内容
         * @param charset
         *            写入时候所使用的字符集
         */
        public static void writeString(String file, String content, String charset) {
            try {
                byte[] data = content.getBytes(charset);
                writeBytes(file, data);
                Log.d("Log", "success" + file);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        /**
         * 从文件中读取数据，返回类型是字符串String类型
         *
         * @param file
         *            文件路径
         * @param charset
         *            读取文件时使用的字符集，如utf-8、GBK等
         * @return
         */
        public static String readString(String file, String charset) {
            byte[] data = readBytes(file);
            String ret = null;

            try {
                ret = new String(data, charset);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return ret;
        }

    }

