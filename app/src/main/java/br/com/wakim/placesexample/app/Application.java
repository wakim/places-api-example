package br.com.wakim.placesexample.app;

/**
 * Created by wakim on 19/04/15.
 */
public class Application extends android.app.Application implements Thread.UncaughtExceptionHandler {

    private static Application sInstance;

    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static Application getInstance() {
        return sInstance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        mDefaultExceptionHandler.uncaughtException(thread, ex);
    }
}