package io.github.gaomjun.gallary.media_provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq on 12/12/2016.
 */

public class MediaProvider {

    public static MediaProvider Instance;
    private final ContentResolver contentResolver;

    private ArrayList<MediaItem> media = new ArrayList<>();

    public MediaProvider(Context context) {
        this.contentResolver = context.getContentResolver();
        Instance = this;
    }

    public ArrayList<MediaItem> getMedia() {
        return media;
    }

    public String[] getMediaPaths() {
        String[] mediaPaths = new String[media.size()];

        for (int i = 0; i < media.size(); i++) {
            mediaPaths[i] = media.get(i).getPath();
        }
        return mediaPaths;
    }

    public boolean[] getMediaTypes() {
        boolean[] mediaTypes = new boolean[media.size()];
        for (int i = 0; i < media.size(); i++) {
            mediaTypes[i] = media.get(i).isVideo();
        }
        return mediaTypes;
    }

    public void asyncGetMedia(final ScanStatusCallbak scanStatusCallbak) {
        this.scanStatusCallbak = scanStatusCallbak;

        new Thread(new Runnable() {
            @Override
            public void run() {
                scanMedia();

//                scanStatusCallbak.scanFinish(media);
            }
        }).start();
    }

    private void scanMedia() {
        Cursor cursor = contentResolver.query(MediaStore.Files.getContentUri("external"), null,
                MediaStore.Images.Media.DATA + " like ? ",
                new String[]{"%DCIM/Ringo%"},
                null);

        int imageColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
        int imagePathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

        media.clear();
        for (int i = 1; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            boolean isVideo = false;
            Bitmap thumbnail = MediaStore.Images.Thumbnails.getThumbnail(contentResolver,
                    cursor.getInt(imageColumnIndex),
                    MediaStore.Images.Thumbnails.MINI_KIND, null);
            if (thumbnail == null) {
                thumbnail = MediaStore.Video.Thumbnails.getThumbnail(contentResolver,
                        cursor.getInt(imageColumnIndex), MediaStore.Video.Thumbnails.MINI_KIND, null);
                isVideo = true;
            }
            String imagePath = cursor.getString(imagePathIndex);

            MediaItem mediaItem = new MediaItem();
            mediaItem.setThumbnail(thumbnail);
            mediaItem.setPath(imagePath);
            mediaItem.setVideo(isVideo);

            media.add(mediaItem);
            scanStatusCallbak.scanning(media);

            Log.d("MediaProvider", imagePath);
        }
        cursor.close();
    }

    private ScanStatusCallbak scanStatusCallbak;

    public interface ScanStatusCallbak {
        void scanning(List<MediaItem> media);
//        void scanFinish(List<MediaItem> media);
    }
}
