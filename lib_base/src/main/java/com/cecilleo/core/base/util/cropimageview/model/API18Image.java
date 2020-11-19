package com.cecilleo.core.base.util.cropimageview.model;

import android.graphics.Matrix;
import com.cecilleo.core.base.util.cropimageview.CropImageView;

public class API18Image extends CropImage {

  API18Image(CropImageView imageView) {
    super(imageView);
  }

  @Override
  public Matrix getMatrix() {
    return cropImageView.getImageMatrix();
  }
}