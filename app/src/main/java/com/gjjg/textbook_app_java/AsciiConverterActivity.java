package com.gjjg.textbook_app_java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AsciiConverterActivity extends AppCompatActivity {

    private TextView asciiTextView;
    private Button postButton, style1Button, style2Button, style3Button, style4Button;
    private ImageButton copyButton;
    private int style = 1;

    private Uri galleryImageUri, cameraImageUri;
    private Bitmap imageBitmap;

    private static final int ACCESS_CAMERA = 1000, PICK_IMAGE = 1100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ascii_converter);

        if (ContextCompat.checkSelfPermission(AsciiConverterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AsciiConverterActivity.this, new String[] {Manifest.permission.CAMERA}, ACCESS_CAMERA);
        }

        asciiTextView = (TextView) findViewById(R.id.asciiTextView);
        postButton = (Button) findViewById(R.id.postButton);
        copyButton = (ImageButton) findViewById(R.id.copyButton);

        style1Button = (Button) findViewById(R.id.style1Button);
        style2Button = (Button) findViewById(R.id.style2Button);
        style3Button = (Button) findViewById(R.id.style3Button);
        style4Button = (Button) findViewById(R.id.style4Button);

        registerForContextMenu(asciiTextView);

        style1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                style = 1;
                asciiTextView.setText(ConvertAsciiImage(imageBitmap));
            }
        });
        style2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                style = 2;
                asciiTextView.setText(ConvertAsciiImage(imageBitmap));
            }
        });
        style3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                style = 3;
                asciiTextView.setText(ConvertAsciiImage(imageBitmap));
            }
        });
        style4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                style = 4;
                asciiTextView.setText(ConvertAsciiImage(imageBitmap));
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ASCII Art", asciiTextView.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(AsciiConverterActivity.this, "ASCII Art copied!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Choose import type");
        getMenuInflater().inflate(R.menu.photo_import, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cameraMenuButton:
                CameraButton();
                return true;
            case R.id.galleryMenuButton:
                GalleryButton();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void GalleryButton(){
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery, "Select a picture"), PICK_IMAGE);
    }

    private void CameraButton(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the greyscale APP");
        cameraImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(camera, ACCESS_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            galleryImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), galleryImageUri);
                imageBitmap = bitmap;
                asciiTextView.setText(ConvertAsciiImage(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == ACCESS_CAMERA && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), cameraImageUri);
                imageBitmap = bitmap;
                asciiTextView.setText(ConvertAsciiImage(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap ResizeImage(Bitmap unresizedImage, int height, int width) {
        return Bitmap.createScaledBitmap(unresizedImage, width, height, false);
    }

    private String ConvertAsciiImage(Bitmap originalImage) {
        Bitmap resizedImage = ResizeImage(originalImage, 100, 100);
        StringBuilder ASCII = new StringBuilder();

        int A, R, G, B;
        int colorPixel;
        int width = resizedImage.getWidth();
        int height = resizedImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                colorPixel = resizedImage.getPixel(x, y);
                A = Color.alpha(colorPixel);
                R = Color.red(colorPixel);
                G = Color.green(colorPixel);
                B = Color.blue(colorPixel);
                int greyScale = (R + G + B) / 3;

                if (A > 0) {
                    switch (style) {
                        case 1:
                            if (greyScale < 31.875) {
                                ASCII.append("█");
                            } else if (greyScale < 63.75) {
                                ASCII.append("▉");
                            } else if (greyScale < 95.625) {
                                ASCII.append("▊");
                            } else if (greyScale < 127.5) {
                                ASCII.append("▋");
                            } else if (greyScale < 159.375) {
                                ASCII.append("▌");
                            } else if (greyScale < 191.25) {
                                ASCII.append("▍");
                            } else if (greyScale < 223.125) {
                                ASCII.append("▎");
                            } else if (greyScale <= 255) {
                                ASCII.append("▏");
                            }
                            break;
                        case 2:
                            if (greyScale < 63.75) {
                                ASCII.append("█");
                            } else if (greyScale < 127.5) {
                                ASCII.append("▓");
                            } else if (greyScale < 191.25) {
                                ASCII.append("▒");
                            } else if (greyScale <= 255) {
                                ASCII.append("░");
                            }
                            break;
                        case 3:
                            if (greyScale < 25.5) {
                                ASCII.append("@");
                            } else if (greyScale < 51) {
                                ASCII.append("&");
                            } else if (greyScale < 76.5) {
                                ASCII.append("%");
                            } else if (greyScale < 102) {
                                ASCII.append("#");
                            } else if (greyScale < 127.5) {
                                ASCII.append("*");
                            } else if (greyScale < 153) {
                                ASCII.append("+");
                            } else if (greyScale < 178.5) {
                                ASCII.append("=");
                            } else if (greyScale < 204) {
                                ASCII.append("-");
                            } else if (greyScale < 229.5) {
                                ASCII.append(",");
                            } else if (greyScale <= 255) {
                                ASCII.append(".");
                            }
                            break;
                        case 4:
                            if (greyScale < 21.25) {
                                ASCII.append("X");
                            } else if (greyScale < 42.5) {
                                ASCII.append("W");
                            } else if (greyScale < 63.75) {
                                ASCII.append("M");
                            } else if (greyScale < 85) {
                                ASCII.append("G");
                            } else if (greyScale < 106.25) {
                                ASCII.append("O");
                            } else if (greyScale < 127.5) {
                                ASCII.append("N");
                            } else if (greyScale < 148.75) {
                                ASCII.append("H");
                            } else if (greyScale < 170) {
                                ASCII.append("A");
                            } else if (greyScale < 191.25) {
                                ASCII.append("Y");
                            } else if (greyScale < 212.5) {
                                ASCII.append("L");
                            } else if (greyScale < 233.75) {
                                ASCII.append("J");
                            } else if (greyScale < 255) {
                                ASCII.append("I");
                            }
                            break;
                    }
                } else {
                    ASCII.append(" ");
                }
            }
            ASCII.append("\n");
        }

        style1Button.setEnabled(true);
        style2Button.setEnabled(true);
        style3Button.setEnabled(true);
        style4Button.setEnabled(true);
        postButton.setEnabled(true);

        asciiTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 10);
        asciiTextView.setTextColor(Color.BLACK);

        if (style == 3 || style == 4) {
            asciiTextView.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        } else {
            asciiTextView.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
        }

        return ASCII.toString();
    }
}