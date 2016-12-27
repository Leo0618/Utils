package com.leo618.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * function : 图片处理工具类.
 */

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "WeakerAccess"})
public class ImageUtil {
    private static final String TAG = "ImageUtil";

    /**
     * 旋转图片
     *
     * @param bitmap 要旋转的图片
     * @param degree 旋转的角度
     * @return 这是旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 压缩图片(质量压缩)
     *
     * @param image 被压缩的图片
     * @return 压缩后的图片
     */
    public static Bitmap compressImage(Bitmap image) {
        return compressImage(image, 300);
    }

    /**
     * 压缩图片(质量压缩)
     *
     * @param image      被压缩的图片
     * @param targetSize 压缩到的目标大小
     * @return 压缩后的图片
     */
    public static Bitmap compressImage(Bitmap image, long targetSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > targetSize) {    //循环判断如果压缩后图片是否大于300kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    /** 获得圆形图片 */
    public static Bitmap circleBitmap(Bitmap bitmapimg) {
        return circleBitmap(bitmapimg, 0xff424242);
    }

    /** 获得圆形图片 */
    public static Bitmap circleBitmap(Bitmap bitmapimg, int color) {
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(), bitmapimg.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmapimg.getWidth(), bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 2, bitmapimg.getHeight() / 2, bitmapimg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

    /** 获得圆角图片 */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        return getRoundedCornerBitmap(bitmap, roundPx, 0xff424242);
    }

    /** 获得圆角图片 */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx, int color) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /** 获得带倒影的图片方法 */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 获取图片
     */
    public static Bitmap getBitmap(String path) {
        return BitmapFactory.decodeFile(path);
    }

    /**
     * 保存图片成JPEG格式
     */
    public static boolean saveBitmap(String path, Bitmap bmp) {
        return saveBitmap(new File(path), bmp);
    }

