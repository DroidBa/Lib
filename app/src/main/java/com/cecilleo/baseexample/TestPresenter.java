package com.cecilleo.baseexample;

import com.cecilleo.lib.mvp.MVPPresenter;
import com.cecilleo.lib.mvp.MVPView;

class TestPresenter extends MVPPresenter<TestView> {
  public TestPresenter(TestView view) {
    super(view);
  }
}
