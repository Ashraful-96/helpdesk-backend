package com.aust.its.dto.passwordtoken;

import java.util.List;

public record TokenDataListView(
    List<TokenDataView> tokenDataViewList
) { }
