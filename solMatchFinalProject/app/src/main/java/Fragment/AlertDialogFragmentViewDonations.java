package Fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.solmatchfinalproject.R;

import Model.Host;
import Model.donations;

public class AlertDialogFragmentViewDonations extends DialogFragment {
    TextView donationUserEmail,donationDescription,donationName;
    EditText category,donationLocation;
    ImageView donationImg;
    Button saveBtn;
    // Use this instance of the interface to deliver action events
    AletListener mListener;
    private donations donation;

    // Override the Fragment.onAttach() method to instantiate the MyAlertDialogFragmentListenerEdit
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // this.activity = activity;
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the MyAlertDialogFragmentListener so we can send events to the host
            mListener = (AletListener) activity;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getArguments();
        View v = inflater.inflate(R.layout.activity_alert_dialog_fragment_viewdonations, null);
        donationDescription=v.findViewById(R.id.donationDescription);
        category=v.findViewById(R.id.editTextCategory);
        donationLocation=v.findViewById(R.id.donationLocation);
        donationImg=v.findViewById(R.id.imageOfDonation);
        saveBtn=(Button) v.findViewById(R.id.saveBtn);
        if (b != null) {
            donations newdonation= (donations) b.getSerializable("Donation");
            donationDescription.setText(newdonation.getDescription());
            donationLocation.setText(newdonation.getAdress());
            category.setText(newdonation.getCatagory());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Glide.with(getContext())
                        .load(newdonation.getImg())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                // Handle image loading failure
                                Log.e("Glide", "Image loading failed: " + e.getMessage());
                                return false; // Return false to allow Glide to handle the error and show any error placeholder you have set
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                // Image successfully loaded
                                return false; // Return false to allow Glide to handle the resource and display it
                            }
                        })
                        .into(donationImg);
            }

        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogPositiveClick(AlertDialogFragmentViewDonations.this);
                getFragmentManager().beginTransaction().remove(AlertDialogFragmentViewDonations.this).commit();

            }
        });






        return v;

    }



}
