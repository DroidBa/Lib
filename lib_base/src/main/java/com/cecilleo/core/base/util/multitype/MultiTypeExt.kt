package com.cecilleo.core.base.util.multitype

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseViewHolder<T : ViewBinding> private constructor(val mViewBinding: T) :
    RecyclerView.ViewHolder(mViewBinding.root) {
  constructor(
    parent: ViewGroup,
    creator: (inflater: LayoutInflater, root: ViewGroup, attachToRoot: Boolean) -> T
  ) : this(creator(LayoutInflater.from(parent.context), parent, false))
}

fun <T : ViewBinding> ViewGroup.getViewHolder(
  creator: (inflater: LayoutInflater, root: ViewGroup, attachToRoot: Boolean) -> T
): BaseViewHolder<T> = BaseViewHolder(this, creator)