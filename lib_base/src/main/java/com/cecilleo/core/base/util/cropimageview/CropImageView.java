/*
 * Copyright 2015 Cesar Diez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cecilleo.core.base.util.cropimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import com.cecilleo.core.base.R;
import com.cecilleo.core.base.util.cropimageview.model.CropImage;
import com.cecilleo.core.base.util.cropimageview.model.CropImageFactory;
import com.cecilleo.core.base.util.cropimageview.model.CropType;

public class CropImageView extends AppCompatImageView {

  private CropImage cropImage;
  private int cropType = CropType.NONE;

  public CropImageView(Context context) {
    super(context);

    initImageView();
  }

  public CropImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CropImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

    parseAttributes(attrs);
    initImageView();
  }

  /**
   * Return the current crop type in use by this ImageView.
   *
   * @return a {@link CropType} in use by this ImageView
   */
  public
  @CropType
  int getCropType() {
    return cropType;
  }

  /**
   * Set crop type for the {@link ImageView}
   *
   * @param cropType A {@link CropType} desired scaling mode.
   */
  public void setCropType(@CropType int cropType) {
    this.cropType = cropType;

    setWillNotCacheDrawing(false);

    requestLayout();
    invalidate();
  }

  @Override
  protected boolean setFrame(int l, int t, int r, int b) {
    final boolean changed = super.setFrame(l, t, r, b);
    if (!isInEditMode() && cropImage != null && getDrawable() != null) {
      cropImage.computeImageTransformation();
    }
    return changed;
  }

  @Override
  public void setImageBitmap(Bitmap bm) {
    super.setImageBitmap(bm);
    initImageView();
  }

  private void parseAttributes(AttributeSet attrs) {
    final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.civ_CropImageView);
    cropType = a.getInt(R.styleable.civ_CropImageView_crop, CropType.NONE);

    a.recycle();
  }

  @Override
  public void setImageDrawable(Drawable drawable) {
    super.setImageDrawable(drawable);
    initImageView();
  }

  @Override
  public void setImageResource(int resId) {
    super.setImageResource(resId);
    initImageView();
  }

  private void initImageView() {
    setScaleType(ImageView.ScaleType.MATRIX);

    if (getDrawable() != null) {
      cropImage = new CropImageFactory().getCropImage(this);
    }
  }
}