    /** 保存图片为文件 */
    public static boolean saveBitmap(File file, Bitmap bmp) {
        try {
            if (!file.exists()) {
                File p = file.getParentFile();
                if (p != null) {
                    p.mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            // 100 means no compression
            boolean isCompress = bmp.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            AndroidUtilsCore.getContext().getContentResolver().notifyChange(Uri.fromFile(file), null);
            return isCompress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 计算图片的inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    /**
     * 将指定资源id的图片转换成指定比例宽高的图片
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 将指定资源路径的图片转换成指定比例宽高的图片
     */
    public static Bitmap decodeSampledBitmapFromFilePath(Context context, String path, int reqWidth, int reqHeight) {
        if (reqWidth == 0 && reqHeight == 0) {
            return null;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        computeSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 将指定资源Uri的图片转换成指定比例宽高的图片
     */
    public static Bitmap decodeSampleBitmapFromUri(Uri uri, int reqWidth, int reqHeight) throws IOException {
        if (reqWidth == 0 && reqHeight == 0) {
            return null;
        }

        InputStream input = AndroidUtilsCore.getContext().getContentResolver().openInputStream(uri);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(input, null, options);
        if (input != null) {
            input.close();
        }

        computeSize(options, reqWidth, reqHeight);

        input = AndroidUtilsCore.getContext().getContentResolver().openInputStream(uri);
        options.inJustDecodeBounds = false;
        Bitmap sampleBimap = BitmapFactory.decodeStream(input, null, options);
        if (input != null) {
            input.close();
        }
        return sampleBimap;
    }

    /**
     * 计算大小
     */
    private static void computeSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int newWidth = reqWidth;
        int newHeight = reqHeight;
        if (reqWidth != reqHeight) {
            final int bitmapHeight = options.outHeight;
            final int bitmapWidth = options.outWidth;

            if (reqHeight == 0) {
                float scaleWidht = ((float) reqWidth / bitmapWidth);
                newHeight = (int) (bitmapHeight * scaleWidht);
            }

            if (reqWidth == 0) {
                float scaleHeight = ((float) reqHeight / bitmapHeight);
                newWidth = (int) (bitmapWidth * scaleHeight);
            }
        }
        options.inSampleSize = calculateInSampleSize(options, newWidth, newHeight);
        LogUtil.i(TAG, "原宽高:" + options.outWidth + "*" + options.outHeight
                + " 压缩比例:" + options.inSampleSize
                + " 压缩后宽高:" + newWidth + "*" + newHeight);
    }

    /**
     * 重新设置图片的宽高
     */
    public static Bitmap resizeBitmap(Uri uri, int reqWidth, int reqHeight) {
        if (reqWidth == 0 && reqHeight == 0) {
            return null;
        }

        Bitmap bmp;
        try {
            bmp = decodeSampleBitmapFromUri(uri, reqWidth, reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        int bitmapWidth = 0;
        if (bmp != null) {
            bitmapWidth = bmp.getWidth();
        }
        int bitmapHeight = 0;
        if (bmp != null) {
            bitmapHeight = bmp.getHeight();
        }

        float scaleWidth;
        float scaleHeight;

        scaleWidth = ((float) reqWidth) / bitmapWidth;
        scaleHeight = ((float) reqHeight) / bitmapHeight;

        if (reqHeight == 0) {
            reqHeight = (int) (bitmapHeight * scaleWidth);
            scaleHeight = ((float) reqHeight) / bitmapHeight;
        }

        if (reqWidth == 0) {
            reqWidth = (int) (bitmapWidth * scaleHeight);
            scaleWidth = ((float) reqWidth) / bitmapWidth;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizeBmp = null;
        if (bmp != null) {
            resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
            bmp.recycle();
        }
        if (resizeBmp != null) {
            LogUtil.d(TAG, "原大小:" + bitmapWidth + "*" + bitmapHeight + " 改变大小后宽高:" + resizeBmp.getWidth() + "*" + resizeBmp.getHeight());
        }
        return resizeBmp;
    }

    /**
     * 获取ImageView的图片原大小
     *
     * @return int[0] = height, int[1] = width
     */
    public static int[] getImageViewBitmapHeightAndWidth(ImageView imageView) {
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bd.getBitmap();
        final int height = bitmap.getHeight();
        final int width = bitmap.getWidth();

        return new int[]{height, width};
    }

    /**
     * 旋转图片一定角度 rotaingImageView
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 旋转图片 指定的角度
     */
    public static Bitmap rotate(Bitmap b, int degrees) {
        return rotateAndMirror(b, degrees, false);
    }

    /**
     * 旋转图片 指定的角度
     */
    public static Bitmap rotateAndMirror(Bitmap b, int degrees, boolean mirror) {
        if ((degrees != 0 || mirror) && b != null) {
            Matrix m = new Matrix();
            // Mirror first. horizontal flip + rotation = -rotation + horizontal flip
            if (mirror) {
                m.postScale(-1, 1);
                degrees = (degrees + 360) % 360;
                if (degrees == 0 || degrees == 180) {
                    m.postTranslate(b.getWidth(), 0);
                } else if (degrees == 90 || degrees == 270) {
                    m.postTranslate(b.getHeight(), 0);
                } else {
                    throw new IllegalArgumentException("Invalid degrees=" + degrees);
                }
            }
            if (degrees != 0) {
                // clockwise
                m.postRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            }
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     * videoThumbnail.setImageBitmap(getVideoThumbnail(videoPath, 100, 100,
     * MediaStore.Images.Thumbnails.MICRO_KIND));
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap;
        // 获取视频的缩略图  
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 获取指定路径下的图片的指定大小的缩略图 getImageThumbnail
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 保存图片到指定目录下
     */
    public static void saveBitmap(String dirpath, String filename, Bitmap bitmap, boolean isDelete) {
        File dir = new File(dirpath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dirpath, filename);
        if (isDelete) {
            if (file.exists()) {
                file.delete();
            }
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(CompressFormat.PNG, 100, out)) {
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;

    }

    /**
     * 图片合成
     *
     * @param circleBmp 边框图片
     * @param targetBmp 内容图片
     * @return 合成后的图片
     */
    public static Bitmap compositeBitMap(Bitmap circleBmp, Bitmap targetBmp) {
        int circleBmpWidth = circleBmp.getWidth();
        int circleBmpHeight = circleBmp.getHeight();
        int targetBmpWidth = targetBmp.getWidth();
        int targetBmpHeight = targetBmp.getHeight();
        // 创建一张空白边框大小的图
        Bitmap drawBitmap = Bitmap.createBitmap(circleBmpWidth, circleBmpHeight, circleBmp.getConfig());
        Canvas canvas = new Canvas(drawBitmap);
        Paint paint = new Paint();
        // 画边框
        canvas.drawBitmap(circleBmp, 0, 0, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.LIGHTEN));
        // 对边框进行缩放
        float scaleX = circleBmp.getWidth() * 1F / targetBmpWidth;
        float scaleY = (circleBmp.getHeight() - 30) * 1F / targetBmpHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY); // 缩放图片
        Bitmap copyBitmap = Bitmap.createBitmap(targetBmp, 0, 0, targetBmpWidth, targetBmpHeight, matrix, true);
        canvas.drawBitmap(copyBitmap, 0, 0, paint);
        return drawBitmap;
    }

    /**
     * drawable转换成Bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * drawable转换成Bitmap
     */
    public static Drawable bitmap2Drawable(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        Resources resources = AndroidUtilsCore.getContext().getResources();
        BitmapDrawable bd = new BitmapDrawable(resources, bm);
        bd.setTargetDensity(bm.getDensity());
        return new BitmapDrawable(resources, bm);
    }

    /**
     * 压缩图片 (比例压缩)
     */
    public static String compressImage(String path, String newPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int inSampleSize = 1;
        int maxSize = 3000;
        if (options.outWidth > maxSize || options.outHeight > maxSize) {
            int widthScale = (int) Math.ceil(options.outWidth * 1.0 / maxSize);
            int heightScale = (int) Math.ceil(options.outHeight * 1.0 / maxSize);
            inSampleSize = Math.max(widthScale, heightScale);
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int newW = w;
        int newH = h;
        if (w > maxSize || h > maxSize) {
            if (w > h) {
                newW = maxSize;
                newH = (int) (newW * h * 1.0 / w);
            } else {
                newH = maxSize;
                newW = (int) (newH * w * 1.0 / h);
            }
        }
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, newW, newH, false);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(newPath);
            newBitmap.compress(CompressFormat.JPEG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        recycle(newBitmap);
        recycle(bitmap);
        return newPath;
    }

    /**
     * 回收图片
     */
    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        System.gc();
    }

    /**
     * 以JPEG格式保存Bitmap到本地文件
     *
     * @param filePath 文件路径+文件名
     * @param bitmap   文件内容
     */
    public static void saveAsJPEG(Bitmap bitmap, String filePath) {
        FileOutputStream fos = null;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            LogUtil.e(TAG, "Exception ：" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LogUtil.e(TAG, "IOException ：" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 以PNG格式保存Bitmap到本地文件
     *
     * @param filePath 文件路径+文件名
     * @param bitmap   文件内容
     */
    public static void saveAsPNG(Bitmap bitmap, String filePath) {
        FileOutputStream fos = null;

        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                fos = new FileOutputStream(file);
                bitmap.compress(CompressFormat.PNG, 100, fos);
                fos.flush();
            } catch (Exception e) {
                LogUtil.e(TAG, "Exception ：" + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LogUtil.e(TAG, "IOException ：" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 图片转换成base64
     *
     * @param imgPath 图片路径
     * @return base64字符串
     */
    public static String imgToBase64(String imgPath) {
        Bitmap bitmap = null;
        if (imgPath != null && imgPath.length() > 0) {
            bitmap = getBitmap(imgPath);
        }
        if (bitmap == null) {
            return "";
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置TextView的左右上下图片
     *
     * @param targetView TextView
     * @param left       左边图片
     * @param top        顶边图片
     * @param right      右边图片
     * @param bottom     底边图片
     */
    public static void setTextViewCompoundDrawables(TextView targetView, @Nullable Drawable left, @Nullable Drawable top,
                                                    @Nullable Drawable right, @Nullable Drawable bottom) {
        setTextViewCompoundDrawables(targetView, left, top, right, bottom, -1);
    }

    /**
     * 设置TextView的左右上下图片
     *
     * @param targetView              TextView
     * @param left                    左边图片
     * @param top                     顶边图片
     * @param right                   右边图片
     * @param bottom                  底边图片
     * @param compoundDrawablePadding 图片与文字之间间距
     */
    public static void setTextViewCompoundDrawables(TextView targetView, @Nullable Drawable left, @Nullable Drawable top,
                                                    @Nullable Drawable right, @Nullable Drawable bottom, int compoundDrawablePadding) {
        if (targetView == null) return;
        if (left != null) {
            left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
        }
        if (right != null) {
            right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getMinimumWidth(), bottom.getMinimumHeight());
        }
        if (compoundDrawablePadding > 0) {
            targetView.setCompoundDrawablePadding(compoundDrawablePadding);
        }
        targetView.setCompoundDrawables(left, top, right, bottom);
    }
}
