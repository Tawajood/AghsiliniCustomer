package com.dotjoo.aghsilinicustomer.data.pagingSource.databinding;

import androidx.databinding.MergedDataBinderMapper;

public class DataBinderMapperImpl extends MergedDataBinderMapper {
  DataBinderMapperImpl() {
    addMapper(new com.dotjoo.aghsilinicustomer.DataBinderMapperImpl());
  }
}
