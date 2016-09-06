package be.evoliris.connectionapp.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Evoliris on 31/08/2016.
 */
public class GetImgAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;

    public GetImgAsyncTask(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        InputStream is = null;
        Bitmap image = null;
        try {

            String strUrl = "http://openweathermap.org/img/w/" + strings[0] + ".png";
            URL url = new URL(strUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            is = connection.getInputStream();
            // pas besoin du reader et tt ça ici
            image = BitmapFactory.decodeStream(is);

        } catch (IOException e){
            Log.e("GetImageAsyncTask", e.getMessage());
        }finally {
            if (is != null){
                try{
                    is.close();
                } catch (IOException e) {
                    Log.e("GetImageAsyncTask", e.getMessage());
                }
            }
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        imageView.setImageBitmap(bitmap);
    }
}
