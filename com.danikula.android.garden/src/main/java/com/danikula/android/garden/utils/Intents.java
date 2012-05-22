package com.danikula.android.garden.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Содержит функциональность для удобного запуска активити и внешних приложений, таких как Market или звонилки
 * 
 * @author danik
 * 
 */
public class Intents {

    private static final String AUDIO_MIME = "audio/*";

    private static final String MIME_TYPE_TEXT_PLAIN = "text/plain";
    
    private static final String PHONE_PREFIX = "tel://";

    private static final String YOUTUBE_PREFIX = "vnd.youtube:";
    
    /**
     * Запускает приложение Android Market для просмотра информации о приложении
     * 
     * @param activity Activity активити, служащая для запуска Маркета
     * @param applicationsPackageName String имя пакета приложения, информацию о котором необходимо просмотреть
     * @throws ActivityNotFoundException если на девайсе отсутствует приложение Android Market
     */
    public static void openAndroidMarket(Context context, String applicationsPackageName) throws ActivityNotFoundException {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + applicationsPackageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    public static void openBrowser(Context context, String url) throws ActivityNotFoundException {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }
    
    /**
     * Starts youtube application to watch video
     * @param context context to be used for launching youtube application 
     * @param videoId youtube video id
     * @throws ActivityNotFoundException if there is no installed app for watching video 
     */
    public static void watchYoutube(Context context, String videoId) throws ActivityNotFoundException {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_PREFIX + videoId));
        context.startActivity(intent);
    }
    
    public static void streamAudio(Context context, String audioUrl) throws ActivityNotFoundException {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW); 
        intent.setDataAndType(Uri.parse(audioUrl), AUDIO_MIME); 
        context.startActivity(intent);
    }

    public static void openEmailClient(Context context, String subject, String to, String body)
        throws ActivityNotFoundException {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + to));
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(emailIntent);
    }
    
    /**
     * Opens dialer and call to specified number.
     * 
     * <p>Note this method requires permission <b>android.permission.CALL_PHONE</b></p>
     * @param context context used for starting dialer
     * @param phone phone to call
     * @throws ActivityNotFoundException if device has'n dialer
     */
    public static void call(Context context, String phone) throws ActivityNotFoundException {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(PHONE_PREFIX + phone));
        context.startActivity(callIntent);
    }

    /**
     * Запускает приложение Android Market для просмотра информации о текущем приложении
     * 
     * @param context активити, служащая для запуска Маркета
     * @throws ActivityNotFoundException если на девайсе отсутствует приложение Android Market
     */
    public static void showAndroidMarket(Context context) throws ActivityNotFoundException {
        openAndroidMarket(context, context.getPackageName());
    }

    public static void restartActivity(Activity activity) {
        startActivity(activity, activity.getClass());
        activity.finish();
    }

    public static void startActivity(Context context, Class<? extends Activity> activityClassToStart) {
        Intent intent = new Intent(context, activityClassToStart);
        context.startActivity(intent);
    }

    public static void shareText(Context context, String textToShare) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(MIME_TYPE_TEXT_PLAIN);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        context.startActivity(shareIntent);
    }
    
    public static void shareText(Context context, int textIdToShare) {
        shareText(context, context.getString(textIdToShare));
    }
}
