package curtin.edu.mathtest.fragments;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;

import javax.net.ssl.HttpsURLConnection;

import curtin.edu.mathtest.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnlineImageList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnlineImageList extends Fragment {
    public static final String ONLINE_LIST_FRAGMENT = "onlineListFragment";
    private static final String SEARCH_TERM = "searchTerm";
    private static final String STUDENT_ID = "studentId";
    private static final String JSON_STRING = "jsonString";

    private String searchTerm;
    private int studentId;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private String jsonString;
    private List<URL> imageUrls;

    public OnlineImageList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param inSearchTerm Parameter 2.
     * @param inStudentId Parameter 1.
     * @return A new instance of fragment OnlineImageList.
     */
    // TODO: Rename and change types and number of parameters
    public static OnlineImageList newInstance(int inStudentId, String inSearchTerm) {
        OnlineImageList fragment = new OnlineImageList();
        Bundle args = new Bundle();
        args.putString(SEARCH_TERM, inSearchTerm);
        args.putInt(STUDENT_ID, inStudentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            searchTerm = getArguments().getString(SEARCH_TERM);
            studentId = getArguments().getInt(STUDENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_online_image_list, container, false);

        //Download list of image urls from pixabay server
        String urlString;
        String key = "23319229-94b52a4727158e1dc3fd5f2db";
        URL url;
        imageUrls = new ArrayList<>();

        urlString = Uri.parse("https://pixabay.com/api/").buildUpon().
                appendQueryParameter("key", key).
                appendQueryParameter("lang", "en").
                appendQueryParameter("q", searchTerm).
                appendQueryParameter("per_page", "50").build().toString();

        if (savedInstanceState == null)
        {
            //First time creating fragment. Download URLs
            try {
                url = new URL(urlString);

                //Download using AsyncTask
                //Will also parse json to urls
                new UrlListDownloader().execute(url);

            }
            catch (MalformedURLException e)
            {

                Toast.makeText(getActivity(), "Error connecting to URL", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            //Restore json string
            jsonString = savedInstanceState.getString(JSON_STRING);
            parseJsonString(jsonString);
        }

        //create list of images from list of image urls
        rv = view.findViewById(R.id.imageList);
        adapter = new ImageListAdapter(imageUrls);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);



        return view;
    }

    //AsyncTask for downloading image url list
    private class UrlListDownloader extends AsyncTask<URL, Void, String>
    {
        @Override
        protected String doInBackground(URL... urls)
        {
            //Code modified from practical 6
            HttpURLConnection conn = null;
            InputStream input;
            ByteArrayOutputStream baos;
            byte[] buffer;
            int bytesRead;
            String result = "";
            int nBytes = 0;


            try
            {
                conn = (HttpsURLConnection) urls[0].openConnection();

                Log.d("Download", "Code: " + conn.getResponseCode());
                //Check status
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                {
                    //Throw exception
                    Log.d("Download", "Failed to open connection");
                }
                else
                {
                    input = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    buffer = new byte[1024];
                    bytesRead = input.read(buffer);
                    while(bytesRead > 0)
                    {
                        nBytes = nBytes + bytesRead;
                        baos.write(buffer, 0, bytesRead);
                        bytesRead = input.read(buffer);
                    }
                    baos.close();
                    result = new String(baos.toByteArray());
                }
            }
            catch (IOException ioEx)
            {
                Log.e("Download", "IOException: " + ioEx.getMessage());
            }
            finally
            {
                if (conn != null)
                {
                    conn.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String json)
        {
            super.onPostExecute(json);

            parseJsonString(json);
        }
    }

    public void parseJsonString(String json)
    {
        jsonString = json;
        //Parse json
        JSONObject baseObject;
        JSONObject currObject;
        JSONArray jsonArray;
        List<String> urlStrings = new ArrayList<>();

        try {
            baseObject = new JSONObject(json);

            jsonArray = baseObject.getJSONArray("hits");

            //Loop up to 50 times to get maximum of 50 images
            for (int ii = 0; ii < 50 && ii < jsonArray.length(); ii++)
            {
                currObject = jsonArray.getJSONObject(ii);
                urlStrings.add(currObject.getString("largeImageURL"));

            }
        }
        catch (JSONException e)
        {
            Toast.makeText(getActivity(), "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //Convert list of string to list of URLs
        for (int ii = 0; ii < urlStrings.size(); ii++)
        {
            try {
                imageUrls.add(new URL(urlStrings.get(ii)));
                adapter.notifyDataSetChanged();
            }
            catch (MalformedURLException e)
            {
                //Just skip to next URL
            }
        }
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder
    {
        private final ViewGroup parent;
        private ImageView image;
        private View view;
        private Bitmap imageBitmap;
        private AsyncTask loadingTask;

        public ImageViewHolder(View view, ViewGroup parent)
        {
            super(view);
            int size;

            this.view = view;

            this.parent = parent;

            //Get reference to image view
            image = view.findViewById(R.id.rowImage);

            //Set size for 3 images in a row
            size = parent.getMeasuredWidth() / 3;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();

            //Set square size
            lp.width = size;
            lp.height = size;




            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager manager = getParentFragmentManager();
                    Uri imageUri;
                    File photoFile = new File(getActivity().getFilesDir(), studentId + ".jpg");
                    Bundle bundle = new Bundle();

                    //Try with resources
                    try (OutputStream outStream = new FileOutputStream(photoFile))
                    {
                        //Compress and save image to file
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outStream);
                    }
                    catch (IOException e)
                    {
                        Toast.makeText(getActivity(), "Failed to save image", Toast.LENGTH_SHORT);
                    }


                    //Build a Uri
                    imageUri = FileProvider.getUriForFile(getActivity(), "curtin.edu.fileprovider", photoFile);

                    //Return Uri to parent fragment and pop backstack
                    bundle.putParcelable(ImageOptions.IMAGE_KEY, imageUri);
                    manager.setFragmentResult(ONLINE_LIST_FRAGMENT, bundle);

                    manager.beginTransaction().remove(OnlineImageList.this).commit();
                    manager.popBackStack();
                }
            });
        }

        public void bind(URL newUrl)
        {
            //Check if previous task is running. If running, cancel before starting new
            if (loadingTask != null && loadingTask.getStatus().equals(AsyncTask.Status.RUNNING))
            {
                loadingTask.cancel(true);
            }

            //Load new image into ImageView using AsyncTask
            loadingTask = new ImageLoadingTask().execute(newUrl);
        }

        private class ImageLoadingTask extends AsyncTask<URL, Void, Bitmap>
        {
            @Override
            protected Bitmap doInBackground(URL... urls) {
                //Download image from URL
                Bitmap bitmap = null;
                InputStream stream;

                try {
                    stream =  urls[0].openStream();

                    bitmap = BitmapFactory.decodeStream(stream);
                }
                catch (IOException e)
                {
                    //Error opening image stream
                    Toast.makeText(getActivity(), "Error loading image", Toast.LENGTH_SHORT);
                }


                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                imageBitmap = bitmap;

                //Set the bitmap to be displayed in ImageView
                image.setImageBitmap(bitmap);


            }
        }
    }

    private class ImageListAdapter extends RecyclerView.Adapter<ImageViewHolder>
    {
        private List<URL> urlList;
        public ImageListAdapter(List<URL> inUrlList)
        {
            urlList = inUrlList;
        }

        @Override
        public int getItemCount()
        {
            return urlList.size();
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());

            View view = li.inflate(R.layout.image_row, parent, false);

            return new ImageViewHolder(view, parent);
        }

        @Override
        public void onBindViewHolder(ImageViewHolder cell, int index)
        {
            //Pass element to cell to bind
            cell.bind(urlList.get(index));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(JSON_STRING, jsonString);
    }
}