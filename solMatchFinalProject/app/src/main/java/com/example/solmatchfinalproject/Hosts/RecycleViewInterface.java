package com.example.solmatchfinalproject.Hosts;

import android.view.View;

public interface RecycleViewInterface {
    void onItemClick(int position);
    void deleteItem(int position);
    void onDonationClick(int position, View view);
    void deleteDonation(int position);
    void AddComments(int position);


}

