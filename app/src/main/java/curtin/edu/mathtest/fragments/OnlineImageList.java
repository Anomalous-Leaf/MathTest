package curtin.edu.mathtest.fragments;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import curtin.edu.mathtest.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnlineImageList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnlineImageList extends Fragment {

    private static final String SEARCH_TERM = "searchTerm";
    private static final String STUDENT_ID = "studentId";

    private String searchTerm;
    private int studentId;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;

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



        return view;
    }

    private class ImageViewHolder extends RecyclerView.ViewHolder
    {
        private final ViewGroup parent;
        private ImageView image;
        private View view;
        private Bitmap imageBitmap;

        public ImageViewHolder(View view, ViewGroup parent)
        {
            super(view);
            int size;

            this.view = view;

            this.parent = parent;

            //Get reference to image view
            image = view.findViewById(R.id.rowImage);

            //Set size for 3 images in a row
            size = ImageViewHolder.this.parent.getMeasuredWidth() / 3;
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
                    manager.setFragmentResult(ImageOptions.IMAGE_KEY, bundle);

                    manager.popBackStack();
                }
            });
        }

        public void bind(URL newUrl)
        {
            //Load new image into ImageView using AsyncTask
            new ImageLoadingTask().execute(newUrl);
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


}