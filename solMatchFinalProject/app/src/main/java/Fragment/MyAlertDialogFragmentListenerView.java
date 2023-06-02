package Fragment;

/** The activity that creates an instance of  dialog fragment must
 * implement this interface in order to receive event callbacks.
 * Each method passes the DialogFragment in case the host needs to query it. */

public interface MyAlertDialogFragmentListenerView {
    public void onDialogPositiveClick(AlertDialogFragmentViewHost dialog);

}
