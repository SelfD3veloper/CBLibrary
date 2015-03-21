package cbedoy.cblibrary.services;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import org.pademobile.application.ApplicationLoader;
import org.pademobile.business.category.interfaces.ICategoryInformationDelegate;
import org.pademobile.business.georeference.interfaces.IGeoReferenceInformationDelegate;
import org.pademobile.business.leftmenu.interfaces.ICountryInformationDelegate;
import org.pademobile.interfaces.IRestServiceCatchable;
import org.pademobile.com.interfaces.IRestService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Carlos Bedoy on 12/18/14.
 */
public class RestServiceCatchable implements IRestServiceCatchable
{

    private final String folderStorage;
    private IRestService restService;

    public void setRestService(IRestService restService) {
        this.restService = restService;
    }

    public RestServiceCatchable()
    {
        File external_files_dir = ApplicationLoader.mainContext.getExternalFilesDir(null);
        if(external_files_dir != null && this.isExternalStorageWritable())
        {
            this.folderStorage = external_files_dir.getAbsolutePath() + File.separator + "bills" + File.separator + "cache" + File.separator;
        }
        else
        {
            this.folderStorage = ApplicationLoader.mainContext.getFilesDir().getAbsolutePath() + File.separator + "bills" + File.separator + "cache" + File.separator;
        }
        new File(this.folderStorage).mkdirs();
    }

    public boolean writeCacheResponseForSHA(String sha, HashMap<String, Object> response)
    {
        DoWriteFile call = new DoWriteFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            call.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sha, response);
        } else {
            call.execute(sha, response);
        }
        return true;
    }


    private class DoWriteFile extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object... params)
        {
            String sha                                  = (String) params[0];
            HashMap<String, Object> response            = (HashMap<String, Object>) params[1];
            try
            {
                String path                             = folderStorage + sha;
                File file                               = new File(path);
                FileOutputStream fileOutputStream       = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream   = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(response);
                objectOutputStream.close();
            }
            catch (Exception e)
            {
                return false;
            }
            System.gc();
            return true;
        }
    }

    private class DoReadCache extends AsyncTask<String, Void, HashMap<String, Object>>{

        @Override
        protected HashMap<String, Object> doInBackground(String... params)
        {
            String sha = params[0];
            HashMap<String, Object> data                = new HashMap<String, Object>();
            try
            {
                String path                             = folderStorage + sha;
                File file = new File(path);
                FileInputStream fileInputStream         = new FileInputStream(file);
                ObjectInputStream objectInputStream     = new ObjectInputStream(fileInputStream);
                data                                    = (HashMap<String, Object>) objectInputStream.readObject();
                objectInputStream.close();
            }
            catch (Exception ignored)
            {

            }
            System.gc();
            return data;
        }
    }


    public boolean exitsFile(String sha)
    {
        String path = this.folderStorage + sha;
        File file = new File(path);
        return file.exists();
    }

    public HashMap<String, Object> readCacheResponseFromSHA(String sha)
    {
        HashMap<String, Object> data = new HashMap<String, Object>();
        DoReadCache call = new DoReadCache();
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                call.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sha);
            }
            else
            {
                call.execute(sha);
            }
            data = call.get();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return data;
    }

    private boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public void requestFromSHAWithHandler(String url, HashMap<String, Object> parameters, IRestService.IRestCallback callback, String SHA1, Object informationHandler)
    {
        if(exitsFile(SHA1))
        {
            HashMap<String, Object> cacheFromSHA = readCacheResponseFromSHA(SHA1);
            Boolean isValidCache = cacheFromSHA.containsKey("status") ? (Boolean) cacheFromSHA.get("status") : false;
            if(isValidCache)
            {
                if(informationHandler instanceof ICategoryInformationDelegate)
                {
                    ((ICategoryInformationDelegate) informationHandler).categoryResponse(cacheFromSHA);
                }
                else if(informationHandler instanceof IGeoReferenceInformationDelegate)
                {
                    ((IGeoReferenceInformationDelegate) informationHandler).geoReferenceResponse(cacheFromSHA);
                }
                else if(informationHandler instanceof ICountryInformationDelegate)
                {
                    ((ICountryInformationDelegate) informationHandler).countriesResponse(cacheFromSHA);
                }
            }
        }
        restService.request(url, parameters, callback);
    }



}
