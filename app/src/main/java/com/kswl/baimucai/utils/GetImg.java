package com.kswl.baimucai.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 获取照片
 *
 * @author Michael.Zhang 2013-07-05 16:45:21
 */
public class GetImg {

    public static final int go_to_camera_code = 1;
    public static final int go_to_cutimg_code = 2;
    public static final int go_to_gallery_code = 3;

    private Activity mActivity = null;
    public AsyncTask<Intent, String, String> saveImgTask = null;

    /* 拍照所得相片路径 */
    public File file_save = null;
    /* 裁切照片存储路径 */
    public File file_cut = null;

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
            Locale.CHINA);

    public static final String directory = "/ggt";

    public GetImg(Activity activity, File fileSave, File fileCut) {
        mActivity = activity;

        file_save = fileSave;
        file_cut = fileCut;

        if (null == file_save) {
            file_save = setDefaultFile("img" + sdf.format(new Date()) + ".jpg");
        } else {
            file_save = new File(file_save.getPath() + "/img.jpg");
        }
        if (null == file_cut) {
            file_cut = setDefaultFile("cut_img.png");
        } else {
            file_cut = new File(fileCut.getPath() + "/cut_img.png");
        }
    }

    public GetImg(Activity activity) {
        this(activity, null, null);
    }

    public static File setDefaultFile(String name) {
        if (!TextUtils.isEmpty(name)
                && Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File pathdir = new File(Environment.getExternalStorageDirectory()
                    + directory);
            if (!pathdir.exists()) {
                pathdir.mkdirs();
            }
            return new File(Environment.getExternalStorageDirectory()
                    + directory, name);
        }
        return null;
    }

    /**
     * 重置裁切图片存储位置
     *
     * @param file
     * @author windy 2014年6月27日 上午11:43:36
     */
    public void resetCutFile(File file) {
        file_cut = file;
    }

    /**
     * 重置裁切图片存储位置
     *
     * @param name
     * @author windy 2014年6月27日 上午11:44:33
     */
    public void resetCutFile(String name) {
        file_cut = setDefaultFile(name);
    }

    /**
     * 相册
     *
     * @author Michael.Zhang 2013-06-20 17:06:04
     */
    public void goToGallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            mActivity.startActivityForResult(intent, go_to_gallery_code);
        } catch (Exception e) {
            // TODO: handle exception
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            mActivity.startActivityForResult(intent, go_to_gallery_code);
        }
    }

    /**
     * 相机
     *
     * @author Michael.Zhang 2013-06-20 16:54:47
     */
    public void goToCamera() {
        file_save = setDefaultFile("img" + sdf.format(new Date()) + ".jpg");
        if (null != file_save) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= 24) {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, file_save.getAbsolutePath());
                Uri uri = mActivity.getContentResolver().insert(MediaStore.Images.Media
                        .EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file_save));
            }
            mActivity.startActivityForResult(intent, go_to_camera_code);
        }
    }

    /**
     * 裁切图片，
     *
     * @param path 裁切图片途径
     * @author TangWei 2013-12-23下午5:11:46
     */
    public void gotoCutImage(String path) {
        gotoCutImage(path, 0, null, 0, 0, 0, 0);
    }

    /**
     * 裁切图片，跳转到CropImageActivity
     *
     * @param path     裁切图片途径
     * @param gridView 图片列表
     * @author windy 2014年6月27日 上午11:01:47
     */
    public void gotoCutImage(String path, View gridView) {

        int aspectX = 0, aspectY = 0, outputX = 0, outputY = 0;
        if (null != gridView) {
            aspectX = gridView.getWidth();
            aspectY = gridView.getHeight();
            outputX = gridView.getWidth();
            outputY = gridView.getHeight();
        }

        gotoCutImage(path, 0, null, aspectX, aspectY, outputX, outputY);
    }

    /**
     * 裁切图片，跳转到CropImageActivity，path/resouceId/bitmap 任选一
     *
     * @param path      图片对应的本地路径
     * @param resouceId 图片资源ID
     * @param bitmap    图片Bitmap
     * @param aspectX   裁切框比例，宽
     * @param aspectY   裁切框比例，高
     * @param outputX   裁切图片保存像素，宽
     * @param outputY   裁切图片保存像素，高
     * @author windy 2014年6月27日 上午11:17:15
     */
    public void gotoCutImage(String path, int resouceId, Bitmap bitmap,
                             int aspectX, int aspectY, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri uri = null;
        if (!TextUtils.isEmpty(path)) {
            uri = Uri.fromFile(new File(path));
        }
        if (resouceId > 0) {
            Bitmap mBitmap = BitmapFactory.decodeResource(
                    mActivity.getResources(), resouceId);
            uri = Uri.parse(MediaStore.Images.Media.insertImage(
                    mActivity.getContentResolver(), mBitmap, null, null));
        }
        if (null != bitmap) {
            uri = Uri.parse(MediaStore.Images.Media.insertImage(
                    mActivity.getContentResolver(), bitmap, null, null));
        }
        if (aspectX > 0) {
            intent.putExtra("aspectX", aspectX);
        }
        if (aspectY > 0) {
            intent.putExtra("aspectY", aspectY);
        }
        if (outputX > 0) {
            intent.putExtra("outputX", outputX);
        }
        if (outputY > 0) {
            intent.putExtra("outputY", outputY);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("return-data", true);
        mActivity.startActivityForResult(intent, go_to_cutimg_code);
    }

    public void gotoCutImage(Uri uri, int aspectX, int aspectY, int outputX,
                             int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        if (aspectX > 0) {
            // aspectX aspectY 是宽高的比例,
            // 输出是X方向的比例
            intent.putExtra("aspectX", aspectX);
        }
        if (aspectY > 0) {
            intent.putExtra("aspectY", aspectY);
        }
        if (outputX > 0) {
            // outputX outputY 是裁剪图片宽高，切忌不要大于500否则会卡死
            // 输出X方向的像素
            intent.putExtra("outputX", outputX);
        }
        if (outputY > 0) {
            intent.putExtra("outputY", outputY);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        /*
         * 设置为不返回数据 在很多博客中都把“return-data”设置为了true然后在onActivityResult中
		 * 通过data.getParcelableExtra("data")来获取数据，不过这样的话dp这个
		 * 变量的值就不能太大了，不然你的程序就挂了
		 */
        intent.putExtra("return-data", false);
        mActivity.startActivityForResult(intent, go_to_cutimg_code);
    }

    /**
     * @param uri
     * @return Bitmap
     * @Description: TODO(这里用一句话描述这个方法的作用)通过uri获取bitmap
     * @author wangjie
     * @date 2015-6-30 下午3:35:28
     */
    public Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    mActivity.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取 从相册选择的照片路径
     *
     * @param data
     * @author Michael.Zhang 2013-07-05 23:30:56
     */
    @SuppressWarnings("deprecation")
    public String getGalleryPath(Intent data) {
        String filePath = "";
        Uri mImageCaptureUri = data.getData();
        if (mImageCaptureUri != null) {
            String[] proj = {MediaStore.Images.Media.DATA};
            // 好像是android多媒体数据库的封装接口，具体的看Android文档
            Cursor cursor = mActivity.managedQuery(mImageCaptureUri, proj,
                    null, null, null);
            if (null != cursor) {
                // 按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                // 最后根据索引值获取图片路径www.2cto.com
                filePath = cursor.getString(column_index);
            } else {
                filePath = mImageCaptureUri.getPath();
            }
        }
        if ("".equals(filePath)
                && mImageCaptureUri.toString().contains("document")) {
            // String wholeID = getDocumentId(mImageCaptureUri);
            // String id = wholeID.split(":")[1];
            // String[] column = { MediaStore.Images.Media.DATA };
            // String sel = MediaStore.Images.Media._ID + "=?";
            // Cursor cursor = mActivity.getContentResolver().query(
            // MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
            // new String[] { id }, null);
            // int columnIndex = cursor.getColumnIndex(column[0]);
            // if (cursor.moveToFirst()) {
            // filePath = cursor.getString(columnIndex);
            // }
            // cursor.close();

            // ExternalStorageProvider
            if (isExternalStorageDocument(mImageCaptureUri)) {
                final String docId = getDocumentId(mImageCaptureUri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(mImageCaptureUri)) {
                final String id = getDocumentId(mImageCaptureUri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(mActivity, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(mImageCaptureUri)) {
                final String docId = getDocumentId(mImageCaptureUri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(mActivity, contentUri, selection,
                        selectionArgs);
            }

        }
        if ("".equals(filePath)) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = mActivity.getContentResolver().query(
                    mImageCaptureUri, projection, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(column_index);
        }
        return filePath;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    public static String getDocumentId(Uri documentUri) {
        final List<String> paths = documentUri.getPathSegments();
        if (paths.size() >= 2 && "document".equals(paths.get(0))) {
            return paths.get(1);
        }
        if (paths.size() >= 4 && "tree".equals(paths.get(0))
                && "document".equals(paths.get(2))) {
            return paths.get(3);
        }
        throw new IllegalArgumentException("Invalid URI: " + documentUri);
    }

    /**
     * 保存裁切后的图片
     */
    public void saveCutImg(final Intent data) {
        if (null != file_cut) {
            saveImgTask = new AsyncTask<Intent, String, String>() {
                @Override
                protected String doInBackground(Intent... params) {
                    // if (params.length > 0) {
                    try {
                        Bitmap photo = BitmapFactory.decodeFile(file_cut
                                .getAbsolutePath());
                        FileOutputStream out = new FileOutputStream(file_cut);
                        photo.compress(Bitmap.CompressFormat.JPEG, 35, out);
                        return file_cut.getAbsolutePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // }
                    return null;
                }
            };
            saveImgTask.execute(data);
        }
    }

    /**
     * @param filePath 图片本地路径
     * @return String
     * @Description: TODO(这里用一句话描述这个方法的作用)图片本地路径转Base64
     * @author wangjie
     * @date 2015-6-30 上午11:36:40
     */
    public static String getByteByPath(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);// 此时返回bm为空

        int w = options.outWidth;
        int h = options.outHeight;
        float wh = 1000f;// 这里设置高度为500f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > wh) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (w / wh);
        } else if (w < h && h > wh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (h / wh);
        }
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;// 设置缩放比例

        options.inJustDecodeBounds = false;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(filePath, options);
        bitmap = reviewPicRotate(bitmap, filePath);
        return getByteByBitmap(bitmap);
    }

    /**
     * @param uriString
     * @param mContext
     * @desc 通过Uri获取byte
     * @author wangjie
     * @date 2017/1/3 13:34
     */
    public static String getByteByUri(String uriString, Context mContext) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    mContext.getContentResolver(), Uri.parse(uriString));
            float wh = 1000f;// 这里设置高度为500f
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            if (w > wh || h > wh) {
                // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
                float be = 1f;// be=1表示不缩放
                if (w > h && w > wh) {// 如果宽度大的话根据宽度固定大小缩放
                    be = wh / w;
                } else if (w < h && h > wh) {// 如果高度高的话根据宽度固定大小缩放
                    be = wh / h;
                }
                if (be <= 0 || be > 1)
                    be = 1;
                Matrix matrix = new Matrix();
                matrix.postScale(be, be); //长和宽放大缩小的比例
                Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap
                        .getHeight(), matrix, true);
                return getByteByBitmap(resizeBmp);
            }
            return getByteByBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param bitmap 图片bitmap
     * @return String
     * @Description: TODO(这里用一句话描述这个方法的作用)图片bitmap转Base64
     * @author wangjie
     * @date 2015-6-30 上午11:36:40
     */
    public static String getByteByBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                // 图片kb大小
                int kb = baos.toByteArray().length / 1024;
                if (kb > 150) {
                    // 压缩百分比
                    int percent = 150 * 100 / kb;
                    // 重置baos即清空baos
                    baos.reset();
                    // 这里压缩percent%，把压缩后的数据存放到baos中
                    bitmap.compress(Bitmap.CompressFormat.JPEG, percent < 1 ? 1 : percent, baos);
                }
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                String result = Base64.encode(bitmapBytes);

                return result;
            }
        } catch (Exception e) {
            if (Looper.getMainLooper() != Looper.myLooper()) {
                Looper.prepare();
                ToastUtil.showToast("图片压缩失败");
                Looper.loop();
            } else {
                ToastUtil.showToast("图片压缩失败");
            }
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取图片文件的信息，是否旋转了90度，如果是则反转
     *
     * @param bitmap 需要旋转的图片
     * @param path   图片的路径
     */
    public static Bitmap reviewPicRotate(Bitmap bitmap, String path) {
        int degree = getPicRotate(path);
        if (degree != 0) {
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(degree); // 旋转angle度
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
        }
        return bitmap;
    }

    /**
     * 读取图片文件旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片旋转的角度
     */
    public static int getPicRotate(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
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

    public static Uri getContentUriFromFilePath(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media
                        .EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (new File(filePath).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media
                        .EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null,
// null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }
}