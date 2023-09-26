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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.solmatchfinalproject.R;
import Model.Host;
import Model.UserStorageData;
public class AlertDialogFragmentViewHost extends DialogFragment {
    TextView hostingDescription,hostDetails;
    EditText hostingAddress,hostingDate;
    ImageView imageOfLocation;
    CheckBox checkboxaccomodation,checkboxPets,checkboxprivateRoom,checkboxsecureOption;
    Button saveBtn;

    // Use this instance of the interface to deliver action events
    MyAlertDialogFragmentListenerView mListener;
    private UserStorageData user;

    // Override the Fragment.onAttach() method to instantiate the MyAlertDialogFragmentListenerEdit
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // this.activity = activity;
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the MyAlertDialogFragmentListener so we can send events to the host
            mListener = (MyAlertDialogFragmentListenerView) activity;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getArguments();
        View v = inflater.inflate(R.layout.activity_alert_dialog_fragment_viewhost, null);
        hostingDescription=(TextView)v.findViewById(R.id.hostingDescription);
        hostingAddress=(EditText)v.findViewById(R.id.editTexthostingAddress);
        hostingDate=(EditText)v.findViewById(R.id.editTexthostingDate);
        imageOfLocation=(ImageView) v.findViewById(R.id.imageOfLocation);
        checkboxaccomodation=(CheckBox)v.findViewById(R.id.checkboxaccomodation);
        checkboxPets=(CheckBox)v.findViewById(R.id.checkboxPets);
        checkboxprivateRoom=(CheckBox)v.findViewById(R.id.checkboxprivateRoom);
        checkboxsecureOption=(CheckBox)v.findViewById(R.id.checkboxsecureOption);
        saveBtn=(Button) v.findViewById(R.id.saveBtn);
        if (b != null) {
            Host newHost= (Host) b.getSerializable("Host");
            if(newHost.getDescription()==null||newHost.getDescription().isEmpty()||newHost.getDescription().equals(""))
            {
                hostingDescription.setText(R.string.secTitleFragment);
            }
            else{
                hostingDescription.setText(newHost.getDescription());
            }



            hostingAddress.setText(newHost.getHostAddress());
            hostingDate.setText(newHost.getHostingDate());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Glide.with(getContext())
                        .load(newHost.getHostingLocImg())
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
                        .into(imageOfLocation);
            }
            if(newHost.getAccommodation().equals("true"))
            {
                checkboxaccomodation.setChecked(true);
                checkboxaccomodation.setClickable(false);
            }
            if(newHost.getPets().equals("true"))
            {
                checkboxPets.setChecked(true);
                checkboxPets.setClickable(false);
            }
             if(newHost.getPrivateRoom().equals("true"))
            {
                checkboxprivateRoom.setChecked(true);
                checkboxprivateRoom.setClickable(false);
            }
             if(newHost.getSecureEnv().equals("true")) {
                checkboxsecureOption.setChecked(true);
                checkboxsecureOption.setClickable(false);
            }

        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogPositiveClick(AlertDialogFragmentViewHost.this);
                dismiss();
            }
        });
        return v;

    }


}