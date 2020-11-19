package com.cecilleo.core.base.util.fragmentnavigator;

import androidx.fragment.app.Fragment;

/**
 * Created by aspsine on 16/3/30.
 */
public interface FragmentNavigatorAdapter {

  Fragment onCreateFragment(int position);

  String getTag(int position);

  int getCount();
}