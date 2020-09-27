package com.zolfagharipour.musicplayers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.zolfagharipour.musicplayers.fragment.MusicTabFragment;

import java.util.List;

public class MusicPlayersActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {



    public static final int REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE = 10;
    public static final String TAG = "tag";
    private static final String[] mPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_players);

        checkPermissions();

        if (getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, MusicTabFragment.newInstance()).commit();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE)
    private void checkPermissions() {
        if (!EasyPermissions.hasPermissions(this, mPermissions))
            EasyPermissions.requestPermissions(this, getString(R.string.message_to_get_permission), REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE, mPermissions);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            new AppSettingsDialog.Builder(this).build().show();
        else
            finish();

    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (!EasyPermissions.hasPermissions(this, mPermissions))
                finish();
        }
    }
}