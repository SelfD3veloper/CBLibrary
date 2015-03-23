/**
 * Created by Carlos Bedoy on 15/05/14.
 * exchange-android - Pademobile
 */
package cbedoy.cblibrary.services;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;


import java.util.concurrent.ExecutionException;


public class BlurService {

    public static BlurService instance;


    public static BlurService getInstance(){
        if(instance == null){
            instance = new BlurService();
        }
        return instance;
    }


    public Bitmap blurRenderScript(Bitmap smallBitmap)
    {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= 16)
        {
            bitmap = supportedBlur(smallBitmap, 25);
        }
        return bitmap;
    }

    public Bitmap blurRenderScript(Bitmap smallBitmap, int radius)
    {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= 16)
        {
            bitmap = supportedBlur(smallBitmap, radius);
        }
        return bitmap;
    }

    public Bitmap performRequestBlurByImage(Bitmap bitmap)
    {
        DoAsyncBlur doAsyncBlur = new DoAsyncBlur();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            doAsyncBlur.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bitmap);
        } else {
            doAsyncBlur.execute(bitmap);
        }
        try {
            return doAsyncBlur.get();
        } catch (InterruptedException e) {
            return bitmap;
        } catch (ExecutionException e) {
            return bitmap;
        }
    }

    public Bitmap performRequestBlurByImageWithRadius(Bitmap bitmap, int radius)
    {
        DoAsyncBlurWithRadius doAsyncBlur = new DoAsyncBlurWithRadius();
        doAsyncBlur.setRadius(radius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            doAsyncBlur.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bitmap);
        } else {
            doAsyncBlur.execute(bitmap);
        }
        try {
            return doAsyncBlur.get();
        } catch (InterruptedException e) {
            return bitmap;
        } catch (ExecutionException e) {
            return bitmap;
        }
    }

    private class DoAsyncBlur extends AsyncTask<Bitmap, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            return blurRenderScript(bitmaps[0]);
        }
    }

    private class DoAsyncBlurWithRadius extends AsyncTask<Bitmap, Void, Bitmap> {
        private int radius;

        public void setRadius(int radius) {
            this.radius = radius;
        }

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            return blurRenderScript(bitmaps[0], radius);
        }
    }

    private Bitmap supportedBlur(Bitmap sentBitmap, int radius)
    {
        Bitmap output = Bitmap.createBitmap(sentBitmap.getWidth(), sentBitmap.getHeight(), sentBitmap.getConfig());
        RenderScript rs = RenderScript.create(ApplicationLoader.mainContext);
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation inAlloc = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_GRAPHICS_TEXTURE);
        Allocation outAlloc = Allocation.createFromBitmap(rs, output);
        script.setRadius(radius > 25 ? 25 : radius);
        script.setInput(inAlloc);
        script.forEach(outAlloc);
        outAlloc.copyTo(output);
        rs.destroy();
        return output;
    }



}
