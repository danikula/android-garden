package com.danikula.android.garden.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

/**
 * Содержит функциональность для удобного запуска активити и внешних приложений, таких как Market или звонилки
 * 
 * @author danik
 * 
 */
public class Intents {

    private static final String MIME_TYPE_TEXT_PLAIN = "text/plain";

    /**
     * Запускает приложение Android Market для просмотра информации о приложении
     * 
     * @param activity Activity активити, служащая для запуска Маркета
     * @param applicationsPackageName String имя пакета приложения, информацию о котором необходимо просмотреть
     * @throws ActivityNotFoundException если на девайсе отсутствует приложение Android Market
     */
    public static void openAndroidMarket(Activity activity, String applicationsPackageName) throws ActivityNotFoundException {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + applicationsPackageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    public static void openBrowser(Activity activity, String url) throws ActivityNotFoundException {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    public static void openEmailClient(Activity activity, String subject, String to, String body)
        throws ActivityNotFoundException {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + to));
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        activity.startActivity(emailIntent);
    }

    /**
     * Запускает приложение Android Market для просмотра информации о текущем приложении
     * 
     * @param activity Activity активити, служащая для запуска Маркета
     * @throws ActivityNotFoundException если на девайсе отсутствует приложение Android Market
     */
    public static void showAndroidMarket(Activity activity) throws ActivityNotFoundException {
        openAndroidMarket(activity, activity.getPackageName());
    }

    public static void restartActivity(Activity activity) {
        startActivity(activity, activity.getClass());
        activity.finish();
    }

    public static void startActivity(Activity activity, Class<? extends Activity> activityClassToStart) {
        Intent intent = new Intent(activity, activityClassToStart);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void shareText(Activity activity, String textToShare) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(MIME_TYPE_TEXT_PLAIN);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        activity.startActivity(shareIntent);
    }
    
    public static void shareText(Activity activity, int textIdToShare) {
        shareText(activity, activity.getString(textIdToShare));
    }
}
