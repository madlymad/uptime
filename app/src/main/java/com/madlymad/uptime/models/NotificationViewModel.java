package com.madlymad.uptime.models;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.madlymad.uptime.MainActivityFragment;
import com.madlymad.uptime.Prefs;
import com.madlymad.uptime.models.data.NotificationLiveData;
import com.madlymad.uptime.models.objects.Times;


/**
 * A ViewModel used for the {@link MainActivityFragment}.
 */
public class NotificationViewModel extends AndroidViewModel {

    private final NotificationLiveData data;

    public NotificationViewModel(Application application) {
        super(application);
        data = new NotificationLiveData(application);
    }

    public LiveData<Times> getData() {
        return data;
    }

    public void setData(long elapsedTime) {
        if (elapsedTime != 0) {
            Prefs.setValue(getApplication(), Prefs.NOTIFY_MILLIS, elapsedTime);
            Prefs.setTimestampFromMilliseconds(getApplication(), elapsedTime);
        }
    }
}