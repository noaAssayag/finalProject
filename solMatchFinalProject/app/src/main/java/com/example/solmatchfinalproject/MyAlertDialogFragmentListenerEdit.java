package com.example.solmatchfinalproject;
/** The activity that creates an instance of  dialog fragment must
 * implement this interface in order to receive event callbacks.
 * Each method passes the DialogFragment in case the host needs to query it. */

public interface MyAlertDialogFragmentListenerEdit {
    public void onDialogPositiveClick(AlertDialogFragmentEdit dialog);
    public void onDialogNegativeClick(AlertDialogFragmentEdit dialog);
}
