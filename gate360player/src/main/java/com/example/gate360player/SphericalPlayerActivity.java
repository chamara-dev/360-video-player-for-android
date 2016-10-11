/**
 * Copyright 2016-present, Facebook, Inc.
 * All rights reserved.
 *
 * Licensed under the Creative Commons CC BY-NC 4.0 Attribution-NonCommercial
 * License (the "License"). You may obtain a copy of the License at
 * https://creativecommons.org/licenses/by-nc/4.0/.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.gate360player;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.gate360player.SphericalVideoPlayer;


public class SphericalPlayerActivity extends Activity {

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0x1;

    private SphericalVideoPlayer videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_360video_player);
        videoPlayer = (SphericalVideoPlayer) findViewById(R.id.spherical_video_player);
        final Intent intent = getIntent();
        String videoPath = intent.getStringExtra("proxy");
        videoPlayer.setVideoURIPath(videoPath);
        videoPlayer.setPlayStaticVideo(intent.getBooleanExtra("isStaticVideo", true));
        videoPlayer.playWhenReady();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        requestExternalStoragePermission();
    }

    @Override
    protected void onPause() {
        super.onPause();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void requestExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            init();
        }
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(
                context,
                msg,
                Toast.LENGTH_SHORT).show();
    }

    private void init() {
        videoPlayer.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                videoPlayer.initRenderThread(surface, width, height);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                videoPlayer.releaseResources();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        });
        videoPlayer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {
        if (requestCode != PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            return;
        }

        if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            toast(this, "Access not granted for reading video file :(");
            return;
        }

        init();
    }
}
